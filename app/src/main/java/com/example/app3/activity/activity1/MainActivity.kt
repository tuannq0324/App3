package com.example.app3.activity.activity1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app3.activity.view.MainScreen
import com.example.app3.activity.view.SecondScreen
import com.example.app3.ui.theme.App3Theme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App3Theme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screens.MainScreen.route,
                    modifier = Modifier
                ) {
                    composable(Screens.MainScreen.route) {
                        //call our composable screens here
                        MainScreen(navController, this@MainActivity)
                    }
                    composable(Screens.SecondScreen.route) {
                        //call our composable screens here
                        SecondScreen(this@MainActivity)
                    }
                }
            }
        }
    }


    sealed class Screens(val route: String) {
        data object MainScreen : Screens("main_route")
        data object SecondScreen : Screens("second_route")
    }
}

