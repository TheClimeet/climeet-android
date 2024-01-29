package com.climus.climeet.presentation.ui.main.shorts

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShortsFilterViewModel @Inject constructor(): ViewModel() {

    var cragId: Long = 0
    var cragName = ""

    fun setFilterInfo(cragId: Long, cragName: String){
        this.cragId = cragId
        this.cragName = cragName
    }

}