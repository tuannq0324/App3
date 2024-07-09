package com.example.app3.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.app3.navigation.child.firstNavGraph
import com.example.app3.navigation.child.secondNavGraph

sealed class Screens(val route: String) {
    data object App: Screens("app_route")

    data object FirstScreen : Screens("first_route")
    data object SecondScreen : Screens("second_route")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    AppNavHost(
        navController = navController,
        startDestination = Screens.FirstScreen.route
    ) {

        firstNavGraph(navController = navController)

        secondNavGraph(navController = navController)

    }
}

@Composable
fun AppNavHost(
    navController: NavHostController, startDestination: String, builder: NavGraphBuilder.() -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = Screens.App.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(350)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(350)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(350)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(350)
            )
        },
        builder = builder
    )
}