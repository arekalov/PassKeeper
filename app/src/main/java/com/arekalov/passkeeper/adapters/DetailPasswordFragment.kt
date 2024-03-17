package com.arekalov.passkeeper.adapters

import android.os.Bundle
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
import com.arekalov.passkeeper.viewmodels.PasswordsViewModel

class DetailPasswordFragment : Fragment() {
    private lateinit var binding: FragmentDetailPasswordBinding
    private lateinit var passwordsViewModel: PasswordsViewModel

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

        saveBtnOncClick()
    }

    private fun saveBtnOncClick() {
        binding.btnSave.setOnClickListener {
            if (checkName(binding.etName.text.toString())) {
                passwordsViewModel.insertPassword(
                    Password(
                        name = binding.etName.text.toString(),
                        url = binding.etUrl.text.toString(),
                        login = binding.etLogin.text.toString(),
                        password = binding.etLogin.text.toString()
                    )
                )
                Toast.makeText(activity, resources.getString(R.string.success), Toast.LENGTH_SHORT)
                    .show()
                findNavController().popBackStack()
            }
        }
    }

    private fun checkName(name: String): Boolean {
        if (name.isEmpty()) {
            binding.etName.error = "Название не должно быть пустым"
            return false
        }
        return true
    }
}