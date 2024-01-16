package com.climus.climeet.presentation.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.climus.climeet.databinding.DialogPermissionBinding
import com.climus.climeet.presentation.util.Constants

class PermissionDialog(
    context: Context,
    private val type: Permission,
    private val confirmBtnClickListener: (Permission) -> Unit,
    private val refuseBtnClickListener: () -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogPermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        window?.attributes?.y = 200
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        tvDescription.text = type.msg
        btnConfirm.setOnClickListener {
            confirmBtnClickListener(type)
            dismiss()
        }
        btnRefuse.setOnClickListener {
            refuseBtnClickListener()
            dismiss()
        }
    }
}

enum class Permission(val code: Int, val msg: String) {
    ALARM(Constants.ALARM_PERMISSION, "`클밋`의 알림을 허용해주세요!\n\n해당 기기에서 운동 완료 알림 등\n 서비스 이용에 필요한 안내 사항을\n푸시 알림으로 보내드릴게요.\n\n앱 푸시에 동의하시겠어요?"),
    STORAGE(Constants.STORAGE_PERMISSION, "클밋의 다음 작업을 허용하시겠습니까?\n기기, 사진, 미디어, 파일 엑세스")
}