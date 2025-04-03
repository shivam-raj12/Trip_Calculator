package com.example.tripcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tripcalculator.screens.history.HistoryScreen
import com.example.tripcalculator.screens.home.HomeScreen
import com.example.tripcalculator.ui.theme.TripCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TripCalculatorTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "home_screen",
                        modifier = Modifier.padding(it)
                    ) {
                        composable(
                            route = "home_screen"
                        ) {
                            HomeScreen(navController)
                        }
                        composable(
                            route = "history_screen"
                        ) {
                            HistoryScreen()
                        }
                    }
                }
            }
        }
    }
}