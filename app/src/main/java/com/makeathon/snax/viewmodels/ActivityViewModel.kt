package com.makeathon.snax.viewmodels

import android.app.Application
import androidx.camera.core.ImageProxy
import androidx.lifecycle.AndroidViewModel
import org.tensorflow.lite.task.vision.detector.Detection

class ActivityViewModel(app: Application) : AndroidViewModel(app) {

    class UserDetails(var name: String, var age: Int, var height: Int, var weight: Int)

    private var latestResults: MutableList<Detection>? = null
    var imageHeight: Int = 0
    var imageWidth:Int = 0
    var userDetails: UserDetails? = null
    private var capturedImage: ImageProxy? = null

    fun setCapturedImage(imageProxy: ImageProxy){
        capturedImage = imageProxy
    }

    fun retrieveCapturedImage(): ImageProxy? {
        return capturedImage
    }

    fun retrieveLatestResults(): MutableList<Detection>? {
        return latestResults
    }

    fun updateResults(results: MutableList<Detection>, imageHeight: Int, imageWidth: Int){
        latestResults = results
        this.imageHeight = imageHeight
        this.imageWidth = imageWidth
    }

}
