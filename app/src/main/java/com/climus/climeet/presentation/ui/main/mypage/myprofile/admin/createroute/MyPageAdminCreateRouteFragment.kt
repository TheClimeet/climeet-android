package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.createroute

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageAdminCreateRouteBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.generateFileName
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.adapter.CreateRouteChipAdapter
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.adapter.CreateRouteHoldAdapter
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.adapter.CreateRouteLevelAdapter
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.adapter.CreateRouteSectorAdapter
import com.climus.climeet.presentation.util.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

@AndroidEntryPoint
class MyPageAdminCreateRouteFragment :
    BaseFragment<FragmentMypageAdminCreateRouteBinding>(R.layout.fragment_mypage_admin_create_route) {

    private val viewModel: MyPageAdminCreateRouteViewModel by viewModels()

    private lateinit var neededPermissionList: ArrayList<String>
    private val requiredPermissionList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }

    internal enum class TouchMode {
        NONE,
        ONE_FINGER,
        TWO_FINGER
    }

    private var touchMode: TouchMode? = null
    private var matrix: Matrix? = null
    private var savedMatrix: Matrix? = null
    private var startPoint: PointF? = null
    private var midPoint: PointF? = null
    private var oldDistance = 0f
    private var cropBitMap : Bitmap?=null

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.rvSector.adapter = CreateRouteSectorAdapter()
        binding.rvSector.itemAnimator = null
        binding.rvHold.adapter = CreateRouteHoldAdapter()
        binding.rvLevel.adapter = CreateRouteLevelAdapter()
        binding.rvRouteChip.adapter = CreateRouteChipAdapter()
        viewModel.setData()
        initEventObserve()
        initStateObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is CreateRouteEvent.CreateRoute -> {
                        getCroppedBitmapFromImageView()?.let { data ->
                            cropBitMap = data
                            onCheckPermissions()
                        }
                    }

                    is CreateRouteEvent.ChangeSector -> {
                        initImageCenterCrop(it.img)
                    }
                }
            }
        }
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.uiState.collect {
                if (it.selectedHoldImage != 0) {
                    binding.ivStone.setImageResource(it.selectedHoldImage)
                }
            }
        }
    }

    private fun onCheckPermissions(){
        neededPermissionList = arrayListOf()

        requiredPermissionList.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) neededPermissionList.add(permission)
        }

        neededPermissionList.forEach{
            Log.d(TAG,it)
        }

        if (neededPermissionList.isNotEmpty()) {
            activityResultLauncher.launch(neededPermissionList.toTypedArray())
        } else {
            bitMapChangeProcess()
        }
    }

    private val contract = ActivityResultContracts.RequestMultiplePermissions()

    private val activityResultLauncher = registerForActivityResult(contract){ resultMap ->
        val isAllGranted = requiredPermissionList.all{ e-> resultMap[e] == true}
        Log.d(TAG,"requiredPermissionList")
        requiredPermissionList.forEach {
            Log.d(TAG,it)
        }
        if(isAllGranted){
            bitMapChangeProcess()
        }
    }

    private fun bitMapChangeProcess(){
        cropBitMap?.let{ bitmap ->
            bitmapToFile(bitmap)?.let { file ->
                val requestFile =
                    file.asRequestBody("image/jpg".toMediaTypeOrNull())
                viewModel.chipImageToUrl(
                    MultipartBody.Part.createFormData(
                        "file",
                        file.name,
                        requestFile
                    )
                )
            }
        }
    }

    private fun bitmapToFile(bitmap: Bitmap): File? {
        val directory = requireContext().filesDir  // 내부 저장소의 파일 디렉토리
        val file = File(directory, generateFileName())

        var outputStream: OutputStream? = null
        try {
            outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            return file
        } catch (e: IOException) {
            Log.d(TAG, e.message.toString())
            e.printStackTrace()
        } finally {
            outputStream?.close()
        }
        return null
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initImageCenterCrop(imgUrl: String) {
        matrix = Matrix()
        savedMatrix = Matrix()

        binding.ivRoute.setOnTouchListener(onTouch)
        binding.ivRoute.scaleType = ImageView.ScaleType.MATRIX

        Glide.with(requireContext())
            .load(imgUrl)
            .centerCrop()
            .into(binding.ivRoute)
    }

    @SuppressLint("ClickableViewAccessibility")
    private val onTouch = View.OnTouchListener { v, event ->
        if (v == binding.ivRoute) {
            val action = event.action
            when (action and MotionEvent.ACTION_MASK) {

                MotionEvent.ACTION_DOWN -> {
                    touchMode = TouchMode.ONE_FINGER
                    downSingleEvent(event)
                }

                MotionEvent.ACTION_POINTER_DOWN -> if (event.pointerCount == 2) {
                    touchMode = TouchMode.TWO_FINGER
                    downMultiEvent(event)
                }

                MotionEvent.ACTION_MOVE -> if (touchMode === TouchMode.ONE_FINGER) {
                    moveSingleEvent(event)
                } else if (touchMode === TouchMode.TWO_FINGER) {
                    moveMultiEvent(event)
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> touchMode = TouchMode.NONE
            }
        }
        true
    }

    private fun downSingleEvent(event: MotionEvent) {
        savedMatrix?.set(matrix)
        startPoint = PointF(event.x, event.y)
    }

    private fun downMultiEvent(event: MotionEvent) {
        oldDistance = getDistance(event)
        if (oldDistance > 5f) {
            savedMatrix?.set(matrix)
            midPoint = getMidPoint(event)
        }
    }

    private fun moveSingleEvent(event: MotionEvent) {
        matrix?.set(savedMatrix)
        matrix?.postTranslate(event.x - startPoint!!.x, event.y - startPoint!!.y)
        binding.ivRoute.imageMatrix = matrix
    }

    private fun moveMultiEvent(event: MotionEvent) {
        val newDistance = getDistance(event)
        if (newDistance > 5f) {
            matrix?.set(savedMatrix)
            val scale = newDistance / oldDistance
            matrix?.postScale(scale, scale, midPoint!!.x, midPoint!!.y)
            matrix?.postRotate(0.0.toFloat(), midPoint!!.x, midPoint!!.y)
            binding.ivRoute.imageMatrix = matrix
        }
    }

    private fun getMidPoint(e: MotionEvent): PointF {
        val x = (e.getX(0) + e.getX(1)) / 2
        val y = (e.getY(0) + e.getY(1)) / 2
        return PointF(x, y)
    }

    private fun getDistance(e: MotionEvent): Float {
        val x = e.getX(0) - e.getX(1)
        val y = e.getY(0) - e.getY(1)
        return sqrt((x * x + y * y).toDouble()).toFloat()
    }


    private fun getCroppedBitmapFromImageView(): Bitmap? {

        val imageView = binding.ivRoute
        val drawable = binding.ivRoute.drawable ?: return null
        val bitmap = (drawable as BitmapDrawable).bitmap

        val matrix = Matrix(binding.ivRoute.imageMatrix)
        val drawableRect = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
        matrix.mapRect(drawableRect)

        val imageViewWidth = imageView.width.toFloat()
        val imageViewHeight = imageView.height.toFloat()

        val values = FloatArray(9)
        matrix.getValues(values)
        val scaleX = values[Matrix.MSCALE_X]
        val scaleY = values[Matrix.MSCALE_Y]

        val imageViewLeft = max(-drawableRect.left / scaleX, 0f)
        val imageViewTop = max(-drawableRect.top / scaleY, 0f)
        val imageViewRight = min(imageViewLeft + imageViewWidth / scaleX, bitmap.width.toFloat())
        val imageViewBottom = min(imageViewTop + imageViewHeight / scaleY, bitmap.height.toFloat())

        if (imageViewLeft >= imageViewRight || imageViewTop >= imageViewBottom) {
            return null
        }

        return Bitmap.createBitmap(
            bitmap,
            imageViewLeft.toInt(),
            imageViewTop.toInt(),
            (imageViewRight - imageViewLeft).toInt(),
            (imageViewBottom - imageViewTop).toInt()
        )
    }

}