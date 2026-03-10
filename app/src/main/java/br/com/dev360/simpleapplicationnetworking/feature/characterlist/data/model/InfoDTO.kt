package br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InfoDTO(
    val count: Int? = null,
    val pages: Int? = null
)
