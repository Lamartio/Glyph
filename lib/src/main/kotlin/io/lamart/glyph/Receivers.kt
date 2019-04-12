package io.lamart.glyph


/**
 * Aggregates all the receives that subscribe to this Sender. It reduces the receivers into the `all` property. By calling the `all` property, all the receivers will receive the argument.
 */

class Receivers<T> : Sender<T>, Disposable {

    var all: Receiver<T> = {}
        private set
    private val subscriptions = mutableListOf<Subscription>()

    override fun invoke(receiver: Receiver<T>): Dispose =
        Subscription(receiver)
            .also {
                subscriptions += it
                invalidateReceiver()
            }
            .toDispose()

    override fun dispose() {
        all = {}
        subscriptions.removeAll { subscription -> subscription.dispose();true }
    }

    private fun invalidateReceiver() =
        subscriptions
            .asSequence()
            .map { it.receiver }
            .reduce { l, r -> { l(it);r(it) } }
            .let { all = it }

    private inner class Subscription(val receiver: Receiver<T>) : Disposable {

        override fun dispose() {
            subscriptions.remove(this)
            invalidateReceiver()
        }

    }

}