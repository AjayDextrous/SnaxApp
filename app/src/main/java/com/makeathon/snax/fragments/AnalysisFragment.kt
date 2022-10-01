package com.makeathon.snax.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.ImageProxy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeathon.snax.R
import com.makeathon.snax.ResultsAdapter
import com.makeathon.snax.databinding.FragmentAnalysisBinding
import com.makeathon.snax.viewmodels.ActivityViewModel
import java.nio.ByteBuffer


class AnalysisFragment : Fragment() {

    companion object {
        fun newInstance() = AnalysisFragment()
    }

    private var _fragmentAnalysisBinding: FragmentAnalysisBinding? = null
    private val fragmentAnalysisBinding
        get() = _fragmentAnalysisBinding!!
    private lateinit var viewModel: ActivityViewModel

    private var image: ImageProxy? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentAnalysisBinding = FragmentAnalysisBinding.inflate(inflater, container, false)
        return fragmentAnalysisBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ActivityViewModel::class.java)

        fragmentAnalysisBinding.imageTitle.text = getString(R.string.users_grocery, viewModel.userDetails.value?.name?.split(" ")?.get(0))

        image = viewModel.retrieveCapturedImage()
        fragmentAnalysisBinding.capturedImage.setImageBitmap(image?.let { imageProxyToBitmap(it) })
        Handler().post {
            fragmentAnalysisBinding.overlay.setResults(viewModel.retrieveLatestResults()!!, viewModel.imageHeight, viewModel.imageWidth)
            fragmentAnalysisBinding.overlay.invalidate()
        }
        setAdapterToResults()

    }

    private fun setAdapterToResults() {
        val adapter: ResultsAdapter = ResultsAdapter(viewModel.retrieveLatestResults()!!)
        fragmentAnalysisBinding.itemsList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        fragmentAnalysisBinding.itemsList.adapter = adapter
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap? {
        Log.d("SNAXAPP", "rot: ${image.imageInfo.rotationDegrees}")
        val rotationDegrees = image.imageInfo.rotationDegrees
        val planeProxy = image.planes[0]
        val buffer: ByteBuffer = planeProxy.buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        val rawBmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        val matrix: Matrix = Matrix()
        matrix.postRotate((rotationDegrees).toFloat())
        val rotatedBitmap = Bitmap.createBitmap(
            rawBmp,
            0,
            0,
            rawBmp.width,
            rawBmp.height,
            matrix,
            true
        )
        return rotatedBitmap
    }

}