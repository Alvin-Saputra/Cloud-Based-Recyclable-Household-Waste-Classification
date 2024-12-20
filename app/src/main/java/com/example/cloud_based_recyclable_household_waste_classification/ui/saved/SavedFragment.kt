package com.example.cloud_based_recyclable_household_waste_classification.ui.saved
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloud_based_recyclable_household_waste_classification.data.pref.UserViewModelFactory
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.ListStoryItem
import com.example.cloud_based_recyclable_household_waste_classification.databinding.SavedFragmentBinding
import com.example.cloud_based_recyclable_household_waste_classification.ui.login.LoginActivity

class SavedFragment : Fragment() {

    private var _binding: SavedFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var email: String? = null
    private var token: String? = null

    private val viewModel by viewModels<SavedViewModel> {
        UserViewModelFactory.getInstance(requireContext(), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = SavedFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.progressBarSaved.visibility = View.GONE

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin || isTokenExpired(user.exp)) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            } else {
                token = "Bearer ${user.token}"
                email = user.email
                if (token != null && email != null) {
                    viewModel.getSavedClassification(email ?: "", token ?: "")
                }
            }
        }

        viewModel.userSavedClassfication.observe(viewLifecycleOwner){ result ->
                setData(result)
        }

        viewModel.dbUserSavedClassfication.observe(viewLifecycleOwner) { result ->
            val listStoryItems = viewModel.convertToListStoryItem(result)
            setData(listStoryItems)
        }

        viewModel.message.observe(viewLifecycleOwner){message ->
            if(message == "No data found"){
                binding.textNull.text = "No Data Have Been Saved"
                binding.textNull.visibility = View.VISIBLE
            }

            if(message == "No Data Have Been Saved"){
                binding.textNull.text = "No Data Have Been Saved"
                binding.textNull.visibility = View.VISIBLE
            }

            else if(message == "Error Occur, Try Again Later"){
//                binding.textNull.text = "Error Occurred, Try Again Later"
//                binding.textNull.visibility = View.VISIBLE
                showToast("You're currently offline. Viewing offline data.")
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner){loading ->
            if(loading == true){
                binding.progressBarSaved.visibility = View.VISIBLE
            }
            else if(loading == false){
                binding.progressBarSaved.visibility = View.GONE
            }
        }

        return root
    }

    private fun setData(listSavedItem: List<ListStoryItem>) {
        val list = listSavedItem

        binding.rvSavedItem.layoutManager = LinearLayoutManager(requireContext())
        val SavedAdapter = SavedAdapter(list)
        binding.rvSavedItem.adapter = SavedAdapter

    }

    fun isTokenExpired(expirationTime: Int): Boolean {
        val currentTime = System.currentTimeMillis() / 1000

        return currentTime > expirationTime
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}