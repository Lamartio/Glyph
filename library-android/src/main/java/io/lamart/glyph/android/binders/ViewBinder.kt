package io.lamart.glyph.android.binders

import android.view.View
import io.lamart.glyph.Bind
import io.lamart.glyph.Binder

interface ViewBinder<T> : Binder<T, View>

operator fun <T> View.invoke(bind: Bind<T>): ViewBinder<T> =
    object : ViewBinder<T> {

        override val bind: Bind<T> = bind
        override val owner: View = this@invoke

    }
