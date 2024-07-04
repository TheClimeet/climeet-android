package com.climus.climeet.presentation.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.ActivityMainBinding
import com.climus.climeet.presentation.base.BaseActivity
import com.climus.climeet.presentation.customview.SelectImageMethodDialog
import com.climus.climeet.presentation.ui.main.mypage.CameraImageForm
import com.climus.climeet.presentation.ui.saveCameraImage
import com.climus.climeet.presentation.ui.toMultiPart
import com.climus.climeet.presentation.ui.toMultiPartImage
import com.climus.climeet.presentation.ui.toVideoThumbnail
import com.climus.climeet.presentation.util.Constants.CAMERA_PERMISSION
import com.climus.climeet.presentation.util.Constants.STORAGE_PERMISSION_IMAGE
import com.climus.climeet.presentation.util.Constants.STORAGE_PERMISSION_VIDEO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var neededPermissionList: MutableList<String>
    private lateinit var navController: NavController

    private val storagePermissionList =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                // 안드로이드 13 이상 필요한 권한들
                Manifest.permission.READ_MEDIA_IMAGES,
                // Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            arrayOf(  // 안드로이드 13 미만 필요한 권한들
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

    private val cameraPermissionList = arrayOf(Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBnv()
        initEventObserve()
        viewModel.patchFcmToken()
    }

    private fun setBnv() {

        binding.mainBnv.itemIconTintList = null

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.mainBnv.apply {
            setupWithNavController(navController)
            setOnItemSelectedListener { item ->
                NavigationUI.onNavDestinationSelected(item, navController)
                navController.popBackStack(item.itemId, inclusive = false)
                true
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.home_fragment || destination.id == R.id.shorts_fragment
                || destination.id == R.id.myPage_fragment || destination.id == R.id.bestClimerFragment || destination.id == R.id.popularShortsFragment
                || destination.id == R.id.popularCragsFragment || destination.id == R.id.popularRoutesFragment
                || destination.id == R.id.set_timer_climbing_record_fragment || destination.id == R.id.calendar_fragment
                || destination.id == R.id.timerMainFragment || destination.id == R.id.timerMainFragment || destination.id == R.id.record_fragment
            ) {
                // todo bnv show 해야되는 frag
                binding.mainBnv.visibility = View.VISIBLE
            } else {
                binding.mainBnv.visibility = View.INVISIBLE
            }
        }

        handleIntent()
    }

    // 알림창을 눌렀을 때 스톱워치 화면이 보여지게 설정하는 함수
    private fun handleIntent() {
        if (intent.hasExtra("showTimerFragment")) {
            navController.navigate(R.id.record_fragment)
        }
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is MainEvent.GoToGalleryForVideo -> onCheckVideoPermissions()
                    is MainEvent.GoToSetProfileImage -> showMethodSelectionDialog(it.context)
                    is MainEvent.ShowToastMessage -> showToastMessage(it.msg)
                    is MainEvent.ChangeStatusBarBlack -> {
                        window.statusBarColor =
                            ContextCompat.getColor(this@MainActivity, R.color.black)
                    }

                    is MainEvent.ChangeStatusBarBackground -> {
                        window.statusBarColor =
                            ContextCompat.getColor(this@MainActivity, R.color.cm_background)
                    }
                }
            }
        }
    }

    // 비디오만 보여주는 권한 확인
    private fun onCheckVideoPermissions() {
        neededPermissionList = mutableListOf()

        storagePermissionList.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) neededPermissionList.add(permission)
        }

        if (neededPermissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                neededPermissionList.toTypedArray(),
                STORAGE_PERMISSION_VIDEO
            )
        } else {
            openGalleryForVideo()
        }
    }

    // 사진 권한 확인
    private fun onCheckImagePermissions() {
        neededPermissionList = mutableListOf()

        storagePermissionList.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) neededPermissionList.add(permission)
        }

        if (neededPermissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                neededPermissionList.toTypedArray(),
                STORAGE_PERMISSION_IMAGE
            )
        } else {
            openGalleryForImage()
        }
    }

    // 카메라 권한
    private fun onCheckCameraPermissions() {
        neededPermissionList = mutableListOf()

        cameraPermissionList.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) neededPermissionList.add(permission)
        }

        if (neededPermissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                neededPermissionList.toTypedArray(),
                CAMERA_PERMISSION
            )
        } else {
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_VIDEO -> {
                neededPermissionList.forEach {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            it
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                }
                openGalleryForVideo()
            }
            STORAGE_PERMISSION_IMAGE -> {
                neededPermissionList.forEach {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            it
                        ) != PackageManager.PERMISSION_GRANTED
                    ) return
                }
                openGalleryForImage()
            }
            CAMERA_PERMISSION -> {
                neededPermissionList.forEach {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            it
                        ) != PackageManager.PERMISSION_GRANTED
                    ) return
                }
                openCamera()
            }
        }
    }

    @SuppressLint("IntentReset")
    private fun openGalleryForVideo() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "video/*"
        galleryLauncher.launch(galleryIntent)
    }

    private fun openGalleryForImage() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imageLauncher.launch(galleryIntent)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(cameraIntent)
    }

    private fun showMethodSelectionDialog(context: Context) {
        // 갤러리랑 카메라 중 선택할 수 있는 diaog
        val dialog = SelectImageMethodDialog(context) { mode ->
            if (mode == 0) {
                onCheckCameraPermissions()
            } else {
                onCheckImagePermissions()
            }
        }
        dialog.show()
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data

                uri?.let {
                    viewModel.setVideoUri(it)
                    it.toVideoThumbnail(this)?.let { image ->
                        viewModel.fileToUrl(image, DataType.SHORTS_THUMBNAIL)
                    } ?: run {
                        showToastMessage("썸네일 추출 실패")
                    }
                } ?: run {
                    showToastMessage("파일 불러오기 실패")
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                navController.navigateUp()
            }
        }

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.cameraImage.value = false

                val uri = result.data?.data
                uri?.let {
                    viewModel.setImageUri(it)
                }
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                viewModel.cameraImage.value = true
                val bitmap = result.data?.extras?.get("data") as Bitmap

                if(viewModel.checkUserMode()) {
                    // admin
                    bitmap.toMultiPart(this)?.let { image ->
                        CameraImageForm.setImage(image)
                    } ?: run {
                        showToastMessage("카메라 이미지 파일 변환 실패")
                    }
                } else {
                    // user
                    bitmap.toMultiPartImage(this)?.let { image ->
                        CameraImageForm.setImage(image)
                    } ?: run {
                        showToastMessage("카메라 이미지 파일 변환 실패")
                    }
                }

                bitmap.saveCameraImage(this).let { uri ->
                    uri?.let {
                        viewModel.setImageUri(it)
                    }
                }
            }
        }
}