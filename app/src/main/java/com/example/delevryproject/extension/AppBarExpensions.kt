package com.example.delevryproject.extension

import android.widget.TextView
import com.google.android.material.appbar.AppBarLayout

//스크롤을 내리면 tolbar에 제목이 작성되는 기능
fun AppBarLayout(View : TextView) {
    AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
        val topPadding = 300f.fromDpToPx().toFloat()
        val realAlphaScrollHeight = appBarLayout.measuredHeight - appBarLayout.totalScrollRange
        val abstractOffset = Math.abs(verticalOffset)

        val realAlphaVerticalOffset: Float = if (abstractOffset - topPadding < 0) 0f else abstractOffset - topPadding

        if (abstractOffset < topPadding) {
            View.alpha = 0f
            return@OnOffsetChangedListener
        }
        val percentage = realAlphaVerticalOffset / realAlphaScrollHeight
        View.alpha = 1 - (if (1 - percentage * 2 < 0) 0f else 1 - percentage * 2)
    }
}