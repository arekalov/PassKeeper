package com.example.passkeeper

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.data.security.RepositorySecurity
import com.example.passkeeper.viewmodels.LoginViewModel
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AeadFactory
import com.google.crypto.tink.aead.AeadKeyTemplates


class MainActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }
    lateinit var repository: RepositorySecurity

    lateinit var encryptedSharedPreferences: SharedPreferences
    private val PREF_NAME = "preferences"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        encryptedSharedPreferences = EncryptedSharedPreferences.create(
            PREF_NAME,
            masterKeyAlias,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        AeadConfig.register()
        val keysetHandle = KeysetHandle.generateNew(
            AeadKeyTemplates.AES256_GCM
        )
        val aead = AeadFactory.getPrimitive(keysetHandle)


        repository = RepositorySecurity(aead)
        val (pin, salt) = repository.encodePin("123")
        encryptedSharedPreferences.edit().putString("salt", salt).apply()
        encryptedSharedPreferences.edit().putString("pass", pin).apply()
        Log.e("!!!", "old_pin: 123")
        Log.e("!!!", "old_salt: $salt")
        Log.e("!!!", "old_pass: $pin")
    }
}