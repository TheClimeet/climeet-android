package com.climus.climeet.presentation.ui.intro.signup.climer.noticesetting

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentNoticeSettingBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm
import com.climus.climeet.presentation.util.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NoticeSettingFragment : BaseFragment<FragmentNoticeSettingBinding>(R.layout.fragment_notice_setting) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNoticeSettingAgreement.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS)
            ) {
                ClimerSignupForm.setNotice(true)
                Log.d("Push Permission", ClimerSignupForm.noticePermission.toString())
                findNavController().toComplete()
            } else {
                // 푸쉬 권한 없음
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), Constants.REQ_PERMISSION_PUSH)
            }
        }

        binding.tvNoticeSettingDisagree.setOnClickListener {
            ClimerSignupForm.setNotice(false)
            Log.d("Push Permission", ClimerSignupForm.noticePermission.toString())
            findNavController().toComplete()
        }

        binding.ivClimbingGoalBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvNoticeSettingNickname.text = ClimerSignupForm.nickName + "님,"
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            Constants.REQ_PERMISSION_PUSH -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // 알림 권한을 거부했을 때 실행
                    ClimerSignupForm.setNotice(false)
                    Log.d("Push Permission", ClimerSignupForm.noticePermission.toString())
                    informAboutPermissionDenial()
                } else {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                    ClimerSignupForm.setNotice(true)
                    Log.d("Push Permission", ClimerSignupForm.noticePermission.toString())
                    findNavController().toComplete()
                }
            }
        }
    }

    private fun NavController.toComplete() {
        val action = NoticeSettingFragmentDirections.actionNoticeSettingFragmentToCompletetFragment()
        navigate(action)
    }

    private fun informAboutPermissionDenial() {
        val snack = Snackbar.make(binding.root,
            "알림 권한을 확인해주세요",
            Snackbar.LENGTH_SHORT)

        val snackView = snack.view
        val textView = snackView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        val icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_snackbar_check)
        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        textView.setCompoundDrawables(icon, null, null, null)
        textView.compoundDrawablePadding = dpToPx(12)
        snackView.setPadding(snackView.paddingLeft, dpToPx(8), snackView.paddingRight, dpToPx(8))

        snack.setTextColor(Color.BLACK)
        snack.setBackgroundTint(Color.WHITE)
        snack.setActionTextColor(ContextCompat.getColor(requireActivity(), R.color.cm_main))
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