package io.lamart.glyph.sample.masterdetail

data class Actions(private val update: ((State) -> State) -> Unit) {

    fun select(person: Person) =
        update { state ->
            state
                .persons
                .firstOrNull { it.name == person.name }
                .let { state.copy(selectedPerson = it) }
        }

}
