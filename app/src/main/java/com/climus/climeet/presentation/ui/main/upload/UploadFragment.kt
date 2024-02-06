package com.climus.climeet.presentation.ui.main.upload

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.abedelazizshe.lightcompressorlibrary.config.Configuration
import com.abedelazizshe.lightcompressorlibrary.config.SaveLocation
import com.abedelazizshe.lightcompressorlibrary.config.SharedStorageConfiguration
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.app.App
import com.climus.climeet.databinding.FragmentUploadBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.CheckPublicBottomSheet
import com.climus.climeet.presentation.ui.main.MainViewModel
import com.climus.climeet.presentation.ui.main.global.selectsector.BottomSheetState
import com.climus.climeet.presentation.util.Constants.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UploadFragment : BaseFragment<FragmentUploadBinding>(R.layout.fragment_upload) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: UploadViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        parentViewModel.goToGalleryForVideo()
        initVideoObserve()
        initEventObserve()
    }

    private fun initVideoObserve() {
        repeatOnStarted {
            parentViewModel.videoUri.collect {
                startCompress(it)
                Glide.with(requireContext())
                    .load(it)
                    .into(binding.ivThumbnail)
            }
        }

        repeatOnStarted {
            parentViewModel.shortsThumbnail.collect {
                viewModel.setThumbnailImg(it)
            }
        }
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is UploadEvent.ShowPublicBottomSheet -> {
                        CheckPublicBottomSheet(requireContext(), it.type) {
                            viewModel.setPublicState(it)
                        }.show()
                    }

                    is UploadEvent.NavigateToSearchCragBottomSheet -> {
                        BottomSheetState.state = "UPLOAD"
                        findNavController().toUploadBottomSheets()
                    }

                    is UploadEvent.ShowToastMessage -> showToastMessage(it.msg)
                    is UploadEvent.ShowLoading -> showLoading(requireContext())
                    is UploadEvent.DismissLoading -> dismissLoading()
                }
            }
        }
    }

    private fun startCompress(uri: Uri) {
        CoroutineScope(Dispatchers.Main).launch {

            val uris = listOf(uri)

            VideoCompressor.start(
                context = App.getContext(),
                uris,
                isStreamable = false,
                sharedStorageConfiguration = SharedStorageConfiguration(
                    saveAt = SaveLocation.movies,
                    subFolderName = "climeet"
                ),
                configureWith = Configuration(
                    quality = VideoQuality.LOW,
                    videoNames = uris.map { uri -> uri.pathSegments.last() },
                    isMinBitrateCheckEnabled = true,
                ),
                listener = object : CompressionListener {
                    override fun onProgress(index: Int, percent: Float) {
                        viewModel.setCompressProgress(percent.toInt())
                    }

                    override fun onStart(index: Int) {
                        binding.ivThumbnail.alpha = 0.2F
                        viewModel.startCompress()
                    }

                    override fun onSuccess(index: Int, size: Long, path: String?) {
                        binding.ivThumbnail.alpha = 1F

                        Log.d(TAG,size.toString())

                        path?.let {
                            val file = File(it)
                            val requestFile = file.asRequestBody("video/mp4".toMediaTypeOrNull())
                            val videoFile =
                                MultipartBody.Part.createFormData("file", file.name, requestFile)
                            viewModel.finishCompress(videoFile)
                        } ?: run {

                        }
                    }

                    override fun onFailure(index: Int, failureMessage: String) {

                    }

                    override fun onCancelled(index: Int) {

                    }
                }
            )
        }
    }

    private fun NavController.toUploadBottomSheets() {
        val action = UploadFragmentDirections.actionUploadFragmentToUploadBottomSheetFragment()
        navigate(action)
    }
}