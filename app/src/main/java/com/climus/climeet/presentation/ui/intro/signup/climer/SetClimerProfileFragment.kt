package com.climus.climeet.presentation.ui.intro.signup.climer

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.net.Uri
import android.provider.Settings
import android.util.Log
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetClimerProfileBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.google.android.material.snackbar.Snackbar

class SetClimerProfileFragment :
    BaseFragment<FragmentSetClimerProfileBinding>(R.layout.fragment_set_climer_profile) {

    private val viewModel: SetClimerProfileViewModel by viewModels()

//    private var imgUri : Uri? = null
//
//    private val selectImageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            imgUri = result.data?.data
//            Glide.with(this)
//                .load(imgUri)
//                .into(binding.ivProfile)
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

//        // 갤러리 호출
//        binding.ivProfile.setOnClickListener{
//            val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
//            selectImageResultLauncher.launch(intent)
//        }

        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SetClimerProfileEvent.NavigateToBack -> findNavController().navigateUp()
                    is SetClimerProfileEvent.ImageClick -> checkPermissionAndPickImage()
                }
            }
        }
    }

    private fun checkPermissionAndPickImage() {
        val readStoragePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        Log.d("SetImage", "READ_EXTERNAL_STORAGE: $readStoragePermission")

        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d("SetImage", "0번")
                // 이미 권한이 부여되어 있음, 갤러리를 여는 로직 실행
                //pickImageFromGallery()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) -> {
                Log.d("SetImage", "1번")
                // 토스트 or 스낵바로 권한 설정해야 이미지 선택 가능함을 알림

            }
            else -> {
                //권한 요청
                requestPermission()
            }
        }
    }

    private fun requestPermission() {
        Log.d("SetImage", "이건가")
        requestPermissions(
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
            REQUEST_CODE_STORAGE_PERMISSION
        )
    }

    companion object {
        private const val REQUEST_CODE_STORAGE_PERMISSION = 1
        private const val REQUEST_CODE_PICK_IMAGE = 2
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여되었을 때 갤러리를 여는 로직
                Log.d("SetImage", "권한 부여됨")
                //pickImageFromGallery()
            } else {
                // 권한이 거부되었을 때 사용자에게 권한이 없음을 알리는 로직
                Log.d("SetImage", "권한 거부")
                informAboutPermissionDenial()
            }
        }
    }

    private fun informAboutPermissionDenial() {
        Snackbar.make(
            binding.root,
            "권한이 거부되었습니다. 설정에서 권한을 허용해주세요.",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("설정") {
            // 애플리케이션 설정 화면으로 이동하는 인텐트
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", requireActivity().packageName, null)
            intent.data = uri
            startActivity(intent)
        }.show()
    }


}