package com.makeathon.snax.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makeathon.snax.R

class MPChartTestFragment : Fragment() {

    companion object {
        fun newInstance() = MPChartTestFragment()
    }

    private lateinit var viewModel: MPChartTestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_m_p_chart_test, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MPChartTestViewModel::class.java)
        // TODO: Use the ViewModel
    }

}