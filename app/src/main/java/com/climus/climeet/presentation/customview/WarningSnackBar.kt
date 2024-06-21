package com.climus.climeet.presentation.customview

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.climus.climeet.R
import com.climus.climeet.databinding.DialogWarningSnackbarBinding
import com.google.android.material.snackbar.Snackbar

class WarningSnackBar(
    view: View
) {

    companion object {

        fun make(view: View) = WarningSnackBar(view)
    }

    private val context = view.context
    private val snackBar = Snackbar.make(view, "", 5000).apply {
        anchorView = view
    }

    private val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout

    private val binding: DialogWarningSnackbarBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.dialog_warning_snackbar,
        null,
        false
    )

    init {
        initView()
    }

    private fun initView() {
        with(snackBarLayout) {
            removeAllViews()
            setPadding(45, 0, 45, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(binding.root, 0)
            binding.ivClose.setOnClickListener {
                snackBar.dismiss()
            }
        }
    }

    // 메세지 수정
    fun setText(text: String) {
        binding.tvInfo.text = text
    }

    fun show() {
        snackBar.show()
    }
}