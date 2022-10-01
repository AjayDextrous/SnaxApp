package com.makeathon.snax.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.makeathon.snax.R
import com.makeathon.snax.databinding.FragmentSettingsBinding
import com.makeathon.snax.viewmodels.ActivityViewModel

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: ActivityViewModel
    private lateinit var fragmentSettingsBinding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return fragmentSettingsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ActivityViewModel::class.java]
        fragmentSettingsBinding.editDetails.setOnClickListener {
            navigateToForm()
        }

        viewModel.userDetails.value?.run {
            fragmentSettingsBinding.userName.text = name
            fragmentSettingsBinding.userAge.text = getString(R.string.user_age, age)
            fragmentSettingsBinding.userWeight.text = getString(R.string.user_weight, weight)
            fragmentSettingsBinding.userHeight.text = getString(R.string.user_height, height)
            fragmentSettingsBinding.userTarget.text = getString(R.string.user_target, target.name)
            fragmentSettingsBinding.userActivity.text = getString(R.string.user_activity, activityDaily)
        }

    }

    private fun navigateToForm() {
        lifecycleScope.launchWhenStarted {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
                SettingsFragmentDirections.actionSettingsToEnterdetails())
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.userDetails.value?.run {
            fragmentSettingsBinding.userName.text = name
            fragmentSettingsBinding.userAge.text = getString(R.string.user_age, age)
            fragmentSettingsBinding.userWeight.text = getString(R.string.user_weight, weight)
            fragmentSettingsBinding.userHeight.text = getString(R.string.user_height, height)
            fragmentSettingsBinding.userTarget.text = getString(R.string.user_target, target.name)
            fragmentSettingsBinding.userActivity.text = getString(R.string.user_activity, activityDaily)
        }
    }

}