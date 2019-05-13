package io.lamart.glyph

import com.badoo.reaktive.observable.Observable

typealias Bind<T> = (next: (model: T) -> Unit) -> Unit
typealias ObservableCompose<T, R> = (Observable<T>) -> Observable<R>
