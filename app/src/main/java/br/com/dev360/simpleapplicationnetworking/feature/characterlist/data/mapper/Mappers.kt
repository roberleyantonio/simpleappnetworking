package br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.mapper

import br.com.dev360.simpleapplicationnetworking.core.database.entity.CharacterEntity
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.model.CharacterDTO
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.model.CharacterListResponse
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.model.InfoDTO
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.model.Character
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.model.CharacterList
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.model.Info

fun CharacterDTO.toDomain() = Character(
    id = id ?: 0,
    name = name ?: "Unknown",
    status = status ?: "Unknown",
    species = species ?: "Unknown",
    imageUrl = imageUrl ?: ""
)

fun InfoDTO.toDomain(): Info = Info(
    count = count ?: 0,
    pages = pages ?: 1,
)

fun CharacterListResponse.toDomain(): CharacterList = CharacterList(
    info = info?.toDomain() ?: Info(0, 1),
    results = results?.map { it.toDomain() } ?: emptyList()
)

fun CharacterEntity.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        imageUrl = imageUrl
    )
}

fun CharacterDTO.toEntity() : CharacterEntity = CharacterEntity(
    id = id ?: 0,
    name = name.orEmpty(),
    status = status.orEmpty(),
    species = species.orEmpty(),
    imageUrl = imageUrl.orEmpty(),
)