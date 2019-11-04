package io.lamart.glyph.sample.basic

import android.view.ViewGroup
import io.lamart.glyph.Bind
import io.lamart.glyph.Dispose
import io.lamart.glyph.Glyph
import io.lamart.glyph.GlyphScope

typealias SampleGlyphScope<O> = GlyphScope<ViewGroup, Actions, State, O>
typealias SampleGlyph<O> = Glyph<ViewGroup, Actions, State, O>

fun <T> sampleGlyph(block: SampleGlyphScope<T>.(bind: Bind<T>) -> Dispose): SampleGlyph<T> = block

val SampleGlyphScope<*>.context
    get() = parent.context

