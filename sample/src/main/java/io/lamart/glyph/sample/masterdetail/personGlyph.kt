package io.lamart.glyph.sample.masterdetail

import android.view.ViewGroup
import android.widget.TextView
import io.lamart.glyph.Glyph
import io.lamart.glyph.android.binders.invoke
import io.lamart.glyph.android.binders.text
import io.lamart.glyph.dispose
import java.util.*

typealias Option<T> = () -> T?

fun <T> T.toOption(): Option<T> = { this }

fun <I> personGlyph(): Glyph<MainActions, ViewGroup, I, Option<Person>> = { bind ->
    val view = TextView(parent.context).also(parent::addView)

    view(bind).text { this()?.name }

    dispose { parent.removeView(view) }
}