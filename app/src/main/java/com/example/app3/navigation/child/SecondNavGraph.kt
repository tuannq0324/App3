package com.example.app3.navigation.child

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.app3.database.AppDatabase
import com.example.app3.database.MainRepository
import com.example.app3.navigation.Screens
import com.example.app3.ui.screen2.SecondScreen
import com.example.app3.ui.screen2.SecondViewModel
import com.example.app3.ui.screen2.SecondViewModelFactory

fun NavGraphBuilder.secondNavGraph(navController: NavHostController) {
    composable(route = Screens.SecondScreen.route) {

        val context = LocalContext.current

        val viewModel = viewModel<SecondViewModel>(
            factory = SecondViewModelFactory(
                MainRepository(
                    AppDatabase.getInstance(context)
                )
            )
        )

        SecondScreen(
            viewModel = viewModel,
            onBack = {
                navController.popBackStack()
            },
        )
    }
}