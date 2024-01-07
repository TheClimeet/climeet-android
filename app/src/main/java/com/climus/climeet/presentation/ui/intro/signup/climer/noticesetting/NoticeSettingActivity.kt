package com.climus.climeet.presentation.ui.intro.signup.climer.noticesetting

import android.content.Intent
import android.os.Bundle
import com.climus.climeet.databinding.ActivityNoticeSettingBinding
import com.climus.climeet.presentation.base.BaseActivity
import com.climus.climeet.presentation.ui.intro.signup.climer.complete.CompleteActivity

class NoticeSettingActivity : BaseActivity<ActivityNoticeSettingBinding>(ActivityNoticeSettingBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnNoticeSettingAgreement.setOnClickListener {
            val intent = Intent(this, CompleteActivity::class.java)
            startActivity(intent)
        }

        binding.tvNoticeSettingDisagree.setOnClickListener {
            val intent = Intent(this, CompleteActivity::class.java)
            startActivity(intent)
        }

    }
}