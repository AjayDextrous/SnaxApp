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
import com.makeathon.snax.databinding.FragmentDetailsFormBinding

class DetailsFormFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsFormFragment()
    }

    private lateinit var viewModel: DetailsFormViewModel
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

        fragmentDetailsFormBinding.continueButton.setOnClickListener {
            navigateToCamera()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailsFormViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun navigateToCamera() {
        lifecycleScope.launchWhenStarted {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
                DetailsFormFragmentDirections.actionDetailsToCamera())
        }
    }

}