package com.climus.climeet.presentation.ui.main.mypage.account

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.app.App
import com.climus.climeet.data.model.response.UserProfileInfoResponse
import com.climus.climeet.databinding.FragmentMypageAccountBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroActivity
import com.climus.climeet.presentation.ui.intro.IntroViewModel
import com.climus.climeet.presentation.util.Constants
import com.climus.climeet.service.TimerService
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyPageAccountFragment :
    BaseFragment<FragmentMypageAccountBinding>(R.layout.fragment_mypage_account) {

    private val viewModel: MyPageAccountViewModel by viewModels()
    private val parentViewModel: IntroViewModel by activityViewModels()
    private var isManger: Boolean = true
    private var userProfile: UserProfileInfoResponse? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.getUserProfile()

        initStateObserve()
        initEventObserver()

        setUpInitialSetting()
        initParentImageObserve()
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.let { vm ->
                vm.uiState.collect { uiState ->
                    userProfile = uiState.myProfile
                    if (userProfile != null) {
                        binding.tvMypageNickname.text = userProfile!!.userName
                        Glide.with(binding.root.context)
                            .load(userProfile!!.profileImgUrl)
                            .into(binding.ivMypageMyProfile)

                    }
                }
            }
        }
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    MyPageAccountEvent.ShowLogoutDialog -> logout()
                    MyPageAccountEvent.ShowWithdrawDialog -> withdraw()
                }
            }
        }
    }

    private fun initParentImageObserve() {
        repeatOnStarted {
            parentViewModel.imageUri.collect {
                setImage(it)
            }
        }
    }

    private fun setImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .circleCrop() // 기본 이미지
            .into(binding.ivMypageMyProfile)
    }

    private fun setUpInitialSetting() {
        isManger = viewModel.checkUserMode()

        if (isManger) {
            binding.icVerified.visibility = View.VISIBLE
            binding.mypageWhoami.text = "암장 관리자로 만났어요"
            binding.btnCompleteLogin.text = "로그인 연동 완료"
        } else {
            binding.icVerified.visibility = View.GONE
            binding.mypageWhoami.text = "클라이머로 만났어요"

            val loginType = viewModel.checkLoginType()
            if (loginType == Constants.KAKAO) {
                binding.btnCompleteLogin.text = "카카오 로그인 연동 완료"
            } else {
                binding.btnCompleteLogin.text = "네이버 로그인 연동 완료"
            }
        }
    }

    private fun logout() {
        val logoutDialog = LayoutInflater.from(activity).inflate(R.layout.logout_dialog, null)
        val builder = AlertDialog.Builder(activity)
            .setView(logoutDialog)

        val alertDialog = builder.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val cancelBtn = alertDialog.findViewById<AppCompatButton>(R.id.btn_dialog_logout_cancel)
        val logoutBtn = alertDialog.findViewById<AppCompatButton>(R.id.btn_dialog_logout)

        cancelBtn!!.setOnClickListener {
            alertDialog.dismiss()
        }

        logoutBtn!!.setOnClickListener {
            alertDialog.dismiss()
            App.sharedPreferences.edit()
                .clear()
                .apply()

            viewModel.deleteLoginType()

            // 스톱워치 서비스 중단 후 로그아웃
            val intent = Intent(context, TimerService::class.java)
            if (TimerService.serviceRunning.value != null) {
                context?.stopService(intent)
            }

            // back stack 지우기
            val logoutIntent = Intent(requireContext(), IntroActivity::class.java)
            logoutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(logoutIntent)
            activity?.finish()
        }
    }

    private fun withdraw() {
        val withdrawDialog = LayoutInflater.from(activity).inflate(R.layout.withdraw_dialog, null)
        val builder = AlertDialog.Builder(activity)
            .setView(withdrawDialog)

        val alertDialog = builder.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val cancelBtn = alertDialog.findViewById<AppCompatButton>(R.id.btn_dialog_withdraw_cancel)
        val withdrawBtn = alertDialog.findViewById<AppCompatButton>(R.id.btn_dialog_withdraw)

        cancelBtn!!.setOnClickListener {
            alertDialog.dismiss()
        }

        withdrawBtn!!.setOnClickListener {
            isManger = viewModel.checkUserMode()

            if (isManger) {
                // todo : 관리자 탈퇴 api 연동
            } else {
                // todo : 유저 탈퇴 api 연동

                val loginType = viewModel.checkLoginType()
                if (loginType == Constants.KAKAO) {
                    Log.d("withdraw", "카카오 탈퇴")
                    kakaoWithdraw()
                } else {
                    Log.d("withdraw", "네이버 탈퇴")
                    naverWithdraw()
                }
            }
            alertDialog.dismiss()

            // back stack 지우기
            val withdrawIntent = Intent(requireContext(), IntroActivity::class.java)
            withdrawIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(withdrawIntent)
            activity?.finish()
        }
    }

    // 네이버 연결 해제
    private fun naverWithdraw() {
        NidOAuthLogin().callDeleteTokenApi(requireContext(), object : OAuthLoginCallback {
            override fun onSuccess() {
                //서버에서 토큰 삭제에 성공한 상태
            }

            override fun onFailure(httpStatus: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업 없음
                Log.d("withdraw", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                Log.d("withdraw", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
            }

            override fun onError(errorCode: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없음
                onFailure(errorCode, message)
            }
        })
    }

    // 카카오 연결 해제
    private fun kakaoWithdraw() {
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("withdraw", "연결 끊기 실패", error)
            } else {
                Log.i("withdraw", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }
    }
}