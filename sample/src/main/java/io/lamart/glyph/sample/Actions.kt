package io.lamart.glyph.sample

import com.badoo.reaktive.subject.behavior.BehaviorSubject

class Actions(private val subject: BehaviorSubject<State>) {

    fun increment() =
        update { state -> state.copy(count = state.count + 1) }

    fun decrement() =
        update { state -> state.copy(count = state.count - 1) }

    private fun update(block: (state: State) -> State) {
        val previous = subject.value
        val next = previous.run(block)

        if (previous != next)
            subject.onNext(next)
    }

}