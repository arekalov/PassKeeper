package com.example.passkeeper.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.example.data.security.RepositorySecurity
import com.example.passkeeper.BiometricAuthListener
import com.example.passkeeper.PasswordsValidationState
import com.google.crypto.tink.Aead
import kotlin.io.encoding.ExperimentalEncodingApi

class LoginViewModel(
    private val encryptedSharedPreferences: SharedPreferences,
    private val aead: Aead
) :
    ViewModel() {
    private val PASS_NAME = "pass"
    private val SALT_NAME = "salt"
    val securityRepo = RepositorySecurity(aead)

    fun signUp(pass: String) {
        val (encryptedPass, salt) = securityRepo.encodePin(pass)
        encryptedSharedPreferences.edit().putString(PASS_NAME, encryptedPass).apply()
        encryptedSharedPreferences.edit().putString(SALT_NAME, salt).apply()
    }
    @OptIn(ExperimentalEncodingApi::class)
    fun checkPin(pin: String): Boolean {
        val pass = encryptedSharedPreferences.getString(PASS_NAME, "?") ?: "errorpass"
        val salt = encryptedSharedPreferences.getString(SALT_NAME, "?") ?: "errorsalt"
        return securityRepo.pinIsValid(pin, pass, salt)
    }

    fun checkHaveAccount(): Boolean {
        return encryptedSharedPreferences.contains("pass") && encryptedSharedPreferences.contains("salt")
    }

    fun validateNewPasswords(first: String, second: String): PasswordsValidationState {
        if (validatePassword(first) && first == second) {
            return PasswordsValidationState.OK
        } else if (first != second) {
            return PasswordsValidationState.DIFFERENT
        }
        return PasswordsValidationState.INCORRECT
    }

    private fun validatePassword(password: String): Boolean {
        if (password.length !in 8..30) {
            return false
        }

        var hasUpperCase = false
        var hasLowerCase = false
        for (char in password) {
            if (char.isUpperCase()) {
                hasUpperCase = true
            }
            if (char.isLowerCase()) {
                hasLowerCase = true
            }
        }
        if (!hasUpperCase || !hasLowerCase) {
            return false
        }

        var hasDigit = false
        for (char in password) {
            if (char.isDigit()) {
                hasDigit = true
                break
            }
        }
        if (!hasDigit) {
            return false
        }

        if (password.isBlank()) {
            return false
        }
        return true
    }

    fun showBiometricPrompt(
        title: String,
        subtitle: String = "",
        description: String = "",
        activity: FragmentActivity?,
        listener: BiometricAuthListener,
        cryptoObject: BiometricPrompt.CryptoObject? = null,
    ) {
        val promptInfo = setBiometricPromptInfo(
            title,
            subtitle,
            description,
        )

        val biometricPrompt = initBiometricPrompt(activity, listener)
        biometricPrompt.apply {
            if (cryptoObject == null) authenticate(promptInfo)
            else authenticate(promptInfo, cryptoObject)
        }
    }
    private fun hasBiometricCapability(context: Context): Int {
        return BiometricManager.from(context).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
    }

    fun isBiometricReady(context: Context) =
        hasBiometricCapability(context) == BiometricManager.BIOMETRIC_SUCCESS

    private fun setBiometricPromptInfo(
        title: String,
        subtitle: String,
        description: String,
    ): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()

    }

    private fun initBiometricPrompt(
        activity: FragmentActivity?,
        listener: BiometricAuthListener
    ): BiometricPrompt {
        val executor = activity?.let { ContextCompat.getMainExecutor(it) }
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                listener.onBiometricAuthenticateError(errorCode, errString.toString())
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                listener.onBiometricAuthenticateSuccess(result)
            }
        }
        return BiometricPrompt(activity!!, executor!!, callback)
    }

}