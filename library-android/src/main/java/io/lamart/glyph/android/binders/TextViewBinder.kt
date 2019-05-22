package io.lamart.glyph.android.binders

import android.widget.TextView
import io.lamart.glyph.Bind

interface TextViewBinder<T> : ViewBinder<T> {

    override val owner: TextView

}

operator fun <T> TextView.invoke(bind: Bind<T>): TextViewBinder<T> =
    object : TextViewBinder<T> {

        override val bind: Bind<T> = bind
        override val owner: TextView = this@invoke

    }

infix fun <T> TextViewBinder<T>.text(block: T.() -> CharSequence) =
    bind.invoke {
        owner.text = block(it)
    }
