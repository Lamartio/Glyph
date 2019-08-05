package io.lamart.glyph

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

fun <I, R> Scope<*, *, I, *>.state(transform: (state: I) -> R): Compose<I, R> =
    { input ->
        input
            .map(transform)
            .distinctUntilChanged()
    }

typealias Update<I, O> = ((localState: O, state: I) -> O) -> Unit

private object UnInitialized

fun <A, P, I, O, A_, I_> Scope<A, P, I, O>.localise(
    localState: I_,
    actionsOf: (actions: A, update: Update<I, I_>) -> A_,
    reduce: (subState: I_, supState: I) -> I_,
    lock: Any = Any()
): Scope<A_, P, I_, I_> = map { actions, parent, input, _ ->
    val subject = PublishSubject.create<I_>()
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
        .let { Observable.merge(it, subject) }
    val actions = actionsOf(actions) { update ->
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

    Scope(actions, parent, input)
}