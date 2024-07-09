package com.example.app3.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app3.navigation.AppNavigation
import com.example.app3.ui.screen1.FirstScreen
import com.example.app3.ui.screen2.SecondScreen
import com.example.app3.ui.theme.App3Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App3Theme {
                AppNavigation()
            }
        }
    }

}

