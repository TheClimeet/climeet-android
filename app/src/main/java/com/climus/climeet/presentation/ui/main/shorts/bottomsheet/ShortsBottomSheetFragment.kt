package com.climus.climeet.presentation.ui.main.shorts.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentShortsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ShortsBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentShortsBottomSheetBinding? = null
    private val binding get() = _binding!!

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

    }
}