package com.arekalov.passkeeper.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.navigation.fragment.findNavController
import com.arekalov.passkeeper.MainActivity
import com.arekalov.passkeeper.R
import com.arekalov.passkeeper.databinding.FragmentSettingsBinding
import com.arekalov.passkeeper.utils.BiometricAuthListener
import com.arekalov.passkeeper.utils.PasswordsValidationState
import com.arekalov.passkeeper.viewmodels.LoginViewModel

class SettingsFragment : Fragment(), BiometricAuthListener {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = (activity as MainActivity).loginViewModel

        changePassBtnOnClick()
        changePasswordWithBio()
        editPassInputsListener()
    }

    private fun changePasswordWithBio() {
        binding.btnFingerprint.setOnClickListener {
            val pass1 = binding.tietPass1.text.toString()
            val pass2 = binding.tietPass2.text.toString()
            when (loginViewModel.validateNewPasswords(pass1, pass2)) {
                PasswordsValidationState.OK -> {
                    if (loginViewModel.checkHaveAccount()) {
                        if (loginViewModel.isBiometricReady(requireContext())) {
                            loginViewModel.showBiometricPrompt(
                                title = resources.getString(R.string.fingerprint_title),
                                activity = activity,
                                listener = this,
                                cryptoObject = null,
                            )
                        }
                    } else {
                        Toast.makeText(
                            activity,
                            resources.getString(R.string.dont_have_account),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                PasswordsValidationState.INCORRECT -> incorrectPasswords()
                PasswordsValidationState.DIFFERENT -> differentPasswords()
            }
        }
    }

    private fun changePassBtnOnClick() {
        binding.btnChangePass.setOnClickListener {
            changePassword()
        }
    }

    private fun changePassword() {
        val oldPass = binding.tietOldPass.text.toString()
        val pass1 = binding.tietPass1.text.toString()
        val pass2 = binding.tietPass2.text.toString()
        if (loginViewModel.checkPin(oldPass) && oldPass != pass1) {
            when (loginViewModel.validateNewPasswords(pass1, pass2)) {
                PasswordsValidationState.OK -> success(pass1)
                PasswordsValidationState.INCORRECT -> incorrectPasswords()
                PasswordsValidationState.DIFFERENT -> differentPasswords()
            }
        } else if (oldPass == pass1 && loginViewModel.checkPin(oldPass)) {
            Toast.makeText(activity, resources.getString(R.string.passwords_same), Toast.LENGTH_SHORT).show()
            binding.tilPass1.error = resources.getString(R.string.passwords_same)
        } else {
            binding.tilOldPass.error = resources.getString(R.string.incorrect_password)
            Toast.makeText(
                activity,
                resources.getString(R.string.incorrect_password),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun incorrectPasswords() {
        binding.tilPass1.error = resources.getString(R.string.incorrect_pass_message)
        Toast.makeText(activity, resources.getString(R.string.try_again), Toast.LENGTH_SHORT).show()
    }

    private fun success(password: String) {
        loginViewModel.signUp(password)
        Toast.makeText(
            activity,
            resources.getString(R.string.success_password_change),
            Toast.LENGTH_SHORT
        )
            .show()
        findNavController().popBackStack()
    }

    private fun differentPasswords() {
        binding.tilPass2.error = resources.getString(R.string.different_pass_message)
        Toast.makeText(activity, resources.getString(R.string.try_again), Toast.LENGTH_SHORT).show()
    }

    private fun editPassInputsListener() {
        binding.tietPass1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tilPass1.isErrorEnabled = false
                binding.tilPass2.isErrorEnabled = false
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.tietPass2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tilPass2.isErrorEnabled = false
                binding.tilPass1.isErrorEnabled = false
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.tietOldPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tilOldPass.isErrorEnabled = false
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun onBiometricAuthenticateError(error: Int, errMsg: String) {
        Toast.makeText(activity, resources.getString(R.string.try_again), Toast.LENGTH_SHORT).show()
    }

    override fun onBiometricAuthenticateSuccess(result: BiometricPrompt.AuthenticationResult) {
        success(binding.tietPass1.text.toString())
    }
}