package com.arekalov.passkeeper.utils

import androidx.biometric.BiometricPrompt

interface BiometricAuthListener {

    fun onBiometricAuthenticateError(error: Int, errMsg: String)
    fun onBiometricAuthenticateSuccess(result: BiometricPrompt.AuthenticationResult)

}