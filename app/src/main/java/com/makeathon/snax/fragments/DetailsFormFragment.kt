package com.makeathon.snax.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.makeathon.snax.R
import com.makeathon.snax.databinding.FragmentDetailsFormBinding
import com.makeathon.snax.viewmodels.ActivityViewModel

class DetailsFormFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsFormFragment()
    }

    private lateinit var viewModel: ActivityViewModel
    private var _fragmentDetailsFormBinding: FragmentDetailsFormBinding? = null

    private val fragmentDetailsFormBinding
    get() = _fragmentDetailsFormBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentDetailsFormBinding = FragmentDetailsFormBinding.inflate(inflater, container, false)
        return fragmentDetailsFormBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ActivityViewModel::class.java]
        val isFromOtherScreen: Boolean = arguments?.getBoolean("isFromOtherScreen") == true

        viewModel.userDetails?.run {
            fragmentDetailsFormBinding.nameEdittext.setText(name)
            fragmentDetailsFormBinding.ageEdittext.setText(age.toString())
            fragmentDetailsFormBinding.weightEdittext.setText(weight.toString())
            fragmentDetailsFormBinding.heightEdittext.setText(height.toString())
        }

        fragmentDetailsFormBinding.continueButton.setOnClickListener {
            saveDetails()

            if(isFromOtherScreen){
                activity?.onBackPressed()
            } else {
                navigateToCamera()
            }
        }
    }

    private fun saveDetails() {
        val name = fragmentDetailsFormBinding.nameEdittext.text.toString().run {
            ifBlank {
                "User"
            }
        }
        val age = fragmentDetailsFormBinding.ageEdittext.text.toString().toIntOrNull()?:18
        val height = fragmentDetailsFormBinding.heightEdittext.text.toString().toIntOrNull()?:175
        val weight = fragmentDetailsFormBinding.weightEdittext.text.toString().toIntOrNull()?:75

        viewModel.userDetails = ActivityViewModel.UserDetails(name, age, height, weight)

        val editor = requireContext().getSharedPreferences(getString(R.string.snax_prefs), Context.MODE_PRIVATE).edit()
        editor.putString("USER_NAME", name)
        editor.putInt("USER_AGE", age)
        editor.putInt("USER_HEIGHT", height)
        editor.putInt("USER_WEIGHT", weight)
        editor.putBoolean(getString(R.string.is_user_details_given), true)
        editor.apply()
    }

    private fun navigateToCamera() {
        lifecycleScope.launchWhenStarted {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
                DetailsFormFragmentDirections.actionDetailsToCamera())
        }
    }

}