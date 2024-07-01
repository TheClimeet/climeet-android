package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.bottomsheet

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.climus.climeet.R
import com.climus.climeet.databinding.DialogSetLevelBottomSheetBinding
import com.climus.climeet.presentation.customview.WarningSnackBar
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.MyPageAdminRouteFindingViewModel
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.LevelColorData
import com.google.android.material.bottomsheet.BottomSheetDialog

class SetLevelBottomSheet(
    context: Context,
    private val viewModel: MyPageAdminRouteFindingViewModel
) : BottomSheetDialog(context) {

    private lateinit var binding: DialogSetLevelBottomSheetBinding
    private var selectedLevel = ""
    private val handler = Handler(Looper.getMainLooper())
    private var levelCheckRunnable: Runnable? = null

    init {
        setOnShowListener {
            initView()
        }
    }

    private fun initView() {
        binding = DialogSetLevelBottomSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLevelPicker()
        checkIfLevelAlreadySelected()
    }

    private fun setLevelPicker() {
        val levelPicker = binding.npLevel
        val levelsArray = LevelColorData.LEVELS.toTypedArray()

        levelPicker.minValue = 0
        levelPicker.maxValue = levelsArray.size - 1
        levelPicker.displayedValues = levelsArray

        val currentIndex = LevelColorData.LEVELS.indexOf(viewModel.selectedLevel.value.climeetLevel).takeIf { it != -1 } ?: 0
        levelPicker.value = currentIndex

        selectedLevel = levelsArray[currentIndex]

        levelPicker.wrapSelectorWheel = false

        levelPicker.setOnValueChangedListener { _, _, newVal ->
            selectedLevel = LevelColorData.LEVELS[newVal]
            levelCheckRunnable?.let { handler.removeCallbacks(it) }

            levelCheckRunnable = Runnable {
                checkIfLevelAlreadySelected()
            }
            handler.postDelayed(levelCheckRunnable!!, 250)
        }

        binding.tvOk.setOnClickListener {
            viewModel.selectLevel(selectedLevel)
            dismiss()
        }
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.tvCancel.setOnClickListener {
            dismiss()
        }

    }

    private fun checkIfLevelAlreadySelected() {
        val isLevelAlreadySelected =
            viewModel.uiState.value.levelList.any { it.climeetLevel == selectedLevel }
        val isLevelIsNotMine = viewModel.modifyingLevel.value.climeetLevel != selectedLevel
        if (isLevelAlreadySelected) {
            if(isLevelIsNotMine) {
                binding.tvOk.isEnabled = false
                binding.tvOk.setBackgroundResource(R.drawable.rect_silverfill_nostroke_5radius)
                binding.tvOk.setTextColor(ContextCompat.getColor(context, R.color.white))
                showCustomSnackbar("$selectedLevel 레벨은 이미 설정되어 있어요!")
            } else {
                binding.tvOk.isEnabled = true
                binding.tvOk.setBackgroundResource(R.drawable.rect_mainfill_nostroke_5radius)
                binding.tvOk.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        } else {
            binding.tvOk.isEnabled = true
            binding.tvOk.setBackgroundResource(R.drawable.rect_mainfill_nostroke_5radius)
            binding.tvOk.setTextColor(ContextCompat.getColor(context, R.color.black))
        }
    }

    private fun showCustomSnackbar(message: String) {
        WarningSnackBar.make(binding.layoutLevelBottom).apply {
            setText(message)
            show()
        }
    }

}