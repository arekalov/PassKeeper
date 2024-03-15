package com.example.passkeeper.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.passkeeper.MainActivity
import com.example.passkeeper.R
import com.example.passkeeper.databinding.FragmentLoginBinding
import com.example.passkeeper.viewmodels.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {
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
                  val action = LoginFragmentDirections.actionLoginFragment2ToHomeFragment()
                  Toast.makeText(activity, resources.getString(R.string.welcome), Toast.LENGTH_SHORT).show()
                  findNavController().navigate(action)
              }
              else {
                  Toast.makeText(activity, resources.getString(R.string.incorrect_password), Toast.LENGTH_SHORT).show()
              }
          }
      }
    }


}