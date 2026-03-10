package br.com.dev360.simpleapplicationnetworking.feature.characterlist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.dev360.simpleapplicationnetworking.core.shared.exception.Failure
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.CharacterListUseCase
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.domain.model.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CharactersUiState(
    val characters: List<Character> = emptyList(),
    val isLoading: Boolean = false,
    val isPaginatedLoading: Boolean = false,
    val error: Failure? = null
) {
    val isFirstLoad: Boolean get() = characters.isEmpty()
}

@Suppress("UNCHECKED_CAST")
class CharacterListViewModel(
    private val useCase: CharacterListUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState = _uiState.asStateFlow()

    private var currentPage = 1
    private var totalPages = Int.MAX_VALUE
    private var canPaginate = true

    init {
        loadCharacters()
    }


    fun loadCharacters() {
        if (!canPaginate || _uiState.value.isLoading || _uiState.value.isPaginatedLoading) return

        _uiState.update { state ->
            if (state.isFirstLoad) {
                state.copy(isLoading = true, error = null)
            } else {
                state.copy(isPaginatedLoading = true, error = null)
            }
        }

        viewModelScope.launch {
            useCase.getCharacters(currentPage).onSuccess { result ->
                totalPages = result.info.pages
                _uiState.update { state ->
                    state.copy(
                        characters = state.characters + result.results,
                        isLoading = false,
                        isPaginatedLoading = false
                    )
                }
                currentPage++
                canPaginate = currentPage <= totalPages
            }.onFailure {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        isPaginatedLoading = false,
                        error = Failure.fromException(it)
                    )
                }
            }
        }
    }


    companion object Companion {
        fun provideFactory(useCase: CharacterListUseCase) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(CharacterListViewModel::class.java)) {
                    return CharacterListViewModel(useCase) as T
                }
                throw IllegalArgumentException()
            }
        }
    }
}