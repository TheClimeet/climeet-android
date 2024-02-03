package com.climus.climeet.presentation.ui.main.shorts.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentShortsBottomSheetBinding
import com.climus.climeet.presentation.ui.main.shorts.ShortsFilterViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ShortsBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentShortsBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShortsBottomSheetViewModel by viewModels()
    private val shortsFilterViewModel: ShortsFilterViewModel by activityViewModels()

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_shorts_bottom_sheet,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEventObserve()
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.applyFilter.collect{
                shortsFilterViewModel.setFilterInfo(it)
                dismiss()
            }
        }
    }
}