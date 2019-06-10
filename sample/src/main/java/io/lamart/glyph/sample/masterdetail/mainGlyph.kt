package io.lamart.glyph.sample.masterdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import io.lamart.glyph.*
import io.lamart.glyph.sample.R

data class MainActions(
    private val actions: Actions,
    private val update: Update<State, MainState>
) {

    fun select(personName: String) =
        update { localState, state ->
            val index = state.persons.map { it.name }.indexOf(personName)
            val person = state.persons.getOrNull(index)

            localState.copy(person = person)
        }

}

data class MainState(
    val names: List<String> = emptyList(),
    val person: Person? = null
)

fun Scope<Actions, ViewGroup, State, State>.toMainScope() =
    localise(
        MainState(),
        ::MainActions,
        { subState, supState ->
            subState.copy(
                names = supState.persons.map { it.name },
                person = subState.person?.takeIf(supState.persons::contains)
            )
        }
    )


fun mainGlyph(): Glyph<MainActions, ViewGroup, MainState, MainState> = {
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater
        .inflate(R.layout.master_detail_container, parent, false)
        .also(parent::addView)
    val masterView = view.findViewById<ViewGroup>(R.id.master)
    val detailView = view.findViewById<ViewGroup>(R.id.detail)

    +masterView + state { it.names } + personsGlyph()
    +detailView + state { it.person } + personGlyph()

    dispose { parent.removeView(view) }
}