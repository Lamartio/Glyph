package io.lamart.glyph.sample.glyphs

import android.support.design.widget.FloatingActionButton
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import io.lamart.glyph.dispose
import io.lamart.glyph.sample.*
import io.lamart.glyph.sample.doc.State

fun incrementGlyph(): SampleGlyph<State> =
    sampleGlyph {
        val view = FloatingActionButton(context)
            .apply {
                layoutParams =
                    FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                        val density = context.resources.displayMetrics.density
                        val margin = 16f.times(density).toInt()

                        gravity = Gravity.BOTTOM or Gravity.END
                        marginEnd = margin
                        bottomMargin = margin
                    }
            }
            .also(parent::addView)

        view.setOnClickListener {
            actions.increment()
        }

        dispose { parent.removeView(view) }
    }