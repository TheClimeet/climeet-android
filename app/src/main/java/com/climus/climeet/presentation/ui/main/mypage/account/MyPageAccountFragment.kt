package com.climus.climeet.presentation.ui.main.mypage.account

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.app.App
import com.climus.climeet.data.model.response.UserProfileInfoResponse
import com.climus.climeet.databinding.FragmentMypageAccountBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyPageAccountFragment: BaseFragment<FragmentMypageAccountBinding>(R.layout.fragment_mypage_account) {

    private val viewModel: MyPageAccountViewModel by viewModels()
    private var isManger: Boolean = true
    private var userProfile: UserProfileInfoResponse? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewModel.getUserProfile()
        initStateObserve()

        setUpInitialSetting()
        setupOnClickListener()
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.let { vm ->
                vm.uiState.collect { uiState ->
                    userProfile = uiState.myProfile
                    if(userProfile != null) {
                        binding.tvMypageNickname.text = userProfile!!.userName
                        Glide.with(binding.root.context)
                            .load(userProfile!!.profileImgUrl)
                            .into(binding.ivMypageMyProfile)

                    }
                }
            }
        }
    }

    private fun setUpInitialSetting() {
        isManger = checkUserType()

        if(isManger) {
            binding.icVerified.visibility = View.VISIBLE
            binding.mypageWhoami.text="암장 관리자로 만났어요"
            binding.btnCompleteLogin.text="로그인 연동 완료"
        } else {
            binding.icVerified.visibility = View.GONE
            binding.mypageWhoami.text="클라이머로 만났어요"
            binding.btnCompleteLogin.text="카카오 로그인 연동 완료"
        }
    }

    private fun checkUserType(): Boolean {
        val userType = App.sharedPreferences.getString("X_MODE", "")
        return userType == "ADMIN"
    }

    private fun setupOnClickListener() {

        binding.btnLogout.setOnClickListener {
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
                val intent = Intent(requireContext(), IntroActivity::class.java)
                startActivity(intent)
            }
        }

        binding.btnWithdraw.setOnClickListener {
            val withdrawDialog = LayoutInflater.from(activity).inflate(R.layout.withdraw_dialog, null)
            val builder = AlertDialog.Builder(activity)
                .setView(withdrawDialog)

            val alertDialog = builder.show()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val cancelBtn = alertDialog.findViewById<AppCompatButton>(R.id.btn_dialog_withdraw_cancel)
            val logoutBtn = alertDialog.findViewById<AppCompatButton>(R.id.btn_dialog_withdraw)

            cancelBtn!!.setOnClickListener {
                alertDialog.dismiss()
            }

            logoutBtn!!.setOnClickListener {
                alertDialog.dismiss()
            }
        }

    }
}