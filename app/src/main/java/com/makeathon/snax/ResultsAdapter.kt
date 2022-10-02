package com.makeathon.snax

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.tensorflow.lite.task.vision.detector.Detection

class ResultsAdapter(
    val context: Context,
    var results: MutableList<Detection>,
    val nutritionalInfo: MutableMap<String, MutableMap<Info, String>>?
): RecyclerView.Adapter<ResultsViewHolder>() {

    //TODO: merge duplicate items, etc.
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
        val displayName = majorCategory?.label?.capitalize() ?: "Null"
        holder.itemName.text = displayName
        holder.calCount.text = context.getString(R.string.calories, nutritionalInfo?.get(displayName)
            ?.get(Info.CALORIES) ?: "???")
        holder.proteinCount.text = context.getString(R.string.grams, nutritionalInfo?.get(displayName)
            ?.get(Info.PROTEIN) ?: "???")
        holder.fatCount.text = context.getString(R.string.grams, nutritionalInfo?.get(displayName)?.get(Info.FAT) ?: "???")
        holder.carbCount.text = context.getString(R.string.grams, nutritionalInfo?.get(displayName)
            ?.get(Info.CARBS) ?: "???")
    }

    override fun getItemCount(): Int {
        return results.size
    }
}

class ResultsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val itemName: TextView = itemView.findViewById(R.id.item_name)
    val calCount: TextView = itemView.findViewById(R.id.cal_count)
    val fatCount: TextView = itemView.findViewById(R.id.fat_count)
    val carbCount: TextView = itemView.findViewById(R.id.carb_count)
    val proteinCount: TextView = itemView.findViewById(R.id.protein_count)
}
