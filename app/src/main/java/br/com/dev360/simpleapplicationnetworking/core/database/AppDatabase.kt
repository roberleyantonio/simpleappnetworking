package br.com.dev360.simpleapplicationnetworking.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.dev360.simpleapplicationnetworking.core.database.dao.CharacterDao
import br.com.dev360.simpleapplicationnetworking.core.database.entity.CharacterEntity

@Database(
    entities = [CharacterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}