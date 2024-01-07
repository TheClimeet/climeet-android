package com.climus.climeet.presentation.ui.intro.signup.admin.background

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetAdminBackgroundBinding
import com.climus.climeet.presentation.base.BaseFragment

class SetAdminBackgroundFragment : BaseFragment<FragmentSetAdminBackgroundBinding>(R.layout.fragment_set_admin_background) {

    private val viewModel: SetAdminBackgroundViewModel by viewModels()

    // Permission 요청용 Launcher
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // 권한이 부여되었을 때 갤러리 열기
                getImageFromGallery()
            } else {
                // 권한이 거부되었을 때 사용자에게 알림
                informAboutPermissionDenial()
            }
        }

    // 이미지 가져오는 Launcher
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val selectedImageUri = data?.data
                viewModel.setImage(selectedImageUri.toString()) // 이미지 링크 넘김
                Log.d("admin", "이미지 링크 뷰모델에 넘김 : $selectedImageUri")
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initEventObserve()

        // 이미지 URI 변경을 감지하고 화면에 반영
        viewModel.imgUri.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.ic_add_img) // 기본 이미지
                    .into(binding.ivImgIcon)
            } else {
                // 이미지 URI가 null이면 기본 이미지 설정
                binding.ivImgIcon.setImageResource(R.drawable.ic_add_img)
            }
        }
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SetAdminBackgroundEvent.NavigateToBack -> findNavController().navigateUp()  // 개인정보 설정으로 이동
                    is SetAdminBackgroundEvent.NavigateToNext -> navigateNext()
                    is SetAdminBackgroundEvent.SetImageClick -> checkPermission()
                }
            }
        }
    }

    // 권한 확인
    private fun checkPermission() {
        // 갤러리 권한이 있는지 확인
        val readStoragePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_MEDIA_IMAGES
        )

        if (readStoragePermission == PackageManager.PERMISSION_GRANTED) {
            Log.d("admin", "권한 있음")
            // 이미 권한이 부여되어 있으면 갤러리 열기
            getImageFromGallery()

        } else {
            Log.d("admin", "권한 없음")
            // 권한이 없으면 권한 요청
            requestPermission()
        }
    }

    // 권한 요청
    private fun requestPermission() {
        Log.d("admin", "권한 요청")

        // TIRAMISU(SDK 33) 이상일 때만 READ_MEDIA_IMAGES로 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            Log.d("admin", "Android 13 이상 권한 요청")
        } else {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            Log.d("admin", "Android 13 이전 권한 요청")
        }
    }

    // 권한 요청 후 거부됨을 알림
    private fun informAboutPermissionDenial() {
        // todo : 인우 스낵바 띄우기 코드 추가
    }

    // 갤러리에서 사진 가져와 뷰모델에 넘겨주기
    private fun getImageFromGallery() {
        Log.d("admin", "이미지 가져오기")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    // 서비스 설정으로 이동
    private fun navigateNext(){
        val action = SetAdminBackgroundFragmentDirections.actionSetAdminBackgroundFragmentToSetAdminServiceFragment()
        findNavController().navigate(action)
    }
}