package io.lamart.glyph

typealias Receiver<T> = (item: T) -> Unit
typealias Sender<T> = (receiver: Receiver<T>) -> Dispose
typealias Composer<T, R> = (Sender<T>) -> Sender<R>

/**
 * Composing is comparable to mapping, but the argument receives the stream instead of just the emission. This grants the possibility to convert both the emissions and the stream.
 */

fun <T, R> Sender<T>.compose(composer: Composer<T, R>): Sender<R> =
    { receiver ->
        composer
            .invoke(this@compose)
            .invoke(receiver)
    }

/**
 * Maps each emission to another type based on the given argument.
 */

fun <T, R> Sender<T>.map(transform: (T) -> R): Sender<R> =
    wrap { item, receiver -> transform(item).let(receiver) }

/**
 * Maps each emission to another type based on the given argument.
 */

fun <T, R> Sender<T>.flatMap(transform: (T) -> List<R>): Sender<R> =
    wrap { item, receiver -> transform(item).forEach(receiver) }

/**
 * Filters emissions based on the given predicate.
 */

fun <T> Sender<T>.filter(predicate: (T) -> Boolean): Sender<T> =
    wrap { item, receiver -> item.takeIf(predicate)?.let(receiver) }

@Suppress("UNCHECKED_CAST")
fun <T, R> Sender<T>.cast(): Sender<R> =
    wrap { item, receiver -> item.let { it as R }.let(receiver) }

/**
 * Filters the emissions that are the same as the previous. What is meaned by same is evaluated in the argument.
 */

fun <T> Sender<T>.distinct(comparator: (T, T) -> Boolean = { l, r -> l !== r }): Sender<T> {
    var getter: (() -> T)? = null

    return wrap { item, receiver ->
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

fun <T> Sender<T>.prepend(value: T): Sender<T> = prepend { value }

/**
 * Whenever a `Receiver` is subscribed, it directly receives the given argument.
 */

fun <T> Sender<T>.prepend(get: () -> T): Sender<T> = { receiver ->
    receiver
        .apply { invoke(get()) }
        .let { this@prepend(it) }
}

/**
 * After each emission the argument gets called with the value that was emitted.
 */

fun <T> Sender<T>.before(block: (T) -> Unit): Sender<T> =
    wrap { item, receiver -> item.also(block).let(receiver) }

/**
 * Before each emission the  gets called with the value that will be emitted.
 */

fun <T> Sender<T>.after(block: (T) -> Unit): Sender<T> =
    wrap { item, receiver -> item.also(receiver).let(block) }

/**
 * An utililty function that helps creating custom operators. The argument receives the item that needs to be emitted and the receiver where it needs to be emitted to.
 */

fun <T, R> Sender<T>.wrap(block: (item: T, receiver: Receiver<R>) -> Unit): Sender<R> =
    { receiver ->
        this@wrap.invoke { item ->
            block(item, receiver)
        }
    }

/**
 * Combines two streams into one. The `transform` argument is called whenever there is an item available of both streams and emit the result to the next operator.
 */

fun <L, R, N> Sender<L>.combine(right: Sender<R>, transform: (left: L, right: R) -> N): Sender<N> =
    let { left ->
        { receiver ->
            var getLeft: (() -> L)? = null
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
