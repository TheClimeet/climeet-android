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
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.databinding.BindingAdapter
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetClimerProfileBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.climer.SetClimerProfileFragmentDirections
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SetClimerProfileFragment :
    BaseFragment<FragmentSetClimerProfileBinding>(R.layout.fragment_set_climer_profile) {

    private val viewModel: SetClimerProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SetClimerProfileEvent.NavigateToBack -> findNavController().navigateUp()
                    is SetClimerProfileEvent.ImageClick -> checkPermissionAndPickImage()
                    is SetClimerProfileEvent.NavigateToSetLevel -> findNavController().toSetClimerLevel()
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
                pickImageFromGallery()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) -> {
                Log.d("SetImage", "1번")
                // 토스트 or 스낵바로 권한 설정해야 이미지 선택 가능함을 알림
                informAboutPermissionDenial()
            }

            else -> {
                //권한 요청
                requestPermission()
            }
        }
    }

    private fun pickImageFromGallery() {
        //val intent = Intent(MediaStore.ACTION_PICK_IMAGES) 이건 갤러리를 띄우는 다른 방법
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
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

    private fun NavController.toSetClimerLevel() {
        val action =
            SetClimerProfileFragmentDirections.actionSetClimerProfileFragmentToSetClimerLevelFragment()
        navigate(action)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            viewModel.setImageUri(imageUri) // ViewModel에 이미지 URI 업데이트
        }
    }

    private fun informAboutPermissionDenial() {
        val snack = Snackbar.make(binding.root,
            "엑세스 권한이 거부되었습니다",
            Snackbar.LENGTH_INDEFINITE)

        val snackView = snack.view
        val textView = snackView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_snackbar_check)
        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        textView.setCompoundDrawables(icon, null, null, null)
        textView.compoundDrawablePadding = dpToPx(12)
        snackView.setPadding(snackView.paddingLeft, dpToPx(8), snackView.paddingRight, dpToPx(8))

        snack.setTextColor(Color.BLACK)
        snack.setBackgroundTint(Color.WHITE)
        snack.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.cm_main))
        snack.setAction("설정"){
            // 애플리케이션 설정 화면으로 이동하는 인텐트
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", requireActivity().packageName, null)
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

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("imageUri")
    fun bindImageUri(imageView: ImageView, uri: Uri?) {
        if (uri != null) {
            Glide.with(imageView.context)
                .load(uri)
                .circleCrop()
                .into(imageView)
        } else {

        }
    }
}