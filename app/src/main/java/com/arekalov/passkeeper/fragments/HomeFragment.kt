package com.arekalov.passkeeper.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arekalov.passkeeper.MainActivity
import com.arekalov.passkeeper.R
import com.arekalov.passkeeper.adapters.PasswordsAdapter
import com.arekalov.passkeeper.databinding.FragmentHomeBinding
import com.arekalov.passkeeper.viewmodels.LoginViewModel
import com.arekalov.passkeeper.viewmodels.PasswordsViewModel
import java.security.Provider.Service


class HomeFragment : Fragment() {
    private lateinit var passwordsViewModel: PasswordsViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var passwordsAdapter: PasswordsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passwordsViewModel = (activity as MainActivity).passwordViewModel
        loginViewModel = (activity as MainActivity).loginViewModel
        preparePasswordRecyclerView()
        observePasswordsLiveData()
        onBackPressClickOn()
        newBtnOnClick()

        passOnClick()
        copyOnCilck()
    }

    private fun passOnClick() {
        passwordsAdapter.onCLickElement = {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailPasswordFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun copyOnCilck() {
        passwordsAdapter.onCLickCopy = {
            copyToClipboard(loginViewModel.decodePass(it.password))
        }
    }

    fun copyToClipboard(text: String) {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Password", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(activity, resources.getString(R.string.copy), Toast.LENGTH_SHORT).show()
    }

    private fun onBackPressClickOn() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finishAffinity()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private fun newBtnOnClick() {
        binding.btnNew.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailPasswordFragment(null)
            findNavController().navigate(action)
        }
    }

    private fun observePasswordsLiveData() {
        passwordsViewModel.passwordsLiveData.observe(viewLifecycleOwner, Observer{
            passwords ->
            passwordsAdapter.differ.submitList(passwords)
        })
    }

    private fun preparePasswordRecyclerView() {
        passwordsAdapter = PasswordsAdapter()
        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(binding.rvPasswords.context, R.drawable.divider)!!)
        binding.rvPasswords.apply {
            adapter = passwordsAdapter
            addItemDecoration(itemDecoration)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }



}