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
import com.climus.climeet.presentation.ui.main.shorts.adapter.SectorImageAdapter
import com.climus.climeet.presentation.ui.main.shorts.adapter.SectorLevelAdapter
import com.climus.climeet.presentation.ui.main.shorts.adapter.WallNameAdapter
import com.climus.climeet.presentation.ui.main.shorts.bottomsheet.ShortsBottomSheetViewModel
import com.climus.climeet.presentation.ui.main.upload.bottomsheet.UploadBottomSheetViewModel

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
        binding.rvSectorName.adapter = WallNameAdapter()
        binding.rvSectorLevel.adapter = SectorLevelAdapter()
        binding.rvSectorImage.adapter = SectorImageAdapter()
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
                            parentUploadViewModel.applyFilter(it.sector)
                        } else {
                            parentShortsViewModel.applyFilter(it.sector)
                        }
                    }
                    is SelectSectorBottomSheetEvent.DismissDialog -> {
                        parentShortsViewModel.dismissDialog()
                        parentUploadViewModel.dismissDialog()
                    }
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