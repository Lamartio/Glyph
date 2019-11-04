package io.lamart.glyph.sample.masterdetail.glyphs

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import io.lamart.glyph.Glyph
import io.lamart.glyph.disposeOf
import io.lamart.glyph.sample.masterdetail.Actions
import io.lamart.glyph.sample.masterdetail.State

private const val listItem = android.R.layout.simple_list_item_1

fun personsGlyph(): Glyph<ViewGroup, Actions, State, List<String>> = { bind ->
    val inflater = LayoutInflater.from(parent.context)
    val view = LinearLayout(parent.context)
        .also(parent::addView)
        .apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundResource(android.R.color.darker_gray)
        }

    bind { names ->
        view.removeAllViews()
        names.forEach { name ->
            val itemView = inflater.inflate(listItem, parent, false) as TextView

            itemView.text = name
            itemView.setOnClickListener { actions.select(name) }
            view.addView(itemView)
        }
    }

    disposeOf { parent.removeView(view) }
}