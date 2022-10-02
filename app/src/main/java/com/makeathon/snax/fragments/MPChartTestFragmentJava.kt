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
import com.makeathon.snax.Info
import com.makeathon.snax.R
import com.makeathon.snax.databinding.FragmentMPChartTestBinding
import com.makeathon.snax.viewmodels.ActivityViewModel
import java.util.ArrayList
import java.util.HashMap
import kotlin.math.roundToInt

class MPChartTestFragmentJava : Fragment() {
    private var maxFat: Float = 15f
    private var maxCarbs: Float = 26f
    private var maxProtein: Float = 64f
    private var maxCal: Float = 1800f

    private var currFat: Float = 5f
    private var currCarbs: Float = 27f
    private var currProtein: Float = 54f
    private var currCal: Float = 1900f

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

        viewModel.userDetails.observe(viewLifecycleOwner) {
            createRDI()
            fragmentMPChartTestBinding.calCount.text = getString(R.string.calories, maxCal.toInt().toString())
            fragmentMPChartTestBinding.proteinCount.text = getString(R.string.grams, maxProtein.toInt().toString())
            fragmentMPChartTestBinding.carbCount.text = getString(R.string.grams, maxCarbs.toInt().toString())
            fragmentMPChartTestBinding.fatCount.text = getString(R.string.grams, maxFat.toInt().toString())
        }

        creatingCategories()
        createChart()

        fragmentMPChartTestBinding.root.setOnClickListener {
            if(fragmentMPChartTestBinding.barChartContainer.visibility == View.VISIBLE){
                fragmentMPChartTestBinding.barChartContainer.visibility = View.GONE
                fragmentMPChartTestBinding.macrosLayout.visibility = View.VISIBLE
            } else {
                fragmentMPChartTestBinding.barChartContainer.visibility = View.VISIBLE
                fragmentMPChartTestBinding.macrosLayout.visibility = View.GONE
            }
        }

        viewModel.latestResultsLiveData.observe(viewLifecycleOwner) { detections ->
            val items = mutableListOf<String>()
            detections.forEach { detection ->
                val item = detection.categories.maxWithOrNull(compareBy { it.score })?.label?.capitalize()
                item?.let {
                    items.add(it)
                }
            }
            currCal = 0f; currCarbs = 0f; currFat = 0f; currProtein = 0f;
            for (item in items) {
                currCal += viewModel.nutritionalInfo?.get(item)?.get(Info.CALORIES)?.toFloat() ?: 0f
                currProtein += viewModel.nutritionalInfo?.get(item)?.get(Info.PROTEIN)?.toFloat() ?: 0f
                currFat += viewModel.nutritionalInfo?.get(item)?.get(Info.FAT)?.toFloat() ?: 0f
                currCarbs += viewModel.nutritionalInfo?.get(item)?.get(Info.CARBS)?.toFloat() ?: 0f
            }
            val days = viewModel.shoppingDays
            currCal /= days; currProtein /= days;currFat /= days;currCarbs /= days

            categories["calories"] = (currCal * 100 / maxCal).roundToInt()
            categories["protein"] = (currFat * 100 / maxFat).roundToInt()
            categories["carbs"] = (currCarbs * 100 / maxCarbs).roundToInt()
            categories["fat"] = (currFat * 100 / maxFat).roundToInt()
            createChart()
        }
    }

    private fun createRDI() {
        val bmr = 88.362 + (13.397 * viewModel.userDetails.value?.weight!!) + (4.799 * viewModel.userDetails.value?.height!!) - (5.677 * viewModel.userDetails.value?.age!!)

        maxCal = when(viewModel.userDetails.value?.target){
            ActivityViewModel.Target.MUSCLE -> (bmr * 1.1).toFloat()
            ActivityViewModel.Target.DIET -> (bmr * 0.7).toFloat()
            ActivityViewModel.Target.MAINTAIN -> bmr.toFloat()
            null -> bmr.toFloat()
        }
        maxCal += (viewModel.userDetails.value?.activityDaily!! * 100f)

        maxProtein = (0.8f * viewModel.userDetails.value?.weight!!)

        maxProtein *= when(viewModel.userDetails.value?.target){
            ActivityViewModel.Target.MUSCLE -> 1.1f
            ActivityViewModel.Target.DIET -> 1.05f
            ActivityViewModel.Target.MAINTAIN -> 1f
            null -> 1f
        }

        val remainingCalories = maxCal - (4 * maxProtein)
        maxCarbs = (0.64f * remainingCalories) / 4
        maxFat = (0.36f * remainingCalories) / 9


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
        for ((i, entry) in labels.withIndex()) {
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

            axisLeft.axisMaximum = 150f
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