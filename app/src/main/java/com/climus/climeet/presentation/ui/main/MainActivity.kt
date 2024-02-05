package com.climus.climeet.presentation.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.ActivityMainBinding
import com.climus.climeet.presentation.base.BaseActivity
import com.climus.climeet.presentation.customview.Permission
import com.climus.climeet.presentation.ui.toMultiPart
import com.climus.climeet.presentation.util.Constants.STORAGE_PERMISSION
import com.climus.climeet.presentation.util.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var neededPermissionList: MutableList<String>
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBnv()
        initEventObserve()
    }

    private fun setBnv(){

        binding.mainBnv.itemIconTintList = null

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.mainBnv.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _,destination,_ ->
            if(destination.id == R.id.home_fragment || destination.id == R.id.shorts_fragment
                || destination.id == R.id.record_fragment || destination.id == R.id.myPage_fragment || destination.id == R.id.shortsBottomSheetFragment){
                // todo bnv show 해야되는 frag
                binding.mainBnv.visibility = View.VISIBLE
            }else{
                binding.mainBnv.visibility = View.INVISIBLE
            }

        }
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is MainEvent.GoToGalleryForVideo -> onCheckStoragePermissions()
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
            openGalleryForVideo()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION) {
            neededPermissionList.forEach {
                if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED) return
            }
            openGalleryForVideo()
        }
    }

    @SuppressLint("IntentReset")
    private fun openGalleryForVideo() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "video/*"
        galleryLauncher.launch(galleryIntent)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data

                uri?.let {
                    viewModel.setVideoUri(it)
                }
            }
        }
}