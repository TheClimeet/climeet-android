package com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selecttime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSelectTimeBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

@AndroidEntryPoint
class SelectTimeBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel: SelectTimeBottomViewModel by viewModels()
    private var _binding: FragmentSelectTimeBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_select_time_bottom_sheet,
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
        setTimePicker(viewModel.startTime.value)
    }

    private fun initEventObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.event.collect {
                when (it) {
                    SelectTimeBottomEvent.CloseFragment -> dismiss()
                    SelectTimeBottomEvent.ClickStart -> setTimePicker(viewModel.startTime.value)
                    SelectTimeBottomEvent.ClickEnd -> setTimePicker(viewModel.endTime.value)
                }
            }
        }
    }

    private fun setTimePicker(time: LocalTime) {
        with(binding) {
            timepicker.setOnTimeChangedListener(null)
            timepicker.hour = time.hour
            timepicker.minute = time.minute
            if (viewModel.selectState.value) {
                timepicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                    viewModel.setStartTime(
                        hourOfDay,
                        minute
                    )
                }
            } else {
                timepicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                    viewModel.setEndTime(
                        hourOfDay,
                        minute
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.selectState.value = false
    }

}