package com.example.app3.navigation.child

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.app3.database.AppDatabase
import com.example.app3.database.MainRepository
import com.example.app3.navigation.Screens
import com.example.app3.ui.screen1.FirstScreen
import com.example.app3.ui.screen1.FirstViewModel
import com.example.app3.ui.screen1.FirstViewModelFactory

fun NavGraphBuilder.firstNavGraph(navController: NavHostController) {
    composable(route = Screens.FirstScreen.route) {

        val context = LocalContext.current

        val viewModel = viewModel<FirstViewModel>(
            factory = FirstViewModelFactory(
                MainRepository(
                    AppDatabase.getInstance(context)
                )
            )
        )

        FirstScreen(
            viewModel = viewModel,
            openSecondScreen = {
                navController.navigate(Screens.SecondScreen.route)
            }
        )
    }
}