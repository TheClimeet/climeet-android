package com.climus.climeet.presentation.ui.main.global.gymprofile.info

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.climus.climeet.R
import com.climus.climeet.app.App
import com.climus.climeet.databinding.FragmentProfileReviewBottomSheetBinding
import com.climus.climeet.presentation.ui.main.global.gymprofile.GymProfileViewModel
import com.climus.climeet.presentation.ui.main.global.selectsector.SelectSectorBottomSheetEvent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class GymReviewBottomSheetFragment : BottomSheetDialogFragment() {

    private val mainViewModel: GymProfileViewModel by activityViewModels()
    private val infoViewModel: GymProfileInfoViewModel by activityViewModels()
    private val viewModel: GymReviewBottomSheetFragmentViewModel by viewModels()
    private var _binding: FragmentProfileReviewBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile_review_bottom_sheet,
            container,
            false
        )

        binding.vm = viewModel
        binding.mainVM = mainViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getGymId()
        initEventObserve()
        initEditReview()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog){
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun initEventObserve(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.event.collect{
                when(it){
                    is GymReviewBottomSheetEvent.CreateReview -> createReview()
                    is GymReviewBottomSheetEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    private fun initEditReview() {
        viewLifecycleOwner.lifecycleScope.launch {
            infoViewModel.uiState.collect {
                if(it.myGymReview != null){
                   setView(it.myGymReview.rating, (it.myGymReview.content))
                }
            }
        }
    }

    private fun setView(rating: Float, content: String){
        // 작성한 리뷰 보여줌
        viewModel.reviewRating.value = rating
        viewModel.reviewText.value = content
    }

    fun showToastMessage(message: String) {
        val toast = Toast.makeText(App.getContext(), message, Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun createReview(){
        if (infoViewModel.uiState.value.myGymReview != null) {
            viewModel.editReview()
            infoViewModel.getReviewInfo()
            Log.d("gym_profile", "리뷰 수정 완료")
        } else {
            viewModel.createReview()
            infoViewModel.getGymTabInfo()
            Log.d("gym_profile", "리뷰 작성 완료")
        }

        dismiss()
    }
}