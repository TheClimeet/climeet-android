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
    ALARM(Constants.ALARM_PERMISSION, "클밋의 다음 작업을 허용하시겠습니까?\n알림 수신"),
    STORAGE(Constants.STORAGE_PERMISSION, "클밋의 다음 작업을 허용하시겠습니까?\n기기, 사진, 미디어, 파일 엑세스")
}