package br.com.dev360.simpleapplicationnetworking.feature.characterlist.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.dev360.simpleapplicationnetworking.R
import br.com.dev360.simpleapplicationnetworking.core.shared.exception.Failure
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.presentation.CharactersUiState

@Composable
fun CharacterListContent(
    state: CharactersUiState,
    listState: LazyListState,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.isLoading && state.isFirstLoad) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = state.characters,
                key = { character -> character.id }
            ) { character ->
                CharacterCard(character = character)
            }

            if (state.isPaginatedLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            strokeWidth = 3.dp
                        )
                    }
                }
            }

            state.error?.let { failure ->
                val resId = when (failure) {
                    is Failure.NetworkConnection -> R.string.error_network
                    else -> R.string.error_generic
                }
                item {
                    ErrorItem(
                        message = stringResource(id = resId),
                        onRetry = onLoadMore
                    )
                }
            }
        }
    }
}