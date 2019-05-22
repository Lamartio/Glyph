package io.lamart.glyph.android.binders

import android.widget.Button
import io.lamart.glyph.Bind

interface ButtonBinder<T> : TextViewBinder<T> {

    override val owner: Button

}

operator fun <T> Button.invoke(bind: Bind<T>): ButtonBinder<T> =
    object : ButtonBinder<T> {

        override val bind: Bind<T> = bind
        override val owner: Button = this@invoke

    }
