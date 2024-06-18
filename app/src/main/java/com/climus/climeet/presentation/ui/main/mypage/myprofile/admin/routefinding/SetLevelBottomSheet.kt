package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding

import android.content.Context
import com.climus.climeet.databinding.DialogSetLevelBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class SetLevelBottomSheet(
    context: Context,
    private var curLevel: String,
    private var selectedLevelList: List<String>,
    private var changeLevel: (String) -> Unit
) : BottomSheetDialog(context) {

    private lateinit var binding: DialogSetLevelBottomSheetBinding

    private fun initView() {
        binding = DialogSetLevelBottomSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLevelPicker()
    }

    private fun setLevelPicker() {
        // todo 레벨 리스트로 levelPicker 설정

        // todo 선택되어있던 레벨로 설정
    }

}