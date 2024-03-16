package com.example.passkeeper

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.passkeeper.databinding.FragmentSignUpBinding
import com.example.passkeeper.viewmodels.LoginViewModel


class SignUpFragment : Fragment() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = (activity as MainActivity).loginViewModel
        signUpBtnOnClick()
        editPassInputsListener()

    }

    private fun editPassInputsListener() {
        binding.tietPass1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tilPass1.isErrorEnabled = false
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.tietPass2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tilPass2.isErrorEnabled = false
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun signUpBtnOnClick() {
        binding.btnSignUp.setOnClickListener {
            val pass1 = binding.tietPass1.text.toString()
            val pass2 = binding.tietPass2.text.toString()
            when (loginViewModel.validateNewPasswords(pass1, pass2)) {
                PasswordsValidationState.OK -> signUp(pass1)
                PasswordsValidationState.INCORRECT -> incorrectPasswords()
                PasswordsValidationState.DIFFERENT -> differentPasswords()
            }
        }
    }

    private fun incorrectPasswords() {
        binding.tilPass1.error = resources.getString(R.string.incorrect_pass_message)
        Toast.makeText(activity, resources.getString(R.string.try_again), Toast.LENGTH_SHORT).show()
    }

    private fun signUp(password: String) {
        loginViewModel.signUp(password)
        Toast.makeText(activity, resources.getString(R.string.success_signUp), Toast.LENGTH_SHORT).show()
        val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment2()
        findNavController().navigate(action)
    }

    private fun differentPasswords() {
        binding.tilPass2.error = resources.getString(R.string.different_pass_message)
        Toast.makeText(activity, resources.getString(R.string.try_again), Toast.LENGTH_SHORT).show()
    }



}