package com.climus.climeet.presentation.ui.intro.signup.admin.error

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetCragErrorBinding
import com.climus.climeet.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetCragErrorFragment: BaseFragment<FragmentSetCragErrorBinding>(R.layout.fragment_set_crag_error) {

    private val args: SetCragErrorFragmentArgs by navArgs()
    private val cragId by lazy { args.cragId }
    private val cragImgUrl by lazy { args.cragImgUrl }
    private val cragName by lazy { args.cragName }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}