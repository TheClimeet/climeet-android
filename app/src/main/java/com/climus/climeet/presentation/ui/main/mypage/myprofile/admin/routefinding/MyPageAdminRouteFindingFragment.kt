package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding

import android.net.Uri
import android.os.Bundle
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
import com.climus.climeet.presentation.ui.main.global.gymprofile.route.GymProfileRouteViewModel
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiLevelItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.adapter.LevelColorAdapter
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.adapter.RouteFindingLevelAdapter
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.adapter.RouteFindingRouteAdapter
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.adapter.RouteFindingSectorAdapter
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.MyPageAdminRouteData
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.bottomsheet.SetLevelBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.update

@AndroidEntryPoint
class MyPageAdminRouteFindingFragment :
    BaseFragment<FragmentMyPageAdminRouteFindingBinding>(R.layout.fragment_my_page_admin_route_finding) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val dateViewModel: SelectDateBottomSheetViewModel by viewModels()
    private val routeViewModel: GymProfileRouteViewModel by activityViewModels()
    private val viewModel: MyPageAdminRouteFindingViewModel by activityViewModels()
    private lateinit var adapter: LevelColorAdapter
    private lateinit var levelAdapter: RouteFindingLevelAdapter
    private lateinit var sectorAdapter: RouteFindingSectorAdapter
    private lateinit var routeAdapter: RouteFindingRouteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        viewModel.getRouteFindingData()
        viewModel.setSelectedDate(MyPageAdminRouteData.selectedDate)

        binding.btnRouteFindingBack.setOnClickListener {
            viewModel.navigateToBack(requireContext())
        }

        setRV()
        initEventObserve()
        initStateObserve()
        initParentImageObserve()
        sectorClickListener()
        changeSectorFloor()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    MyPageAdminRouteFindingEvent.ShowDatePicker -> {
                        SelectDateBottomSheet(
                            requireContext(),
                            dateViewModel,
                            MyPageAdminRouteData.selectedDate,
                            MyPageAdminRouteData::setSelectedDate
                        ) { date ->
                            viewModel.setSelectedDate(date)
                            routeViewModel.setSelectedDate(date)
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
                    MyPageAdminRouteFindingEvent.DeleteSecondFloor -> {
                        binding.switchSectorFloor.isChecked = false
                    }

                    is MyPageAdminRouteFindingEvent.UpdateRouteFindingData -> if (it.isSuccess) {
                        showToastMessage(it.msg)
                        findNavController().navigateUp()
                    } else {
                        showToastMessage(it.msg)
                    }
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
                    levelAdapter.submitList(state.levelList)
                    levelAdapter.notifyDataSetChanged()
                }
                binding.rvRouteFindingSector.post {
                    sectorAdapter.notifyDataSetChanged()
                }

                val curLayoutGymImg = state.layoutList[viewModel.selectedLayoutFloor.value - 1].gymImg
                if (curLayoutGymImg == "") {
                    binding.tvImageExplain.visibility = View.VISIBLE
                } else {
                    setImage(curLayoutGymImg.toUri(), binding.ivAddGymIamge)
                    binding.tvImageExplain.visibility = View.GONE
                }

            }
        }
        repeatOnStarted {
            viewModel.selectedLevel.collect {
                handleLevelSelection(it)
                binding.rvLevelColor.post {
                    adapter.notifyDataSetChanged()
                }
            }
        }
        repeatOnStarted {
            viewModel.selectedLayoutFloor.collect {
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
                    sectorName = binding.etSectorName.text.toString(),
                    sectorFloor = viewModel.selectedSectorFloor.value
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

                checkExistSectorName(sector.sectorName) -> {
                    showCustomSnackbar("중복된 섹터/벽면의 이름입니다")
                }

                else -> {
                    if (viewModel.modifyingSector.value == viewModel.defaultSectorItem) {
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

    private fun changeSectorFloor() {
        with(binding) {
            switchSectorFloor.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && !viewModel.isSecondFloorExist.value!!) {
                    showCustomSnackbar("2층이 존재하지 않습니다.")
                    switchSectorFloor.isChecked = false
                } else {
                    if (isChecked) {
                        viewModel.changeFloorSector(2)
                        tvSwitchFirst.setTextColor(R.color.white)
                        tvSwitchSecond.setTextColor(R.color.black)
                    } else {
                        viewModel.changeFloorSector(1)
                        tvSwitchFirst.setTextColor(R.color.black)
                        tvSwitchSecond.setTextColor(R.color.white)
                    }

                }
            }
        }
    }

    private fun checkExistSectorName(sectorName: String): Boolean {
        viewModel.uiState.value.sectorList.forEach {
            if (it.sectorName == sectorName) {
                return true
            }
        }

        return false
    }

    private fun setTvExplain() {
        binding.tvExplain.text = "${viewModel.selectedLevel.value.colorName} 레벨은 이미 등록되어 있어요"
        binding.tvExplain.setTextColor(resources.getColor(R.color.cm_red))
    }

    private fun setRV() {
        adapter = LevelColorAdapter(viewModel)
        binding.rvLevelColor.adapter = adapter

        levelAdapter = RouteFindingLevelAdapter(viewModel)
        binding.rvRouteFindingLevel.adapter = levelAdapter

        sectorAdapter = RouteFindingSectorAdapter(viewModel)
        binding.rvRouteFindingSector.adapter = sectorAdapter

        routeAdapter = RouteFindingRouteAdapter(viewModel)
        binding.rvRouteFindingRoute.adapter = routeAdapter
    }

    private fun handleLevelSelection(selectedLevel: UiLevelItem) {
        val isCompletable = !viewModel.isColorAlreadySelected()
        if (isCompletable) {
            setTvExplain()
        } else {
            updateExplanationText(selectedLevel)
        }
    }

    private fun updateExplanationText(level: UiLevelItem) {
        binding.tvExplain.setTextColor(resources.getColor(R.color.cm_main))
        when {
            isCompetitionLevel(level) -> {
                binding.layoutSetLevel.isClickable = false
                binding.tvExplain.text = "컴피티션 레벨은 C에 고정되어 있어요"
                handleCompetitionColor(level)
            }

            isLevelNothing(level) -> {
                binding.tvExplain.text = "컴피티션 레벨은 C에 고정되어 있어요"
                viewModel.isCompletable.postValue(false)
            }

            else -> {
                binding.layoutSetLevel.isClickable = true
                binding.tvExplain.text = ""
            }
        }
    }

    private fun handleCompetitionColor(level: UiLevelItem) {
        if (level.colorName == "컴피") {
            viewModel.isCompletable.postValue(true)
        } else {
            binding.layoutSetLevel.isClickable = true
            viewModel.selectLevel("레벨 설정")
            viewModel.isCompletable.postValue(false)
        }
    }

    private fun isCompetitionLevel(level: UiLevelItem) =
        level.climeetLevel == "C"

    private fun isLevelNothing(level: UiLevelItem) =
        level.colorName == "-" || level.climeetLevel == "레벨 설정"

    private fun setImage(uri: Uri, ivIamge: AppCompatImageView) {
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