package com.makeathon.snax.fragments

import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.makeathon.snax.R
import com.makeathon.snax.databinding.FragmentMPChartTestBinding
import com.makeathon.snax.viewmodels.ActivityViewModel
import java.util.ArrayList
import java.util.HashMap

class MPChartTestFragmentJava : Fragment() {
    //different nutrition categories
    private lateinit var categories: MutableMap<String, Int>

    //the last changes made for nutrition values
    private lateinit var lastUpdate: MutableMap<String, Int>

    private lateinit var colors: MutableList<Int>
    private lateinit var labels: MutableList<String>

    private fun creatingCategories() {
        categories["calories"] = 30
        categories["protein"] = 12
        categories["carbs"] = 15
        categories["fat"] = 9

        lastUpdate["calories"] = 26
        lastUpdate["protein"] = 13
        lastUpdate["carbs"] = 15
        lastUpdate["fat"] = 7

        labels = mutableListOf("calories","protein", "carbs", "fat")
        colors = mutableListOf()
        colors.add(ContextCompat.getColor(requireContext(), R.color.calorie_color))
        colors.add(ContextCompat.getColor(requireContext(), R.color.protein_color))
        colors.add(ContextCompat.getColor(requireContext(), R.color.carb_color))
        colors.add(ContextCompat.getColor(requireContext(), R.color.fat_color))

    }

    private lateinit var fragmentMPChartTestBinding: FragmentMPChartTestBinding
    private lateinit var viewModel: ActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMPChartTestBinding = FragmentMPChartTestBinding.inflate(inflater, container, false)
        return fragmentMPChartTestBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ActivityViewModel::class.java]

        categories = HashMap()
        lastUpdate = HashMap()
        creatingCategories()
        createChart()

        fragmentMPChartTestBinding.root.setOnClickListener {
            if(fragmentMPChartTestBinding.barChart.visibility == View.VISIBLE){
                fragmentMPChartTestBinding.barChart.visibility = View.GONE
                fragmentMPChartTestBinding.macrosLayout.visibility = View.VISIBLE
            } else {
                fragmentMPChartTestBinding.barChart.visibility = View.VISIBLE
                fragmentMPChartTestBinding.macrosLayout.visibility = View.GONE
            }
        }
    }

    //updating old nutrition values by adding new values
    fun addData(category: String, value: Int) {
        categories[category] = categories[category]!! + value
        lastUpdate[category] = value
        createChart()
    }

    //deletes the information about the last scanned product
    fun deleteLastChange(category: String) {
        if (lastUpdate[category] == 0) {
            println("There is no information about last updates")
        } else {
            for (entry in lastUpdate.keys) {
                categories[entry] = categories[category]!! - lastUpdate[category]!!
                lastUpdate[entry] = 0
            }
        }
    }

    private var entries: MutableList<BarEntry> = ArrayList()
    private var dataSet: BarDataSet? = null
    private var data: BarData? = null

    private fun createChart() {
        for ((i, entry) in categories.keys.withIndex()) {
            // turn your data into Entry objects
            Log.d("SNAXAPP", "createChart: ${categories[entry]} -> $i")
            entries.add(BarEntry(i.toFloat(), categories[entry]!!.toFloat() ))
        }
        dataSet = BarDataSet(entries, "Label")
        dataSet?.colors = colors
        dataSet?.setDrawValues(false)
        data = BarData(dataSet)
        data!!.barWidth = 0.9f

        fragmentMPChartTestBinding.barChart.apply {
            axisLeft.setDrawGridLines(false)
            axisRight.setDrawGridLines(false)
            axisLeft.setDrawAxisLine(false)
            axisRight.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            xAxis.setDrawAxisLine(false)

            axisLeft.setDrawLabels(false)
            axisRight.setDrawLabels(false)
//            xAxis.setDrawLabels(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = IndexAxisValueFormatter(labels.map { it.capitalize() })
            xAxis.granularity = 1f
            xAxis.isGranularityEnabled = true
        }

        val description = Description()
        description.text = ""
        fragmentMPChartTestBinding.barChart.setTouchEnabled(false)
        fragmentMPChartTestBinding.barChart.isDragEnabled = false
        fragmentMPChartTestBinding.barChart.legend.isEnabled = false
        fragmentMPChartTestBinding.barChart.description = description
        fragmentMPChartTestBinding.barChart.setDrawGridBackground(false)
        fragmentMPChartTestBinding.barChart.setDrawBorders(false)
        fragmentMPChartTestBinding.barChart.data = data
        fragmentMPChartTestBinding.barChart.setFitBars(true) // make the x-axis fit exactly all bars
        fragmentMPChartTestBinding.barChart.invalidate() // refresh
    }


}