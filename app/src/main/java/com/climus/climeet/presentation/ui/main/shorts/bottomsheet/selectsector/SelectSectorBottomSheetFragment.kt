package com.climus.climeet.presentation.ui.main.shorts.bottomsheet.selectsector

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
import com.climus.climeet.presentation.ui.main.shorts.ShortsFilterViewModel
import com.climus.climeet.presentation.ui.main.shorts.adapter.SectorImageAdapter
import com.climus.climeet.presentation.ui.main.shorts.adapter.SectorLevelAdapter
import com.climus.climeet.presentation.ui.main.shorts.adapter.SectorNameAdapter
import com.climus.climeet.presentation.ui.main.shorts.bottomsheet.ShortsBottomSheetViewModel

class SelectSectorBottomSheetFragment: BaseFragment<FragmentSelectSectorBottomSheetBinding>(R.layout.fragment_select_sector_bottom_sheet) {

    private val parentViewModel: ShortsBottomSheetViewModel by activityViewModels()
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

    private fun setRecyclerView(){
        binding.rvSectorName.adapter = SectorNameAdapter()
        binding.rvSectorLevel.adapter = SectorLevelAdapter()
        binding.rvSectorImage.adapter = SectorImageAdapter()
        binding.rvSectorName.itemAnimator = null
        binding.rvSectorLevel.itemAnimator = null
        binding.rvSectorImage.itemAnimator = null
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is SelectSectorBottomSheetEvent.NavigateToBack -> findNavController().toBack()
                    is SelectSectorBottomSheetEvent.ApplyFilter -> parentViewModel.applyFilter(it.sector)
                }
            }
        }
    }

    private fun NavController.toBack(){
        val action = SelectSectorBottomSheetFragmentDirections.actionSelectSectorBottomSheetFragmentToSearchCragBottomSheetFragment()
        navigate(action)
    }

}