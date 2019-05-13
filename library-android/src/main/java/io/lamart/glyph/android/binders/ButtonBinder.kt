package io.lamart.glyph.android.binders

import android.widget.Button
import io.lamart.glyph.Bind

interface ButtonBinder<T> : TextViewBinder<T> {

    override val owner: Button

}

operator fun <T> Bind<T>.invoke(view: Button): ButtonBinder<T> =
    object : ButtonBinder<T> {

        override val bind: Bind<T> = this@invoke
        override val owner: Button = view

    }
