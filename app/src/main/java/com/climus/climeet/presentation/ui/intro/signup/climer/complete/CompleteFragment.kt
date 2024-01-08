package com.climus.climeet.presentation.ui.intro.signup.climer.complete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentCompletetBinding
import com.climus.climeet.presentation.base.BaseFragment

class CompleteFragment : BaseFragment<FragmentCompletetBinding>(R.layout.fragment_completet) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completet, container, false)
    }

}