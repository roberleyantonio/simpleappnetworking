package br.com.dev360.simpleapplicationnetworking.feature.characterlist.di

import android.content.Context
import br.com.dev360.simpleapplicationnetworking.core.database.DatabaseProvider
import br.com.dev360.simpleapplicationnetworking.core.network.NetworkProvider
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.CharacterListRepositoryImpl
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.api.RickMortyApi
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.local.CharacterLocalDataSource
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.data.remote.CharacterRemoteDataSource
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.CharacterListRepository
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.CharacterListUseCase
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.CharacterListUseCaseImpl

interface AppContainer {
    val characterListUseCase: CharacterListUseCase
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val baseUrl = "https://rickandmortyapi.com/api/"

    private val networkProvider = NetworkProvider(baseUrl = baseUrl)

    private val database by lazy {
        DatabaseProvider.getDatabase(context)
    }

    private val rickMortyApi: RickMortyApi by lazy {
        networkProvider.createService(RickMortyApi::class.java)
    }

    private val characterLocalDataSource by lazy {
        CharacterLocalDataSource(database.characterDao())
    }

    private val characterRemoteDataSource by lazy {
        CharacterRemoteDataSource(rickMortyApi)
    }

    private val characterListRepository: CharacterListRepository by lazy {
        CharacterListRepositoryImpl(localDataSource = characterLocalDataSource, remoteDataSource = characterRemoteDataSource)
    }

    override val characterListUseCase: CharacterListUseCase by lazy {
        CharacterListUseCaseImpl(repository = characterListRepository)
    }

}