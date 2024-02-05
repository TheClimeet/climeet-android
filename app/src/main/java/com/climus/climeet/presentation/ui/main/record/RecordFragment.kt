package com.climus.climeet.presentation.ui.main.record

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentRecordBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.climer.setprofile.SetClimerProfileFragmentDirections

class RecordFragment:BaseFragment<FragmentRecordBinding>(R.layout.fragment_record) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController().toSetCalendar()
    }

    private fun NavController.toSetCalendar() {
        val action =
            RecordFragmentDirections.actionRecordFragmentToCalendarFragment()
        navigate(action)
    }

}