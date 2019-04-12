package io.lamart.glyph

interface Emitter<T> : Sender<T>, Disposable {

    fun emit(item: T)

    companion object {

        operator fun <T> invoke(composer: Composer<T, T> = { it }): Emitter<T> = Instance(composer)

        fun <T> prependLatest(seed: T): Emitter<T> {
            var value = seed

            return invoke { sender ->
                sender
                    .before { value = it }
                    .prepend { value }
            }

        }

    }

    class Instance<T>(
        private val composer: Composer<T, T> = { it },
        private val receivers: Receivers<T> = Receivers()
    ) :
        Emitter<T>,
        Sender<T> by receivers,
        Disposable by receivers {

        override fun emit(item: T) {
            Sender(item).let(composer).invoke(receivers.all)
        }

    }

}

fun <T> Emitter<T>.toReceiver(): Receiver<T> = this@toReceiver::emit

