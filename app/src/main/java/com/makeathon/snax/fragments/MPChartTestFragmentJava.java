package com.makeathon.snax.fragments;


import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.makeathon.snax.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MPChartTestFragmentJava extends AppCompatActivity {

    //different nutrition categories
    private Map<String, Integer> categories;
    //the last changes made for nutrition values
    private Map<String, Integer> lastUpdate;

    public MPChartTestFragmentJava() {
        categories = new HashMap<>();
        lastUpdate = new HashMap<>();
        creatingCategories();
        createChart();
    }

    private void creatingCategories(){
        categories.put("energy", 0);
        categories.put("fat", 0);
        categories.put("carbohydrates", 0);
        categories.put("sugar", 0);
        categories.put("protein", 0);
        categories.put("salt", 0);

        lastUpdate.put("energy", 0);
        lastUpdate.put("fat", 0);
        lastUpdate.put("carbohydrates", 0);
        lastUpdate.put("sugar", 0);
        lastUpdate.put("protein", 0);
        lastUpdate.put("salt", 0);
    }

    //updating old nutrition values by adding new values
    public void addData(String category, int value){
        categories.put(category, categories.get(category)+value);
        lastUpdate.put(category, value);

        createChart();
    }
    //deletes the information about the last scanned product
    public void deleteLastChange(String category) {
        if (lastUpdate.get(category) == 0) {
            System.out.println("There is no information about last updates");
        } else {
            for (String entry : lastUpdate.keySet()) {
                categories.put(entry, categories.get(category) - lastUpdate.get(category));
                lastUpdate.put(entry, 0);
            }
        }
    }

    List<BarEntry> entries = new ArrayList<>();
    BarDataSet dataSet;
    BarData data;
    BarChart barChart = findViewById(R.id.barChart);
    private void createChart() {
        int i = 0;
        for (String entry : categories.keySet()) {
            // turn your data into Entry objects
            entries.add(new BarEntry(categories.get(entry), i++));
        }

        dataSet = new BarDataSet (entries, "Label");

        data = new BarData(dataSet);
        data.setBarWidth(0.9f);

        barChart.setData(data);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh
    }


}
