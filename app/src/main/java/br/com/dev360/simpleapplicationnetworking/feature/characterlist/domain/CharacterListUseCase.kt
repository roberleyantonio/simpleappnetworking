package br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain

import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.model.CharacterList

interface CharacterListUseCase {
    suspend fun getCharacters(page: Int? = null): Result<CharacterList>
}

class CharacterListUseCaseImpl(
    private val repository: CharacterListRepository
) : CharacterListUseCase {

    override suspend fun getCharacters(page: Int?): Result<CharacterList> = repository.getCharacters(page)

}