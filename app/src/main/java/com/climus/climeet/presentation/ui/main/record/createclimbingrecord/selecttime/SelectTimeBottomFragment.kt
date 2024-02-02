package com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selecttime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSelectTimeBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectTimeBottomFragment : BottomSheetDialogFragment() {

    private val viewModel: SelectTimeBottomViewModel by viewModels()
    private var _binding: FragmentSelectTimeBottomBinding? = null
    private val binding get() = _binding!!

    private val meridiemArr = arrayOf("오전", "오후") // am, pm
    private val hoursArr = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
    private val minutesArr = Array(60) { i -> i.toString() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_select_time_bottom,
            container,
            false
        )
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEventObserve()
        setNP()

    }

    private fun initEventObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.event.collect {
                when (it) {
                    SelectTimeBottomEvent.CloseFragment -> dismiss()
                }
            }
        }
    }

    private fun setNP(){
        with(binding){
            npMeridiem.wrapSelectorWheel = false // 순환 막기
            npMeridiem.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            npHour.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            npMin.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            // 최소값 설정
            npMeridiem.minValue = 0
            npHour.minValue = 1
            npMin.minValue = 0

            // 최대값 설정
            npMeridiem.maxValue = meridiemArr.size - 1
            npHour.maxValue = 12
            npMin.maxValue = minutesArr.size - 1

            //  array 값 넣기
            npMeridiem.displayedValues = meridiemArr
            npHour.displayedValues = hoursArr
            npMin.displayedValues = minutesArr

            // 선택된 기본값 설정
            npMeridiem.value = 0
            npHour.value = 9
            npMin.value = 0
        }
    }
}