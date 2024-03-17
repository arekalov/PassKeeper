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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arekalov.data.db.Password
import com.arekalov.passkeeper.MainActivity
import com.arekalov.passkeeper.PasswordApplication
import com.arekalov.passkeeper.R
import com.arekalov.passkeeper.adapters.PasswordsAdapter
import com.arekalov.passkeeper.databinding.FragmentHomeBinding
import com.arekalov.passkeeper.viewmodels.PasswordsViewModel
import com.arekalov.passkeeper.viewmodels.PasswordsViewModelFactory


class HomeFragment : Fragment() {
    private lateinit var viewModel: PasswordsViewModel
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
        viewModel = (activity as MainActivity).passwordViewModel
        preparePasswordRecyclerView()
        observePasswordsLiveData()
        onBackPressClickOn()
        newBtnOnClick()
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
            val action = HomeFragmentDirections.actionHomeFragmentToDetailPasswordFragment()
            findNavController().navigate(action)
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