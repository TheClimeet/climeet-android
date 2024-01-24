package com.climus.climeet.presentation.customview

import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.climus.climeet.R

class NoticePopup(
    private val anchorView: View,
    private val message: String
) {

    companion object {
        fun make(anchorView: View, message: String) = NoticePopup(anchorView, message)
    }

    private val context = anchorView.context
    private val inflater = LayoutInflater.from(context)
    private val popupView = inflater.inflate(R.layout.dialog_notice_record, null)
    private val popupWindow = PopupWindow(
        popupView,
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    private val handler = Handler(Looper.getMainLooper())
    private val dismissRunnable = Runnable { popupWindow.dismiss() }

    init {
        val tvNotice = popupView.findViewById<TextView>(R.id.tv_notice)
        tvNotice.text = message

        val ivClose = popupView.findViewById<ImageView>(R.id.iv_close)
        ivClose.setOnClickListener {
            popupWindow.dismiss()
        }
    }

    fun show() {
        // 팝업의 너비와 높이를 측정
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        // 중앙 정렬
        val x = (context.resources.displayMetrics.widthPixels - popupView.measuredWidth) / 2
        val y = 100

        popupWindow.showAtLocation(
            anchorView,
            Gravity.NO_GRAVITY,
            x,
            y
        )

        // 5초가 지나면 자동으로 사라진다
        handler.postDelayed(dismissRunnable, 5000)
    }
}
