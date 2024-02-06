package com.climus.climeet.presentation.ui.main.upload.complete

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentUploadCompleteBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.upload.UploadViewModel
import kotlinx.coroutines.delay

class UploadCompleteFragment: BaseFragment<FragmentUploadCompleteBinding>(R.layout.fragment_upload_complete) {

    private val sharedViewModel: UploadViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = sharedViewModel

        initCompleteObserve()


    }

    private fun initCompleteObserve(){
        repeatOnStarted {
            sharedViewModel.uploadComplete.collect{
                if(it){
                    with(binding.ivCheck.drawable){
                        if (this is Animatable) {
                            (this as Animatable).start()
                        }
                    }
                } else {
                    showToastMessage("업로드 실패")
                }
            }
        }
    }


}