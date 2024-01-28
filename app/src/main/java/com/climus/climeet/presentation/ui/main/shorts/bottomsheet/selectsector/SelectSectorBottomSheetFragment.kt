package com.climus.climeet.presentation.ui.main.shorts.bottomsheet.selectsector

import androidx.fragment.app.activityViewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSelectSectorBottomSheetBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.shorts.ShortsFilterViewModel

class SelectSectorBottomSheetFragment: BaseFragment<FragmentSelectSectorBottomSheetBinding>(R.layout.fragment_select_sector_bottom_sheet) {

    private val shortsFilterViewModel: ShortsFilterViewModel by activityViewModels()

}