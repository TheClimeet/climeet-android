package com.climus.climeet.presentation.ui.intro.login.climer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentClimerLoginBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroViewModel
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm
import com.climus.climeet.presentation.ui.main.MainActivity
import com.climus.climeet.presentation.util.Constants.KAKAO
import com.climus.climeet.presentation.util.Constants.NAVER
import com.climus.climeet.presentation.util.Constants.TAG
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClimerLoginFragment :
    BaseFragment<FragmentClimerLoginBinding>(R.layout.fragment_climer_login) {

    private val parentViewModel: IntroViewModel by activityViewModels()
    private val viewModel: ClimerLoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.signUpProgressStop()
        binding.vm = viewModel
        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is ClimerLoginEvent.StartKakaoLogin -> kakaoLogin()
                    is ClimerLoginEvent.StartNaverLogin -> naverLogin()
                    is ClimerLoginEvent.NavigateBack -> findNavController().navigateUp()

                    is ClimerLoginEvent.GoToMainActivity -> {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }

                    is ClimerLoginEvent.NavigateToSignUp -> {
                        ClimerSignupForm.setSocialType(it.socialType)
                        ClimerSignupForm.setToken(it.token)

                        findNavController().toSetClimerNick()
                    }

                    is ClimerLoginEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    private fun kakaoLogin() {

        // 카카오톡 설치 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {

            // 카카오톡 로그인
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->

                // 로그인 실패 부분
                if (error != null) {

                    // 사용자가 취소
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    // 다른 오류
                    else {
                        UserApiClient.instance.loginWithKakaoAccount(
                            requireContext(),
                            callback = kakaoEmailCb
                        ) // 카카오 이메일 로그인
                    }
                }
                // 로그인 성공 부분
                else if (token != null) {
                    viewModel.login(KAKAO, token.accessToken)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                requireContext(),
                callback = kakaoEmailCb
            ) // 카카오 이메일 로그인
        }
    }

    // 카카오톡 이메일 로그인 콜백
    private val kakaoEmailCb: (OAuthToken?, Throwable?) -> Unit = { token, error ->

        if (error != null) {
            Log.e(TAG, "이메일 로그인 실패 $error")
        } else if (token != null) {
            viewModel.login(KAKAO, token.accessToken)
        }
    }

    private fun naverLogin() {
        val oauthLoginCallback = object : OAuthLoginCallback {

            override fun onSuccess() {
                viewModel.login(NAVER, NaverIdLoginSDK.getAccessToken().toString())
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }
        NaverIdLoginSDK.authenticate(requireContext(), oauthLoginCallback)
    }

    private fun NavController.toSetClimerNick() {
        val action = ClimerLoginFragmentDirections.actionClimerLoginFragmentToSetClimerNameFragment()
        navigate(action)
    }

}