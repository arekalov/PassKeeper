package com.arekalov.passkeeper.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arekalov.data.db.Password
import com.arekalov.data.db.PasswordDataBase
import com.arekalov.passkeeper.PasswordApplication
import com.arekalov.passkeeper.R
import com.arekalov.passkeeper.adapters.PasswordsAdapter
import com.arekalov.passkeeper.databinding.FragmentHomeBinding
import com.arekalov.passkeeper.viewmodels.PasswordsViewModel
import com.arekalov.passkeeper.viewmodels.PasswordsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: PasswordsViewModel by viewModels {
        PasswordsViewModelFactory((requireActivity().application as PasswordApplication).repository)
    }
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
        preparePasswordRecyclerView()
        observePasswordsLiveData()
        onBackPressClickOn()
        btnListener()
    }

    private fun onBackPressClickOn() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finishAffinity()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private fun btnListener() {
        binding.btnNew.setOnClickListener {
            viewModel.insertPassword(Password("artem", "https://www.google.com", "artme", "artem"))
        }
    }

    private fun observePasswordsLiveData() {
        viewModel.passwordsLiveData.observe(viewLifecycleOwner, Observer{
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