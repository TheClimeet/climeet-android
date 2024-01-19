package com.climus.climeet.presentation.ui.intro.signup.climer.setprofile

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetClimerProfileBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetClimerProfileFragment :
    BaseFragment<FragmentSetClimerProfileBinding>(R.layout.fragment_set_climer_profile) {

    private val parentViewModel: IntroViewModel by activityViewModels()
    private val viewModel: SetClimerProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pvm = parentViewModel
        binding.vm = viewModel

        initEventObserve()
        initParentImageObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SetClimerProfileEvent.NavigateToBack -> findNavController().navigateUp()
                    is SetClimerProfileEvent.NavigateToSetLevel -> findNavController().toSetClimerLevel()
                }
            }
        }
    }

    private fun initParentImageObserve() {
        repeatOnStarted {
            parentViewModel.imageUri.collect {
                setImage(it)
            }
        }
    }

    private fun setImage(uri: Uri) {
        viewModel.setImageUri(requireContext(), uri)
    }

    private fun NavController.toSetClimerLevel() {
        val action =
            SetClimerProfileFragmentDirections.actionSetClimerProfileFragmentToSetClimerLevelFragment()
        navigate(action)
    }

}

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("imageUri")
    fun bindImageUri(imageView: ImageView, uri: Uri?) {
        if (uri != null) {
            Glide.with(imageView.context)
                .load(uri)
                .circleCrop()
                .into(imageView)
        } else {

        }
    }
}