package io.lamart.glyph

typealias Receiver<T> = (item: T) -> Unit
typealias Composer<T, R> = (Sender<T>) -> Sender<R>

interface Sender<T> {

    operator fun invoke(receiver: Receiver<T>): Dispose

    companion object {

        /**
         * General constructor for a Sender
         */

        operator fun <T> invoke(block: (Receiver<T>) -> Dispose): Sender<T> =
            object : Sender<T> {

                override fun invoke(receiver: Receiver<T>): Dispose = block(receiver)

            }

        /**
         * General constructor for a Sender
         */

        operator fun <T> invoke(vararg items: T): Sender<T> =
            invoke { receiver ->
                items.forEach(receiver)
                dispose
            }

    }

    /**
     * Composing is comparable to mapping, but the argument receives the stream instead of just the emission. This grants the possibility to convert both the emissions and the stream.
     */

    fun <R> compose(composer: Composer<T, R>): Sender<R> =
        Sender { receiver ->
            composer
                .invoke(this)
                .invoke(receiver)
        }

    /**
     * Maps each emission to another type based on the given argument.
     */

    fun <R> map(transform: (T) -> R): Sender<R> =
        wrap { item, receiver -> transform(item).let(receiver) }

    /**
     * Maps each emission to another type based on the given argument.
     */

    fun <R> flatMap(transform: (T) -> Iterable<R>): Sender<R> =
        wrap { item, receiver -> transform(item).forEach(receiver) }

    /**
     * Filters emissions based on the given predicate.
     */

    fun filter(predicate: (T) -> Boolean): Sender<T> =
        wrap { item, receiver -> item.takeIf(predicate)?.let(receiver) }

    @Suppress("UNCHECKED_CAST")
    fun <R> cast(): Sender<R> =
        wrap { item, receiver -> item.let { it as R }.let(receiver) }

    /**
     * Filters the emissions that are the same as the previous. What is meaned by same is evaluated in the argument.
     */

    fun distinct(comparator: (T, T) -> Boolean = { l, r -> l !== r }): Sender<T> =
        Sender { receiver ->
            var getter: (() -> T)? = null

            invoke { item ->
                getter
                    .also { getter = { item } }
                    .let { getter ->
                        val state = getter?.invoke()

                        when {
                            state == null -> item
                            comparator(state, state) -> item
                            else -> null
                        }
                    }
                    ?.let(receiver)
            }
        }

    /**
     * Whenever a `Receiver` is subscribed, it directly receives the given argument.
     */

    fun prepend(value: T): Sender<T> =
        Sender { receiver ->
            receiver(value)

            invoke { item ->
                receiver(item)
            }
        }

    fun prepend(get: () -> T): Sender<T> =
        Sender { receiver ->
            receiver(get())

            invoke { item ->
                receiver(item)
            }
        }

    /**
     * After each emission the argument gets called with the value that was emitted.
     */

    fun before(block: (T) -> Unit): Sender<T> =
        wrap { item, receiver -> item.also(block).let(receiver) }

    /**
     * Before each emission the  gets called with the value that will be emitted.
     */

    fun after(block: (T) -> Unit): Sender<T> =
        wrap { item, receiver -> item.also(receiver).let(block) }

    /**
     * An utililty function that helps creating custom operators. The argument receives the item that needs to be emitted and the receiver where it needs to be emitted to.
     */

    fun <R> wrap(block: (item: T, receiver: Receiver<R>) -> Unit): Sender<R> =
        Sender { receiver ->
            invoke { item ->
                block(item, receiver)
            }
        }

    /**
     * Returns the last two emissions. Good for comparing.
     */

    fun record(): Sender<Pair<T, T>> = record(null)

    /**
     * Returns the last two emissions. Good for comparing.
     */

    fun record(seed: T): Sender<Pair<T, T>> = record { seed }

    private fun record(get: (() -> T)?): Sender<Pair<T, T>> =
        Sender { receiver ->
            var getter = get

            invoke { next ->
                getter?.invoke()?.let { previous ->
                    receiver(previous to next)
                }

                getter = { next }
            }
        }

    /**
     * Collects the `size` amount of emissions, bundles them into a list and emits it. When allowPartial is `true`, it will emit also when the bundle is smaller thatn the `size`.
     */

    fun window(size: Int, seed: List<T> = emptyList(), allowPartial: Boolean = false): Sender<List<T>> =
        Sender { receiver ->
            val items = seed.toMutableList()

            invoke { item ->
                items.add(item)

                if (items.size > size)
                    items.subList(0, size - items.size).clear()

                if (allowPartial || items.size == size)
                    receiver.invoke(items)
            }
        }

    /**
     * Combines two streams into one. The `transform` argument is called whenever there is an item available of both streams and emit the result to the emit operator.
     */

    fun <R, N> combine(right: Sender<R>, transform: (left: T, right: R) -> N): Sender<N> =
        let { left ->
            Sender { receiver ->
                var getLeft: (() -> T)? = null
                var getRight: (() -> R)? = null

                dispose(
                    left { l ->
                        getLeft = { l }
                        getRight
                            ?.invoke()
                            ?.let { r -> transform(l, r) }
                            ?.let(receiver)
                    },
                    right { r ->
                        getRight = { r }
                        getLeft
                            ?.invoke()
                            ?.let { l -> transform(l, r) }
                            ?.let(receiver)
                    }
                )

            }
        }

    fun first(): Sender<T> = take(1)
    fun take(count: Int): Sender<T> {
        return Sender { receiver ->
            var counter = count

            invoke { item ->
                if (counter > 0) {
                    receiver(item)
                    counter--
                }
            }
        }
    }

}