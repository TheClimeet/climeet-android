package com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selectdate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSelectDateBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectDateBottomFragment : BottomSheetDialogFragment() {

    private val viewModel: SelectDateBottomViewModel by viewModels()
    private var _binding : FragmentSelectDateBottomBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectDateBottomBinding.inflate(inflater, container, false)
        binding.vm = viewModel

        return binding.root
    }


}