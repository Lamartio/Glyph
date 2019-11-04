package io.lamart.glyph.sample.masterdetail

data class Actions(private val update: ((State) -> State) -> Unit) {

    fun select(personName: String) =
        update { state ->
            val index = state.persons.map { it.name }.indexOf(personName)
            val person = state.persons.getOrNull(index)

            state.copy(selectedPerson = person)
        }

}
