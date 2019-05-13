package io.lamart.glyph.sample

import android.view.ViewGroup
import io.lamart.glyph.Bind
import io.lamart.glyph.Dispose
import io.lamart.glyph.Glyph
import io.lamart.glyph.Scope


typealias SampleScope<T> = Scope<Actions, ViewGroup, State, T>
typealias SampleGlyph<T> = Glyph<Actions, ViewGroup, State, T>

fun <T> sampleGlyph(block: SampleScope<T>.(bind: Bind<T>) -> Dispose): SampleGlyph<T> = block

val SampleScope<*>.actions
    get() = dependencies

val SampleScope<*>.context
    get() = parent.context