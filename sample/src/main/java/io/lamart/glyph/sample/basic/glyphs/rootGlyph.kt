package io.lamart.glyph.sample.basic.glyphs

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toolbar
import io.lamart.glyph.Dispose
import io.lamart.glyph.disposeOf
import io.lamart.glyph.output
import io.lamart.glyph.sample.R
import io.lamart.glyph.sample.basic.SampleGlyph
import io.lamart.glyph.sample.basic.State

fun rootGlyph(): SampleGlyph<State> =
    {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.root, parent, false)
            .also(parent::addView)
        val toolBar: Toolbar = layout.findViewById(R.id.toolBar)
        val content: ViewGroup = layout.findViewById(R.id.content)

        val disposeCounter: Dispose = +content + output { it.count } + counterGlyph()

        disposeOf(
            disposeCounter,
            { parent.removeView(layout) }
        )
    }