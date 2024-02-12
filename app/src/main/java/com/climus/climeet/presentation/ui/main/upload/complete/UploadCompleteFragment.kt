package com.climus.climeet.presentation.ui.main.upload.complete

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentUploadCompleteBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.upload.UploadViewModel
import com.climus.climeet.presentation.ui.toShortsPlayer
import kotlinx.coroutines.delay

class UploadCompleteFragment :
    BaseFragment<FragmentUploadCompleteBinding>(R.layout.fragment_upload_complete) {

    private val sharedViewModel: UploadViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )

        binding.vm = sharedViewModel

        initCompleteObserve()
    }

    private fun initCompleteObserve() {
        repeatOnStarted {
            sharedViewModel.uploadComplete.collect {
                if (it) {
                    with(binding.ivCheck.drawable) {
                        if (this is Animatable) {
                            (this as Animatable).start()
                        }
                        delay(2000)
                        // todo API 에서 shortsId 반환받은뒤, Fragment Direction에 넣어주기
//                        findNavController().toShortsPlayer(-1, 0)

                        findNavController().toShortsFragment()
                    }
                } else {
                    showToastMessage("업로드 실패")
                    findNavController().toShortsFragment()
                }
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().toShortsFragment()
        }
    }

    private fun NavController.toShortsFragment() {
        val action = UploadCompleteFragmentDirections.actionUploadCompleteFragmentToShortsFragment()
        navigate(action)
    }
}