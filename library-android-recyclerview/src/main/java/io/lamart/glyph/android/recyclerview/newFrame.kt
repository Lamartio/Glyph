package io.lamart.glyph.android.recyclerview

import android.content.Context
import android.os.Build
import android.view.ViewGroup
import android.widget.FrameLayout

fun newFrame(
    context: Context,
    params: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
): ViewGroup =
    FrameLayout(context).apply {
        layoutParams = params
        clipToPadding = false
        clipChildren = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            clipToOutline = false
    }