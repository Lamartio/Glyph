package io.lamart.glyph


interface EmitterSource<T> : Sender<T>, Disposable {

    fun next(item: T)

}

class Emitter<T>(private val composer: Composer<T, T> = { it }) : EmitterSource<T> {

    private var receiver: Receiver<T> = {}
    private val subscriptions = mutableListOf<Subscription>()
    private val sender: Sender<T> = Sender { receiver ->
        Subscription(receiver)
            .also { subscriptions += it }
            .also { invalidateReceiver() }
            .toDispose()
    }

    override fun next(item: T) = receiver(item)

    override fun invoke(receiver: Receiver<T>): Dispose =
        sender
            .compose(composer)
            .invoke(receiver)

    override fun dispose() {
        subscriptions.clear()
        receiver = {}
    }

    private fun invalidateReceiver() =
        subscriptions
            .asSequence()
            .map { it.receiver }
            .reduce { l, r -> { l(it);r(it) } }
            .let { receiver = it }

    private inner class Subscription(val receiver: Receiver<T>) : Disposable {

        override fun dispose() {
            subscriptions.remove(this)
            invalidateReceiver()
        }

    }

    companion object {

        fun <T> prependLatest(seed: T): Emitter<T> {
            var value = seed

            return Emitter { sender ->
                sender
                    .before { value = it }
                    .prepend { value }
            }
        }

    }

}
