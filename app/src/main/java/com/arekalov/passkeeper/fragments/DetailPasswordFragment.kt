package com.arekalov.passkeeper.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.arekalov.data.db.Password
import com.arekalov.passkeeper.MainActivity
import com.arekalov.passkeeper.R
import com.arekalov.passkeeper.databinding.FragmentDetailPasswordBinding
import com.arekalov.passkeeper.viewmodels.LoginViewModel
import com.arekalov.passkeeper.viewmodels.PasswordsViewModel

class DetailPasswordFragment : Fragment() {
    private lateinit var binding: FragmentDetailPasswordBinding
    private lateinit var passwordsViewModel: PasswordsViewModel
    private lateinit var loginViewModel: LoginViewModel
    private var passNow: Password? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailPasswordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passwordsViewModel = (activity as MainActivity).passwordViewModel
        loginViewModel = (activity as MainActivity).loginViewModel

        saveBtnOncClick()
        setPassword()
        copyBtnOnClick()
        deleteBtnOnClick()
    }

    private fun deleteBtnOnClick() {
        binding.btnDelete.setOnClickListener {
            findNavController().popBackStack()
            if (passNow != null) {
                passwordsViewModel.deletePassword(passNow!!)
            }
        }
    }

    private fun copyBtnOnClick() {
        binding.btnCopy.setOnClickListener {
            copyToClipboard(binding.etPassword.text.toString())
        }
    }

    private fun setPassword() {
        val password = arguments?.getParcelable<Password>("password")
        passNow = password
        if (password != null) {
            binding.etName.setText(password.name)
            binding.etLogin.setText(password.login)
            binding.etPassword.setText(loginViewModel.decodePass(password.password))
            binding.etUrl.setText(password.url)
        } else {
            Log.e("Error", "setProduct null product")
        }
    }

    private fun saveBtnOncClick() {
        binding.btnSave.setOnClickListener {
            if (checkName(binding.etName.text.toString())) {
                passwordsViewModel.insertPassword(
                    Password(
                        name = binding.etName.text.toString(),
                        url = binding.etUrl.text.toString(),
                        login = binding.etLogin.text.toString(),
                        password = loginViewModel.encodePass(binding.etPassword.text.toString())
                    )
                )
                Toast.makeText(activity, resources.getString(R.string.success), Toast.LENGTH_SHORT)
                    .show()
                findNavController().popBackStack()
            }
        }
    }

    fun copyToClipboard(text: String) {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Password", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(activity, resources.getString(R.string.copy), Toast.LENGTH_SHORT).show()
    }

    private fun checkName(name: String): Boolean {
        if (name.isEmpty()) {
            binding.etName.error = "Название не должно быть пустым"
            return false
        }
        return true
    }
}