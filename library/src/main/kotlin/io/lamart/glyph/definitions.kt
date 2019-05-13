package io.lamart.glyph

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.distinctUntilChanged
import com.badoo.reaktive.observable.map

typealias Bind<T> = (next: (model: T) -> Unit) -> Unit
typealias Compose<T, R> = (Observable<T>) -> Observable<R>

fun <I, R> Scope<*, *, I, *>.state(transform: (state: I) -> R): Compose<I, R> =
    { input ->
        input
            .distinctUntilChanged()
            .map(transform)
            .distinctUntilChanged()
    }
