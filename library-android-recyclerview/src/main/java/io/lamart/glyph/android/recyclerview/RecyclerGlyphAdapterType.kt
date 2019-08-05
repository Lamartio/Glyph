package io.lamart.glyph.android.recyclerview

import android.view.ViewGroup
import io.lamart.glyph.Compose
import io.lamart.glyph.Dispose
import io.lamart.glyph.Scope

interface RecyclerGlyphAdapterType<D, I, O> {

    fun onCreateScope(parent: ViewGroup, compose: Compose<I, O>): Scope<D, ViewGroup, I, O>

    fun onCreateGlyph(
        viewType: Int,
        create: (frame: ViewGroup, build: Scope<D, ViewGroup, I, O>.() -> Dispose) -> Unit
    )

}