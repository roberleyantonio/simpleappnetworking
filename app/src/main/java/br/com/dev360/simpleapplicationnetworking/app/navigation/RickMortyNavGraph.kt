package br.com.dev360.simpleapplicationnetworking.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.presentation.CharacterListScreen

@Composable
fun RickMortyNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "character_list"
    ) {
        composable("character_list") {
            CharacterListScreen()
        }
    }
}