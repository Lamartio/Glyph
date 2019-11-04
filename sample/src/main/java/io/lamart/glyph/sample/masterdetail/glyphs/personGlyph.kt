package io.lamart.glyph.sample.masterdetail.glyphs

import android.view.ViewGroup
import android.widget.TextView
import io.lamart.glyph.Glyph
import io.lamart.glyph.disposeOf
import io.lamart.glyph.sample.masterdetail.Actions
import io.lamart.glyph.sample.masterdetail.Person
import io.lamart.glyph.sample.masterdetail.State

fun personGlyph(): Glyph<ViewGroup, Actions, State, Person?> = { bind ->
    val view = TextView(parent.context).also(parent::addView)

    bind { view.text = it?.name }

    disposeOf { parent.removeView(view) }
}
