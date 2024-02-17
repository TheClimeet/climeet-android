package com.climus.climeet.presentation.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.climus.climeet.databinding.DialogDeleteBinding

class DeleteDialog(
    context: Context,
    private val itemClickListener: (Boolean) -> Unit,
) : Dialog(context) {

    private lateinit var binding: DialogDeleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        window?.attributes?.y = 200
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnDelete.setOnClickListener {
            itemClickListener(true)
            dismiss()
        }
        btnRefuse.setOnClickListener {
            itemClickListener(false)
            dismiss()
        }
    }
}