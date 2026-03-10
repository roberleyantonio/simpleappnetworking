package br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharacterDTO(
    val id: Int? = null,
    val name: String? = null,
    val status: String? = null,
    val species: String? = null,
    @field:Json(name = "image") val imageUrl: String? = null
)
