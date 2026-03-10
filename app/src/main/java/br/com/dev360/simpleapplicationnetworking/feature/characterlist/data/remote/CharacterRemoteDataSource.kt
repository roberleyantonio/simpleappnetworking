package br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.remote

import br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.api.RickMortyApi
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.model.CharacterListResponse

class CharacterRemoteDataSource(
    private val api: RickMortyApi
) {
    suspend fun getCharacters(page: Int?): CharacterListResponse = api.getCharacters(page)
}