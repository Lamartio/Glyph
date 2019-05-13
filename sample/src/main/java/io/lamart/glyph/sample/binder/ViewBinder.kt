package io.lamart.glyph.sample.binder

import android.view.View
import io.lamart.glyph.Bind
import io.lamart.glyph.Binder

interface ViewBinder<T> : Binder<T, View>

operator fun <T> Bind<T>.invoke(view: View): ViewBinder<T> =
    object : ViewBinder<T> {

        override val bind: Bind<T> = this@invoke
        override val owner: View = view

    }
