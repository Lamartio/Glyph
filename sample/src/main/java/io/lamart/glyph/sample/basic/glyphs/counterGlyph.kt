package io.lamart.glyph.sample.basic.glyphs

import android.os.Build
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import io.lamart.glyph.dispose
import io.lamart.glyph.sample.basic.context
import io.lamart.glyph.sample.basic.sampleGlyph

fun counterGlyph() = sampleGlyph<Int> { bind ->
    val view = TextView(context)
        .apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                gravity = Gravity.CENTER
                appearance = android.R.style.TextAppearance_DeviceDefault_Large
            }
        }
        .also(parent::addView)

    bind { count ->
        view.text = count.toString()
    }

    dispose { parent.removeView(view) }
}


private var TextView.appearance: Int
    get() = throw UnsupportedOperationException()
    set(value) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            setTextAppearance(context, value)
        else
            setTextAppearance(value)
    }

