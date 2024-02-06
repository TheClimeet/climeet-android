package com.climus.climeet.presentation.ui.main.record.calendar.createclimbingrecord

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentCreateClimbingRecordBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.record.adapter.RouteRecordAdapter
import com.climus.climeet.presentation.ui.main.record.adapter.RouteAdapter
import com.climus.climeet.presentation.ui.main.record.adapter.GymLevelAdapter
import com.climus.climeet.presentation.ui.main.record.adapter.SectorNameAdapter
import com.climus.climeet.presentation.ui.toSelectDateBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateClimbingRecordFragment :
    BaseFragment<FragmentCreateClimbingRecordBinding>(R.layout.fragment_create_climbing_record) {

    private lateinit var viewModel: CreateClimbingRecordViewModel
    private var isTimeSet = false
    private lateinit var itemAdapter: RouteRecordAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =
            ViewModelProvider(requireActivity()).get(CreateClimbingRecordViewModel::class.java)
        itemAdapter = RouteRecordAdapter(viewModel)
        binding.vm = viewModel

        viewModel.selectedDate.observe(viewLifecycleOwner, Observer { date ->
            viewModel.setDate()
            binding.tvChoiceDate.setTextColor(Color.WHITE)
        })
        viewModel.selectedStartTime.observe(viewLifecycleOwner, Observer { date ->
            if (isTimeSet) {
                viewModel.setTime()
                binding.tvChoiceTime.setTextColor(Color.WHITE)
            }
        })
        viewModel.selectedEndTime.observe(viewLifecycleOwner, Observer { date ->
            if (isTimeSet) {
                viewModel.setTime()
                binding.tvChoiceTime.setTextColor(Color.WHITE)
            }
        })
        viewModel.selectedCragEvent.observe(viewLifecycleOwner, Observer { event ->
            event?.let { (id, name) ->
                if (viewModel.isSelectedCrag.value) {
                    binding.tvGym.setTextColor(Color.WHITE)
                }
            }
        })

        binding.ivCelebrate.bringToFront()

        viewModel.selectedCragEvent.value?.let { viewModel.getCragInfo(it.first) }
        setRecyclerView()
        initEventObserve()
        initItemObserve()

    }

    private fun setRecyclerView() {
        binding.rvSectorName.adapter = SectorNameAdapter()
        binding.rvSectorLevel.adapter = GymLevelAdapter()
        binding.rvSectorImage.adapter = RouteAdapter()
        binding.rvRouteRecord.adapter = itemAdapter
        binding.rvSectorName.itemAnimator = null
        binding.rvSectorLevel.itemAnimator = null
        binding.rvSectorImage.itemAnimator = null
        binding.rvRouteRecord.itemAnimator = null
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    CreateClimbingRecordEvent.ShowDatePicker -> findNavController().toSelectDateBottomSheetFragment()
                    CreateClimbingRecordEvent.ShowTimePicker -> {
                        isTimeSet = true
                        findNavController().toSelectTimeBottomSheetFragment()
                    }

                    CreateClimbingRecordEvent.NavigateToSelectCrag -> findNavController().toSelectCrag()
                    CreateClimbingRecordEvent.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun initItemObserve() {
        repeatOnStarted {
            viewModel.items.collect { items ->
                itemAdapter.items = items
                itemAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun NavController.toSelectTimeBottomSheetFragment() {
        val action =
            CreateClimbingRecordFragmentDirections.actionCreateClimbingRecordFragmentToSelectTimeBottomFragment()
        navigate(action)
    }

    private fun NavController.toSelectCrag() {
        val action =
            CreateClimbingRecordFragmentDirections.actionCreateClimbingRecordFragmentToCreateSelectCragFragment()
        navigate(action)
    }

}