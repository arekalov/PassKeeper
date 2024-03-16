package com.example.passkeeper.fragments

import android.app.Instrumentation
import android.content.Context

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.example.passkeeper.BiometricAuthListener
import com.example.passkeeper.MainActivity
import com.example.passkeeper.R
import com.example.passkeeper.databinding.FragmentLoginBinding
import com.example.passkeeper.viewmodels.LoginViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginFragment : Fragment(), BiometricAuthListener {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var logInViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        logInViewModel = activity.loginViewModel

        logInBtnClickOn()
        signUpBtnClickOn()
        fingerprintBtnClickOn()
    }



    private fun fingerprintBtnClickOn() {
        binding.btnFingerprint.setOnClickListener {
            if (logInViewModel.checkHaveAccount()) {
                if (logInViewModel.isBiometricReady(requireContext())) {
                    logInViewModel.showBiometricPrompt(
                        title = resources.getString(R.string.fingerprint_title),
                        activity = activity,
                        listener = this,
                        cryptoObject = null,
                    )
                }
            } else {
                Toast.makeText(activity, resources.getString(R.string.dont_have_account), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBiometricAuthenticateError(error: Int, errMsg: String) {
        Toast.makeText(activity, resources.getString(R.string.try_again), Toast.LENGTH_SHORT).show()
    }
    override fun onBiometricAuthenticateSuccess(result: BiometricPrompt.AuthenticationResult) {
        accessIsAllowed()
    }



    private fun signUpBtnClickOn() {
        binding.btnSignUp.setOnClickListener {
            if (logInViewModel.checkHaveAccount()) {
                Toast.makeText(
                    activity,
                    resources.getString(R.string.have_account),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val action = LoginFragmentDirections.actionLoginFragment2ToSignUpFragment()
                findNavController().navigate(action)
            }
        }
    }


    private fun logInBtnClickOn() {
      binding.btnLogin.setOnClickListener {
          if (!logInViewModel.checkHaveAccount()) {
              Toast.makeText(activity, resources.getString(R.string.dont_have_account), Toast.LENGTH_SHORT).show()
          }
          else {
              val enterEdPin = binding.editTextPassword.text.toString()
              if (logInViewModel.checkPin(enterEdPin)) {
                    accessIsAllowed()
              }
              else {
                  Toast.makeText(activity, resources.getString(R.string.incorrect_password), Toast.LENGTH_SHORT).show()
              }
          }
      }
    }

    private fun accessIsAllowed() {
        val action = LoginFragmentDirections.actionLoginFragment2ToHomeFragment()
        Toast.makeText(activity, resources.getString(R.string.welcome), Toast.LENGTH_SHORT).show()
        binding.editTextPassword.text?.clear()
        findNavController().navigate(action)
    }


}