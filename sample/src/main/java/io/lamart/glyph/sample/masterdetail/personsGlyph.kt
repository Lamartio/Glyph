package io.lamart.glyph.sample.masterdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import io.lamart.glyph.Glyph
import io.lamart.glyph.dispose

private const val listItem = android.R.layout.simple_list_item_1

fun <I> personsGlyph(): Glyph<MainActions, ViewGroup, I, List<String>> = { bind ->
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
            itemView.setOnClickListener { dependencies.select(name) }
            view.addView(itemView)
        }
    }

    dispose { parent.removeView(view) }
}