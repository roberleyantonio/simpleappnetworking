package br.com.dev360.simpleapplicationnetworking.feature.characterlist.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.dev360.simpleapplicationnetworking.R
import br.com.dev360.simpleapplicationnetworking.app.SimpleApplication
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.presentation.components.CharacterListContent
import kotlinx.coroutines.flow.distinctUntilChanged

private const val PAGINATION_THRESHOLD = 5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(

) {
    val viewModel: CharacterListViewModel = viewModel(
        factory = CharacterListViewModel.provideFactory(
            (LocalContext.current.applicationContext as SimpleApplication).container.characterListUseCase
        )
    )

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = listState.layoutInfo.totalItemsCount
            lastVisible >= total - PAGINATION_THRESHOLD
        }.distinctUntilChanged()
            .collect { shouldLoad ->
                if (shouldLoad) viewModel.loadCharacters()
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.characters_screen_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        CharacterListContent(
            state = state,
            listState = listState,
            modifier = Modifier.padding(paddingValues),
            onLoadMore = { viewModel.loadCharacters() }
        )
    }
}