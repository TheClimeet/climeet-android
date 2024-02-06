package com.climus.climeet.presentation.ui.main.global.selectsector

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSelectSectorBottomSheetBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.GymLevelAdapter
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.RouteImageAdapter
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.SectorNameAdapter
import com.climus.climeet.presentation.ui.main.shorts.bottomsheet.ShortsBottomSheetViewModel
import com.climus.climeet.presentation.ui.main.upload.bottomsheet.UploadBottomSheetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectSectorBottomSheetFragment :
    BaseFragment<FragmentSelectSectorBottomSheetBinding>(R.layout.fragment_select_sector_bottom_sheet) {

    private val parentShortsViewModel: ShortsBottomSheetViewModel by activityViewModels()
    private val parentUploadViewModel: UploadBottomSheetViewModel by activityViewModels()
    private val viewModel: SelectSectorBottomSheetViewModel by viewModels()

    private val args: SelectSectorBottomSheetFragmentArgs by navArgs()
    private val cragId by lazy { args.cragId }
    private val cragName by lazy { args.cragName }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.setCragInfo(cragId, cragName)
        setRecyclerView()
        initEventObserve()
    }

    private fun setRecyclerView() {
        binding.rvSectorName.adapter = SectorNameAdapter()
        binding.rvSectorLevel.adapter = GymLevelAdapter()
        binding.rvSectorImage.adapter = RouteImageAdapter()
        binding.rvSectorName.itemAnimator = null
        binding.rvSectorLevel.itemAnimator = null
        binding.rvSectorImage.itemAnimator = null
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SelectSectorBottomSheetEvent.NavigateToBack -> findNavController().toBack()
                    is SelectSectorBottomSheetEvent.ApplyFilter -> {
                        if(BottomSheetState.state == "UPLOAD"){
                            parentUploadViewModel.applyFilter(it.filter)
                        } else {
                            parentShortsViewModel.applyFilter(it.filter)
                        }
                    }
                    is SelectSectorBottomSheetEvent.DismissDialog -> {
                        parentShortsViewModel.dismissDialog()
                        parentUploadViewModel.dismissDialog()
                    }

                    is SelectSectorBottomSheetEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    private fun NavController.toBack() {
        val action =
            SelectSectorBottomSheetFragmentDirections.actionSelectSectorBottomSheetFragmentToSearchCragBottomSheetFragment()
        navigate(action)
    }

}