package com.example.passkeeper

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.data.security.RepositorySecurity
import com.example.passkeeper.viewmodels.LoginViewModel
import com.example.passkeeper.viewmodels.LoginViewModelFactory
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AeadFactory
import com.google.crypto.tink.aead.AeadKeyTemplates


class MainActivity : AppCompatActivity() {
    private lateinit var encryptedSharedPreferences: SharedPreferences
    lateinit var loginViewModel: LoginViewModel
    val PREF_NAME = "preferences"
    val PASS_NAME = "pass"
    val SALT_NAME = "salt"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    private fun initComponents() {
        initEncryptedSharePreferences()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
    }

    private fun initEncryptedSharePreferences() {
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
        val viewModelFactory = LoginViewModelFactory(encryptedSharedPreferences, aead)
        loginViewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }
}