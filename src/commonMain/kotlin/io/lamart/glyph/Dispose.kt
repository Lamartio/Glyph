package io.lamart.glyph

typealias Dispose = () -> Unit

val dispose: Dispose = {}
fun dispose(value: Dispose): Dispose = value
fun dispose(vararg values: Dispose): Dispose = values.reduce { l, r -> { l(); r() } }

interface Disposable {

    fun dispose()

    fun toDispose() : Dispose = ::dispose
}

class DisposableCollection(private val collection: MutableCollection<Dispose> = mutableListOf()) :
    MutableCollection<Dispose> by collection, Disposable {

    override fun dispose() {
        collection.removeAll { dispose -> dispose();true }
    }

}