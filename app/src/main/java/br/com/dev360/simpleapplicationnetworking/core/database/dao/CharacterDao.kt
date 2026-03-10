package br.com.dev360.simpleapplicationnetworking.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.dev360.simpleapplicationnetworking.core.database.entity.CharacterEntity

@Dao
interface CharacterDao {

    @Query("""
        SELECT * FROM characters
        ORDER BY id
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getCharacters(
        limit: Int,
        offset: Int
    ): List<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)


    @Query("SELECT COUNT(*) FROM characters")
    suspend fun getTotalCount(): Int
}