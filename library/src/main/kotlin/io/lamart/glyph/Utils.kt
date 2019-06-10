package io.lamart.glyph

import com.badoo.reaktive.observable.*
import com.badoo.reaktive.subject.publish.publishSubject

fun <I, R> Scope<*, *, I, *>.state(transform: (state: I) -> R): Compose<I, R> =
    { input ->
        input
            .map(transform)
            .distinctUntilChanged()
    }

typealias Update<I, O> = ((localState: O, state: I) -> O) -> Unit

private object UnInitialized

fun <D, P, I, O, D_, I_> Scope<D, P, I, O>.localise(
    localState: I_,
    dependenciesOf: (dependencies: D, update: Update<I, I_>) -> D_,
    reduce: (subState: I_, supState: I) -> I_,
    lock: Any = Any()
): Scope<D_, P, I_, I_> = map { dependencies, parent, input, _ ->
    val subject = publishSubject<I_>()
    var state: Any? = UnInitialized
    var localState: I_ = localState
    val input = input
        .scan(localState) { acc: I_, value: I ->
            reduce(acc, value).also {
                synchronized(lock) {
                    state = value
                    localState = it
                }
            }
        }
        .let { merge(it, subject) }
    val dependencies = dependenciesOf(dependencies) { update ->
        synchronized(lock) { state }?.let { state ->

            @Suppress("UNCHECKED_CAST")
            if (state != UnInitialized) {
                val local = synchronized(lock) { localState }
                val next = update(local, state as I)

                synchronized(lock) { localState = next }
                subject.onNext(next)
            }

        }
    }

    Scope(dependencies, parent, input)
}