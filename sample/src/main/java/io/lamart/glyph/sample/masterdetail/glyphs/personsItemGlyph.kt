package io.lamart.glyph.sample.masterdetail.glyphs

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import io.lamart.glyph.Glyph
import io.lamart.glyph.disposeOf
import io.lamart.glyph.sample.masterdetail.Actions
import io.lamart.glyph.sample.masterdetail.Person
import io.lamart.glyph.sample.masterdetail.State

fun personsItemGlyph(): Glyph<ViewGroup, Actions, State, Person> = { bind ->
    val inflater = LayoutInflater.from(parent.context)
    val itemView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false) as TextView

    parent.addView(itemView)

    bind { person ->
        itemView.text = person.name
        itemView.setOnClickListener { actions.select(person) }
    }

    disposeOf { parent.removeView(itemView) }
}