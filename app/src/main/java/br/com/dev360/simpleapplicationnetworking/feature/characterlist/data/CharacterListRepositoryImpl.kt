package br.com.dev360.simpleapplicationnetworking.feature.characterlist.data

import br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.local.CharacterLocalDataSource
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.mapper.toDomain
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.mapper.toEntity
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.remote.CharacterRemoteDataSource
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.CharacterListRepository
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.model.CharacterList
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.model.Info

class CharacterListRepositoryImpl(
    private val localDataSource: CharacterLocalDataSource,
    private val remoteDataSource: CharacterRemoteDataSource
): CharacterListRepository {

    override suspend fun getCharacters(page: Int?): Result<CharacterList> {
        return runCatching {
            val pageNumber = page ?: 1
            val offset = (pageNumber - 1) * PAGE_SIZE

            val totalCount = localDataSource.getTotalCount()
            val savedPages = if (totalCount == 0) 0 else ((totalCount + PAGE_SIZE - 1) / PAGE_SIZE)

            val cached = localDataSource.getCharacters(
                limit = PAGE_SIZE,
                offset = offset
            )

            if (cached.isNotEmpty() && cached.size == PAGE_SIZE || pageNumber == savedPages) {
                return@runCatching CharacterList(
                    info = Info(
                        count = totalCount,
                        pages = savedPages
                    ),
                    results = cached.map { it.toDomain() }
                )
            }

            val response = remoteDataSource.getCharacters(pageNumber)

            response.results?.let { results ->
                localDataSource.saveCharacters(
                    results.map { it.toEntity() }
                )
            }


            val updated = localDataSource.getCharacters(
                limit = PAGE_SIZE,
                offset = offset
            )

            CharacterList(
                info = response.info?.toDomain() ?: Info(count = totalCount, pages = savedPages),
                results = updated.map { it.toDomain() }
            )
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}