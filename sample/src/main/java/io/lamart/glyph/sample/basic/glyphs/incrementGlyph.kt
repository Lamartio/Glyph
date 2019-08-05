package io.lamart.glyph.sample.basic.glyphs

import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import io.lamart.glyph.dispose
import io.lamart.glyph.docs.State
import io.lamart.glyph.sample.R
import io.lamart.glyph.sample.basic.SampleGlyph
import io.lamart.glyph.sample.basic.context
import io.lamart.glyph.sample.basic.sampleGlyph

fun incrementGlyph(): SampleGlyph<State> =
    sampleGlyph {
        val view = FloatingActionButton(context)
            .apply {
                setImageResource(R.drawable.ic_add_black_24dp)
                layoutParams =
                    FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                        val density = context.resources.displayMetrics.density
                        val margin = 24f.times(density).toInt()

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