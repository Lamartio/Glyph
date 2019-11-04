package io.lamart.glyph.sample.masterdetail

data class Person(val name: String, val age: Int)

data class State(
    val persons: List<Person> = listOf(
        Person("Danny", 28),
        Person("Diede", 26)
    ),
    val selectedPerson: Person? = null
)
