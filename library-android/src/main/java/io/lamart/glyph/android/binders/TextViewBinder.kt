package io.lamart.glyph.android.binders

import android.widget.TextView
import io.lamart.glyph.Bind

interface TextViewBinder<T> : ViewBinder<T> {

    override val owner: TextView

}

operator fun <T> Bind<T>.invoke(view: TextView): TextViewBinder<T> =
    object : TextViewBinder<T> {

        override val bind: Bind<T> = this@invoke
        override val owner: TextView = view

    }

infix fun <T> TextViewBinder<T>.text(block: T.(state: T) -> CharSequence) =
    bind.invoke {
        owner.text = block(it, it)
    }
