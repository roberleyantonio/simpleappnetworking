package br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharacterListResponse(
    val info: InfoDTO? = null, // todo @field:Json(name = "info") é desnecessário
    val results: List<CharacterDTO>? = null
)