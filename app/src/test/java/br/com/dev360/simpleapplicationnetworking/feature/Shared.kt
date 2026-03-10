package br.com.dev360.simpleapplicationnetworking.feature

import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.model.Character
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.model.CharacterList
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.model.Info

val fakeCharacter = Character(
    id = 1,
    name = "Rick",
    status = "Alive",
    species = "Human",
    imageUrl = "url"
)
val fakeInfo = Info(count = 40, pages = 2)
val fakeCharacterList = CharacterList(info = fakeInfo, results = listOf(fakeCharacter))

const val RICK = "Rick"
const val MORTY = "Morty"