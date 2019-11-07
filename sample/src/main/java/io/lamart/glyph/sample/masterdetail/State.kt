package io.lamart.glyph.sample.masterdetail

data class Person(val name: String, val age: Int)

data class State(
    val persons: List<Person> = (1..100).map { Person("Danny $it", it + 20) },
    val selectedPerson: Person? = null
)
