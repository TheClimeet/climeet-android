package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMyPageAdminRouteFindingBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.WarningSnackBar
import com.climus.climeet.presentation.customview.selectdate.SelectDateBottomSheet
import com.climus.climeet.presentation.customview.selectdate.SelectDateBottomSheetViewModel
import com.climus.climeet.presentation.ui.main.MainViewModel
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.adapter.LevelColorAdapter
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.adapter.RouteFindingLevelAdapter
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.adapter.RouteFindingSectorAdapter
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.bottomsheet.SetLevelBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.update

@AndroidEntryPoint
class MyPageAdminRouteFindingFragment :
    BaseFragment<FragmentMyPageAdminRouteFindingBinding>(R.layout.fragment_my_page_admin_route_finding) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val dateViewModel: SelectDateBottomSheetViewModel by viewModels()
    private val viewModel: MyPageAdminRouteFindingViewModel by activityViewModels()
    private lateinit var lvAdapter: RouteFindingLevelAdapter
    private lateinit var adapter: LevelColorAdapter
    private lateinit var sectorAdapter: RouteFindingSectorAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        setRV()
        initEventObserve()
        initStateObserve()
        initParentImageObserve()
        sectorClickListener()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    MyPageAdminRouteFindingEvent.ShowDatePicker -> {
                        SelectDateBottomSheet(
                            requireContext(),
                            dateViewModel,
                            viewModel.selectedDate.value,
                            viewModel::noUse
                        ) { date ->
                            viewModel.setSelectedDate(date)
                        }.show()
                    }

                    MyPageAdminRouteFindingEvent.ShowSetLevel -> {
                        SetLevelBottomSheet(
                            requireContext(),
                            viewModel
                        ).show()
                    }

                    MyPageAdminRouteFindingEvent.GoToGallery -> context?.let { itsContext ->
                        parentViewModel.goToSetProfileImage(
                            itsContext
                        )
                    }

                    is MyPageAdminRouteFindingEvent.ShowLayoutImg -> setImage(
                        it.uri,
                        binding.ivAddGymIamge
                    )

                    MyPageAdminRouteFindingEvent.GoToCreateRoute -> findNavController().toCreateRoute()
                    MyPageAdminRouteFindingEvent.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.uiState.collect { state ->
                val isCompletable = !viewModel.isColorAlreadySelected()
                if (isCompletable) {
                    setTvExplain()
                }
                binding.rvRouteFindingLevel.post {
                    lvAdapter.submitList(state.levelList)
                    lvAdapter.notifyDataSetChanged()
                }
                binding.rvRouteFindingSector.post {
                    sectorAdapter.notifyDataSetChanged()
                }

                if (state.layoutList[viewModel.selectedFloor.value - 1].gymImg == "") {
                    binding.tvImageExplain.visibility = View.VISIBLE
                } else {
                    binding.tvImageExplain.visibility = View.GONE
                }
            }
        }
        repeatOnStarted {
            viewModel.selectedLevel.collect {
                val isCompletable = !viewModel.isColorAlreadySelected()
                if (isCompletable) {
                    setTvExplain()
                } else {
                    binding.tvExplain.setTextColor(resources.getColor(R.color.cm_main))
                    if (it.climeetLevel == "C" && it.colorName == "컴피") {
                        binding.layoutSetLevel.isClickable = false
                        binding.tvExplain.text = "컴피티션 레벨은 C에 고정되어 있어요"
                        viewModel.isCompletable.postValue(true)
                    } else {
                        binding.layoutSetLevel.isClickable = true
                        binding.tvExplain.text = ""
                    }
                    if (it.colorName == "-" || it.climeetLevel == "레벨 설정") {
                        binding.tvExplain.text = "컴피티션 레벨은 C에 고정되어 있어요"
                        viewModel.isCompletable.postValue(false)
                    }
                }
                binding.rvLevelColor.post {
                    adapter.notifyDataSetChanged()
                }
            }
        }
        repeatOnStarted {
            viewModel.selectedFloor.collect {
                if (it == 2) {
                    binding.btnDeleteSecondFloor.visibility = View.VISIBLE
                } else {
                    binding.btnDeleteSecondFloor.visibility = View.GONE
                }
                if (viewModel.uiState.value.layoutList[it - 1].gymImg == "") {
                    binding.tvImageExplain.visibility = View.VISIBLE
                } else {
                    binding.tvImageExplain.visibility = View.GONE
                }
            }
        }
        repeatOnStarted {
            viewModel.selectedSector.collect {
                setImage(it.sectorImg.toUri(), binding.ivAddSectorIamge)
                binding.etSectorName.setText(it.sectorName)
            }
        }
    }

    private fun initParentImageObserve() {
        repeatOnStarted {
            parentViewModel.imageUri.collect {
                if (viewModel.selectedImageType.value == DataType.GYM) {
                    setImage(it, binding.ivAddGymIamge)
                }
                viewModel.updateImg(it.toString())
            }
        }
    }

    private fun sectorClickListener() {
        binding.btnSectorComplete.setOnClickListener {
            viewModel.selectedSector.update {
                it.copy(
                    sectorName = binding.etSectorName.text.toString()
                )
            }
            val sector = viewModel.selectedSector.value
            when {
                sector.sectorImg.isEmpty() && sector.sectorName.isEmpty() -> {
                    showCustomSnackbar("섹터/벽면을 설정해주세요")
                }
                sector.sectorImg.isEmpty() -> {
                    showCustomSnackbar("섹터/벽면의 사진을 넣어주세요")
                }
                sector.sectorName.isEmpty() -> {
                    showCustomSnackbar("섹터/벽면의 이름을 입력해주세요")
                }
                else -> {
                    if(viewModel.modifyingSector.value == viewModel.defaultSectorItem) {
                        viewModel.addSector()
                    } else {
                        viewModel.modifySector()
                    }
                    binding.etSectorName.setText("")
                }
            }
        }
        binding.tvAddSector.setOnClickListener {
            binding.etSectorName.setText("")
            viewModel.resetSector()
        }
    }

    private fun setTvExplain() {
        binding.tvExplain.text = "${viewModel.selectedLevel.value.colorName} 레벨은 이미 등록되어 있어요"
        binding.tvExplain.setTextColor(resources.getColor(R.color.cm_red))
    }

    private fun setRV() {
        adapter = LevelColorAdapter(viewModel)
        binding.rvLevelColor.adapter = adapter

        lvAdapter = RouteFindingLevelAdapter(viewModel)
        binding.rvRouteFindingLevel.adapter = lvAdapter

        sectorAdapter = RouteFindingSectorAdapter(viewModel)
        binding.rvRouteFindingSector.adapter = sectorAdapter
    }

    private fun setImage(uri: Uri, ivIamge: AppCompatImageView) {
        Log.d("tlqkf", "$ivIamge")
        Glide.with(this)
            .load(uri)
            .apply(RequestOptions().dontTransform())
            .placeholder(R.drawable.ic_add_image_background)
            .into(ivIamge)
    }

    private fun showCustomSnackbar(message: String) {
        WarningSnackBar.make(binding.layoutAddSector).apply {
            setText(message)
            show()
        }
    }

    private fun NavController.toCreateRoute() {
        val action =
            MyPageAdminRouteFindingFragmentDirections.actionMyPageAdminRouteFindingFragmentToMyPageAdminSetRouteFragment()
        navigate(action)
    }
}