package com.climus.climeet.presentation.ui.intro

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Build.*
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.climus.climeet.R
import com.climus.climeet.databinding.ActivityIntroBinding
import com.climus.climeet.presentation.base.BaseActivity
import com.climus.climeet.presentation.util.Constants.STORAGE_PERMISSION
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>(ActivityIntroBinding::inflate) {

    private val viewModel: IntroViewModel by viewModels()

    private lateinit var neededPermissionList: MutableList<String>
    private val storagePermissionList =
        if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            arrayOf(  // 안드로이드 13 이상 필요한 권한들
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
            } else {
                informAboutPermissionDenial()
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

    private fun informAboutPermissionDenial() {
        val snack = Snackbar.make(binding.root,
            "엑세스 권한이 거부되었습니다",
            Snackbar.LENGTH_INDEFINITE)

        val snackView = snack.view
        val textView = snackView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        val icon = ContextCompat.getDrawable(this, R.drawable.ic_snackbar_check)
        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        textView.setCompoundDrawables(icon, null, null, null)
        textView.compoundDrawablePadding = dpToPx(12)
        snackView.setPadding(snackView.paddingLeft, dpToPx(8), snackView.paddingRight, dpToPx(8))

        snack.setTextColor(Color.BLACK)
        snack.setBackgroundTint(Color.WHITE)
        snack.setActionTextColor(ContextCompat.getColor(this, R.color.cm_main))
        snack.setAction("설정"){
            // 애플리케이션 설정 화면으로 이동하는 인텐트
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", this.packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        snack.show()

        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            snack.dismiss()
        }

    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

}