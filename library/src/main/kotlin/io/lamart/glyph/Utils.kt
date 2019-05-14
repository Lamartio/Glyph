package io.lamart.glyph

import com.badoo.reaktive.observable.distinctUntilChanged
import com.badoo.reaktive.observable.map

fun <I, R> Scope<*, *, I, *>.state(transform: (state: I) -> R): Compose<I, R> =
    { input ->
        input
            .distinctUntilChanged()
            .map(transform)
            .distinctUntilChanged()
    }
