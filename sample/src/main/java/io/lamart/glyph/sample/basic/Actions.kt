package io.lamart.glyph.sample.basic

import kotlinx.coroutines.channels.ConflatedBroadcastChannel

class Actions(private val channel: ConflatedBroadcastChannel<State>) {

    fun increment() =
        update { state -> state.copy(count = state.count + 1) }

    fun decrement() =
        update { state -> state.copy(count = state.count - 1) }

    private fun update(block: (state: State) -> State) {
        channel.value?.let { previous ->
            previous
                .let(block)
                .takeIf { it != previous }
                ?.let { channel.offer(it) }
        }
    }

}