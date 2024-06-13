package com.climus.climeet.presentation.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.climus.climeet.databinding.DialogSelectImageMethodBinding

class SelectImageMethodDialog(
    context: Context,
    private val itemClickListener: (Int) -> Unit,
) : Dialog(context) {

    private lateinit var binding: DialogSelectImageMethodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSelectImageMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        window?.let { window ->
            val layoutParams = window.attributes
            layoutParams.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.y = 200 // 200 pixels from the bottom
            window.attributes = layoutParams
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        buttonCamera.setOnClickListener {
            itemClickListener(0)
            dismiss()
        }
        buttonGallery.setOnClickListener {
            itemClickListener(1)
            dismiss()
        }
    }
}