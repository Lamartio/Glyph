package io.lamart.glyph.test

import io.lamart.glyph.Emitter
import kotlin.test.Test
import kotlin.test.assertEquals


class EmitterTests {

    @Test
    fun prependLatest() {
        val emitter = Emitter.prependLatest(1)

        emitter.first().invoke { assertEquals(1, it) }
        emitter.first().invoke { assertEquals(1, it) }
        emitter.emit(2)
        emitter.first().invoke { assertEquals(2, it) }
        emitter.first().invoke { assertEquals(2, it) }
    }

}
