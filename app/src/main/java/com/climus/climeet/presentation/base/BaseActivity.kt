package com.climus.climeet.presentation.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.climus.climeet.presentation.customview.LoadingDialog
import com.climus.climeet.presentation.customview.Permission
import com.climus.climeet.presentation.customview.PermissionDialog
import com.climus.climeet.presentation.customview.PermissionSnackBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseActivity<B : ViewDataBinding>(
    private val inflate: (LayoutInflater) -> B,
) : AppCompatActivity() {

    protected lateinit var binding: B
    private lateinit var loadingDialog: LoadingDialog
    private var loadingState = false

    private lateinit var permissionDialog: PermissionDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    fun showPermissionDialog(
        type: Permission,
        confirmBtnClickListener: (Permission) -> Unit,
        refuseBtnClickListener: () -> Unit
    ) {
        permissionDialog = PermissionDialog(this, type, confirmBtnClickListener, refuseBtnClickListener)
        permissionDialog.show()
    }

    fun showPermissionSnackBar(
        view: View,
    ) {
        PermissionSnackBar.make(view) {
            val intent = Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.parse("package:$packageName")
            }
            startActivity(intent)
        }.show()
    }

    fun showLoading(context: Context) {
        if (!loadingState) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
            loadingState = true
        }
    }

    fun dismissLoading() {
        if (loadingState) {
            loadingDialog.dismiss()
            loadingState = false
        }
    }

    fun showToastMessage(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (loadingState) {
            loadingDialog.dismiss()
        }
    }
}