package br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.local

import br.com.dev360.simpleapplicationnetworking.core.database.dao.CharacterDao
import br.com.dev360.simpleapplicationnetworking.core.database.entity.CharacterEntity

class CharacterLocalDataSource(
    private val dao: CharacterDao
) {
    suspend fun getCharacters(limit: Int, offset: Int) =
        dao.getCharacters(limit, offset)

    suspend fun saveCharacters(characters: List<CharacterEntity>) =
        dao.insertAll(characters)

    suspend fun getTotalCount() = dao.getTotalCount()
}