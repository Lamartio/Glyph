package io.lamart.glyph.sample.masterdetail.glyphs

import android.view.LayoutInflater
import android.view.ViewGroup
import io.lamart.glyph.Glyph
import io.lamart.glyph.disposeOf
import io.lamart.glyph.outputOf
import io.lamart.glyph.sample.R
import io.lamart.glyph.sample.masterdetail.Actions
import io.lamart.glyph.sample.masterdetail.State

fun mainGlyph(): Glyph<ViewGroup, Actions, State, State> = {
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater
        .inflate(R.layout.master_detail_container, parent, false)
        .also(parent::addView)
    val masterView = view.findViewById<ViewGroup>(R.id.master)
    val detailView = view.findViewById<ViewGroup>(R.id.detail)

    +masterView + outputOf { it.persons } + personsGlyph()
    +detailView + outputOf { it.selectedPerson } + personGlyph()

    disposeOf { parent.removeView(view) }
}
