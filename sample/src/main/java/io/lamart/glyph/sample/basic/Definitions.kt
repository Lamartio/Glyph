package io.lamart.glyph.sample.basic

import android.view.ViewGroup
import io.lamart.glyph.Bind
import io.lamart.glyph.Dispose
import io.lamart.glyph.Glyph
import io.lamart.glyph.Scope
import io.lamart.glyph.docs.Actions
import io.lamart.glyph.docs.SampleScope
import io.lamart.glyph.docs.State
import io.reactivex.subjects.BehaviorSubject

typealias SampleScope<T> = Scope<Actions, ViewGroup, State, T>
typealias SampleGlyph<T> = Glyph<Actions, ViewGroup, State, T>

fun <T> sampleGlyph(block: SampleScope<T>.(bind: Bind<T>) -> Dispose): SampleGlyph<T> = block

val SampleScope<*>.context
    get() = parent.context

data class State(val count: Int = 0)

class Actions(private val subject: BehaviorSubject<State>) {

    fun increment() =
        update { state -> state.copy(count = state.count + 1) }

    fun decrement() =
        update { state -> state.copy(count = state.count - 1) }

    private fun update(block: (state: State) -> State) {
        subject.value?.let { previous ->
            previous
                .let(block)
                .takeIf { it != previous }
                ?.let(subject::onNext)
        }
    }

}
