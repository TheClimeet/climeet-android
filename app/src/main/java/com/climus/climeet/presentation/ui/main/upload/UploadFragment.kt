package com.climus.climeet.presentation.ui.main.upload

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentUploadBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.CheckPublicBottomSheet
import com.climus.climeet.presentation.ui.main.MainViewModel
import com.climus.climeet.presentation.util.Constants.TAG

class UploadFragment: BaseFragment<FragmentUploadBinding>(R.layout.fragment_upload) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: UploadViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        parentViewModel.goToGalleryForVideo()
        initVideoObserve()
        initEventObserve()
    }

    private fun initVideoObserve(){
        repeatOnStarted {
            parentViewModel.videoUri.collect{
                viewModel.setVideoUri(it)
                Glide.with(requireContext())
                    .load(it)
                    .into(binding.ivThumbnail)
            }
        }
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is UploadEvent.ShowPublicBottomSheet -> {
                        CheckPublicBottomSheet(requireContext(), it.type){
                            viewModel.setPublicState(it)
                        }.show()
                    }
                }
            }
        }
    }
}