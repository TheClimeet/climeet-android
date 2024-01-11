package com.climus.climeet.presentation.ui.intro.signup.admin.alarm

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import android.Manifest
import android.os.Build
import android.util.Log
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetAdminAlarmBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.admin.AdminSignupForm
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SetAdminAlarmFragment: BaseFragment<FragmentSetAdminAlarmBinding>(R.layout.fragment_set_admin_alarm) {

    private val viewModel: SetAdminAlarmViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.navigateToNextAlarmOn()   // 권한이 허용됨
            } else {
                // 권한이 거부됨
                informAboutPermissionDenial()   // 권한이 허용됨
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 바인딩 변수에 뷰모델 연결
        binding.vm = viewModel

        // AdminSignupForm의 지점명을 가져와 붙여줄 예정 (임시로 이름을 넣어둠)
        binding.tvExplainLarge.text = AdminSignupForm.cragName + " " + binding.tvExplainLarge.text

        binding.btnSetAlarm.setOnClickListener{
            requestAlarmPermission()
        }

        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SetAdminAlarmEvent.NavigateToBack -> findNavController().navigateUp()  // 서비스 설정으로 이동
                    is SetAdminAlarmEvent.NavigateToNextAlarmOn -> navigateNext()
                    is SetAdminAlarmEvent.NavigateToNextAlarmOff -> navigateNext()
                }
            }
        }
    }

    private val REQUEST_CODE = 1000

    fun requestAlarmPermission() {
        Log.d("admin", "알림 권한 요청")
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // 권한이 이미 허용됨
                Log.d("admin", "알림 권한 이미 허용")
                viewModel.navigateToNextAlarmOn()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                // 사용자가 권한 요청을 거부한 적이 있는 경우 설명 제공
                Log.d("admin", "알림 권한 거부")
                informAboutPermissionDenial()
            }
            else -> {
                // 처음 권한을 요청하는 경우
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    // Android 13 이전 버전에서는 알림 권한을 명시적으로 요청할 필요가 없음
                    viewModel.navigateToNextAlarmOn()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d("admin", "알림 권한 처음 요청됨")
        when (requestCode) {
            REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    viewModel.navigateToNextAlarmOn()   // 권한 허용됨
                    Log.d("admin", "알림 권한 허용")
                } else {
                    informAboutPermissionDenial()   // 권한 거부됨
                }
                return
            }
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

    // 완료 페이지로 이동
    private fun navigateNext(){
        val action = SetAdminAlarmFragmentDirections.actionSetAdminAlarmFragmentToSetAdminCompleteFragment()
        findNavController().navigate(action)
    }
}