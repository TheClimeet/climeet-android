package com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selectdate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class SharedBottomSheetViewModel : ViewModel() {
    val selectedDate: MutableLiveData<LocalDate> by lazy {
        MutableLiveData<LocalDate>()
    }

}