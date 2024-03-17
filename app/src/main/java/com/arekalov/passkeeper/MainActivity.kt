package com.arekalov.passkeeper

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.arekalov.passkeeper.viewmodels.LoginViewModel
import com.arekalov.passkeeper.viewmodels.LoginViewModelFactory
import com.google.crypto.tink.Aead
import com.google.crypto.tink.CleartextKeysetHandle
import com.google.crypto.tink.JsonKeysetReader
import com.google.crypto.tink.JsonKeysetWriter
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AeadFactory
import com.google.crypto.tink.aead.AeadKeyTemplates

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


class MainActivity : AppCompatActivity() {
    private lateinit var encryptedSharedPreferences: SharedPreferences
    lateinit var loginViewModel: LoginViewModel
    lateinit var keysetHandle: KeysetHandle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    private fun initComponents() {
        initEncryptedSharePreferencesAndAead()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
    }

    private fun initEncryptedSharePreferencesAndAead() {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        encryptedSharedPreferences = EncryptedSharedPreferences.create(
            Companion.PREF_NAME,
            masterKeyAlias,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        AeadConfig.register()
        val aead = createAead()
        val viewModelFactory = LoginViewModelFactory(encryptedSharedPreferences, aead)
        loginViewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    private fun createAead(): Aead {
        if (encryptedSharedPreferences.contains(KEY_ALIAS)) {
            val a = encryptedSharedPreferences.getString(KEY_ALIAS, "?")
            keysetHandle = stringToKeysetHandle(a!!)
        } else {
            keysetHandle = KeysetHandle.generateNew(
                AeadKeyTemplates.AES256_GCM
            )
            val encoded = keysetHandleToString(keysetHandle)
            encryptedSharedPreferences.edit().putString(KEY_ALIAS, encoded).apply()
        }
        val aead = AeadFactory.getPrimitive(
            keysetHandle
        )
        return aead
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun keysetHandleToString(keysetHandle: KeysetHandle): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        CleartextKeysetHandle.write(
            keysetHandle,
            JsonKeysetWriter.withOutputStream(byteArrayOutputStream)
        )
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encode(byteArray)
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun stringToKeysetHandle(encodedKeysetHandle: String): KeysetHandle {
        val byteArray = Base64.decode(encodedKeysetHandle)
        val inputStream = ByteArrayInputStream(byteArray)
        return CleartextKeysetHandle.read(JsonKeysetReader.withInputStream(inputStream))
    }


    companion object {
        const val PREF_NAME = "preferences"
        private const val KEY_ALIAS = "key"
    }
}