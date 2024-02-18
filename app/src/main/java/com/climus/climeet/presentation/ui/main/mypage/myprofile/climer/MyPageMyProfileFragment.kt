package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.app.App
import com.climus.climeet.databinding.FragmentMypageClimerMyprofileBinding
import com.climus.climeet.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageMyProfileFragment: BaseFragment<FragmentMypageClimerMyprofileBinding>(R.layout.fragment_mypage_climer_myprofile) {

    private val viewModel: MyPageMyProfileViewModel by viewModels()
    private var isManger: Boolean = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isManger = checkUserType() // Manager
        setUpInitialSetting()
        setupOnClickListener()
    }

    private fun setUpInitialSetting() {

        if(!isManger) {
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