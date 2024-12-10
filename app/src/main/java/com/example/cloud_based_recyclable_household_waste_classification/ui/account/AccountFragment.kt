package com.example.cloud_based_recyclable_household_waste_classification.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cloud_based_recyclable_household_waste_classification.data.pref.UserViewModelFactory
import com.example.cloud_based_recyclable_household_waste_classification.databinding.FragmentAccountBinding
import com.example.cloud_based_recyclable_household_waste_classification.ui.login.LoginActivity

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AccountViewModel> {
        UserViewModelFactory.getInstance(requireContext(), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        observeViewModel()

        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
        }

        return root
    }

    private fun observeViewModel() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin || isTokenExpired(user.exp)) {
                viewModel.DeleteAllFromDatabase()
                navigateToLogin()
            } else {
                // Update UI with user data
                binding.tvUserName.text = "Hello, ${user.username}!"
                binding.tvEmail.text = "${user.email}"
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }

    fun isTokenExpired(expirationTime: Int): Boolean {
        // Waktu saat ini dalam detik (timestamp UNIX)
        val currentTime = System.currentTimeMillis() / 1000

        // Periksa apakah waktu saat ini lebih besar dari waktu kedaluwarsa (exp)
        return currentTime > expirationTime
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
