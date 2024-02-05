package com.climus.climeet.presentation.customview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.climus.climeet.R
import com.climus.climeet.databinding.DialogCheckPublicBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class CheckPublicBottomSheet(
    context: Context,
    private var curType: PublicType,
    private val changeType: (PublicType) -> Unit
) : BottomSheetDialog(context) {

    private var binding: DialogCheckPublicBottomSheetBinding

    init{
        binding = DialogCheckPublicBottomSheetBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setBottomSheetListener()
    }

    private fun setBottomSheetListener(){
        when(curType){
            PublicType.PUBLIC -> binding.rbPublic.isChecked = true
            PublicType.FOLLWER -> binding.rbFollower.isChecked = true
            PublicType.ONLYME -> binding.rbOnlyMe.isChecked = true
            else -> binding.rbPublic.isChecked = true
        }

        binding.rgState.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb_public -> curType = PublicType.PUBLIC
                R.id.rb_follower -> curType = PublicType.FOLLWER
                R.id.rb_only_me -> curType = PublicType.ONLYME
            }
        }

        binding.btnComplete.setOnClickListener {
            changeType(curType)
            dismiss()
        }
    }

}

enum class PublicType(val text: String){
    EMPTY(""),
    PUBLIC("전체 공개"),
    FOLLWER("팔로워만 공개"),
    ONLYME("나만 보기")
}