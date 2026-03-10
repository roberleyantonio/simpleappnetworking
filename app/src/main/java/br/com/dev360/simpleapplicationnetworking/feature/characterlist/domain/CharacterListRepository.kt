package br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain

import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.model.CharacterList

interface CharacterListRepository {
    suspend fun getCharacters(page: Int? = null): Result<CharacterList>
}