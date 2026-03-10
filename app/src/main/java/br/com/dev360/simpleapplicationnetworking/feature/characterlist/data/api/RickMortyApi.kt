package br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.api

import br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.model.CharacterListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RickMortyApi {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int? = null): CharacterListResponse
}