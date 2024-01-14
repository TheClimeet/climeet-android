package com.climus.climeet.presentation.customview

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.climus.climeet.R
import com.climus.climeet.databinding.DialogPermissionSnackbarBinding
import com.google.android.material.snackbar.Snackbar

class PermissionSnackBar(
    view: View,
    private val btnClickListener: () -> Unit
) {

    companion object {

        fun make(view: View, btnClickListener: () -> Unit) = PermissionSnackBar(view, btnClickListener)
    }

    private val context = view.context
    private val snackBar = Snackbar.make(view, "", 5000).apply {
        anchorView = view
    }

    private val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout

    private val binding: DialogPermissionSnackbarBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.dialog_permission_snackbar,
        null,
        false
    )

    init {
        initView()
    }

    private fun initView() {
        with(snackBarLayout) {
            removeAllViews()
            setPadding(40, 0, 40, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(binding.root, 0)
            binding.btnGotoSetting.setOnClickListener {
                btnClickListener()
            }
        }
    }

    fun show() {
        snackBar.show()
    }
}