package com.climus.climeet.presentation.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.climus.climeet.databinding.DialogPermissionBinding

class PermissionDialog(
    context: Context,
    private val description: String,
    private val confirmBtnClickListener: () -> Unit,
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
        tvDescription.text = description
        btnConfirm.setOnClickListener {
            confirmBtnClickListener()
            dismiss()
        }
        btnRefuse.setOnClickListener {
            refuseBtnClickListener()
            dismiss()
        }
    }
}