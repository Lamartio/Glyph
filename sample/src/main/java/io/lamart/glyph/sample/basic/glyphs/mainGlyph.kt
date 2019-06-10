package io.lamart.glyph.sample.basic.glyphs

import android.view.LayoutInflater
import android.view.ViewGroup
import io.lamart.glyph.dispose
import io.lamart.glyph.sample.*
import io.lamart.glyph.docs.State
import io.lamart.glyph.sample.basic.SampleGlyph
import io.lamart.glyph.sample.basic.context
import io.lamart.glyph.sample.basic.sampleGlyph
import io.lamart.glyph.state

fun mainGlyph(): SampleGlyph<State> = sampleGlyph {
    val inflater = LayoutInflater.from(context)
    val view = inflater
        .inflate(R.layout.main, parent, false)
        .also(parent::addView) as ViewGroup
    val disposeButton = +view + incrementGlyph()
    val disposeCounter = +view + state { it.count } + counterGlyph()

    dispose(
        disposeCounter,
        disposeButton,
        { parent.removeView(view) }
    )
}