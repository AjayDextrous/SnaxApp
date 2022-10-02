package com.makeathon.snax.viewmodels

import android.app.Application
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.makeathon.snax.CSVReader
import com.makeathon.snax.Info
import org.tensorflow.lite.task.vision.detector.Detection
import java.io.IOException

class ActivityViewModel(val app: Application) : AndroidViewModel(app) {

    enum class Target(id: Int) {
        MUSCLE(0), DIET(1), MAINTAIN(2)
    }

    var shoppingDays: Int = 2
    var nutritionalInfo: MutableMap<String, MutableMap<Info, String>>? = null

    class UserDetails(var name: String, var age: Int, var height: Int, var weight: Int, var target: Target, var activityDaily: Int)

    private var latestResults: MutableList<Detection>? = null
    var imageHeight: Int = 0
    var imageWidth:Int = 0
    var userDetails: MutableLiveData<UserDetails> = MutableLiveData()
    private var capturedImage: ImageProxy? = null

    var latestResultsLiveData : MutableLiveData<MutableList<Detection>> = MutableLiveData()

    fun loadNutritionalInfo(): MutableMap<String, MutableMap<Info, String>>? {
        val csvReader = CSVReader(app.applicationContext, "nutritional_info.csv")
        try {
            nutritionalInfo = csvReader.readCSV()
        } catch (e: IOException) {
            e.printStackTrace()
        }
//        for ((index, info) in nutritionalInfo.withIndex()){
//            Log.d("SNAXAPP", " ${index}. $info")
//        }
        return nutritionalInfo
    }

    fun setCapturedImage(imageProxy: ImageProxy){
        capturedImage = imageProxy
    }

    fun retrieveCapturedImage(): ImageProxy? {
        return capturedImage
    }

    fun retrieveLatestResults(): MutableList<Detection>? {
        return latestResults
    }

    fun notifyResultsUpdate(){
        latestResults?.let {
            latestResultsLiveData.postValue(it)
        }

    }

    fun updateResults(results: MutableList<Detection>, imageHeight: Int, imageWidth: Int){
        latestResults = results
        this.imageHeight = imageHeight
        this.imageWidth = imageWidth
    }

}
