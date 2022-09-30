package com.makeathon.snax

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.tensorflow.lite.task.vision.detector.Detection

class ResultsAdapter(val results: MutableList<Detection>): RecyclerView.Adapter<ResultsViewHolder>() {

    init {
        Log.d("SNAXAPP", "results = ${results.size}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        return ResultsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.results_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        val detection = results[position]
        val categories = detection.categories
        Log.d("SNAXAPP", "categories = ${categories.map { "${it.label}=${it.score}, " }}")
        val majorCategory = categories.maxWithOrNull(compareBy { it.score })
        Log.d("SNAXAPP", "category major = ${majorCategory?.label}")
        val displayName = majorCategory?.label ?: "Null"
        holder.itemName.text = displayName
    }

    override fun getItemCount(): Int {
        return results.size
    }
}

class ResultsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val itemName = itemView.findViewById<TextView>(R.id.item_name)
}
