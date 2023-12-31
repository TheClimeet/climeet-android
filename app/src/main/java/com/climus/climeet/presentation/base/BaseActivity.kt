package com.climus.climeet.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.climus.climeet.presentation.customview.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseActivity<B : ViewDataBinding>(
    private val inflate: (LayoutInflater) -> B,
) : AppCompatActivity() {

    protected lateinit var binding: B
    private lateinit var loadingDialog: LoadingDialog
    private var loadingState = false

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