package com.climus.climeet.presentation.ui.intro

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.climus.climeet.databinding.ActivityIntroBinding
import com.climus.climeet.presentation.base.BaseActivity
import com.climus.climeet.presentation.util.Constants.STORAGE_PERMISSION
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>(ActivityIntroBinding::inflate) {

    private val viewModel: IntroViewModel by viewModels()

    private lateinit var neededPermissionList: MutableList<String>
    private val storagePermissionList =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(  // 안드로이드 13 이상 필요한 권한들
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            arrayOf(  // 안드로이드 13 미만 필요한 권한들
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel

        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is IntroEvent.GoToGallery -> onCheckStoragePermissions()
                }
            }
        }
    }

    private fun onCheckStoragePermissions() {
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
                STORAGE_PERMISSION
            )
        } else {
            openGallery()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openGallery()
            }
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(galleryIntent)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data

                uri?.let {
                    viewModel.setImage(it)
                }
            }
        }

}