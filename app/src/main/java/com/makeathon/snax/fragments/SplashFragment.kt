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
import com.makeathon.snax.databinding.FragmentCameraBinding
import com.makeathon.snax.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var viewModel: SplashViewModel

    private var _fragmentSplashBinding: FragmentSplashBinding? = null

    private val fragmentSplashBinding
        get() = _fragmentSplashBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentSplashBinding = FragmentSplashBinding.inflate(inflater, container, false)

        return fragmentSplashBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentSplashBinding.enterDetailsButton.setOnClickListener {
            navigateToForm()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun navigateToForm() {
        lifecycleScope.launchWhenStarted {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
                SplashFragmentDirections.actionSplashToEnterdetails())
        }
    }

}