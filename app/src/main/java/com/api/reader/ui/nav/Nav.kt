package com.api.reader.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.api.reader.Greeting

@Composable
fun Nav() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination ="s"  ){
        composable(route= "s"){
            Greeting(navController)
        }
        composable(route = "link?id={id}", arguments = listOf(
            navArgument(name = "id"){
                type = NavType.StringType
            }
        )){
            Start(it.arguments?.getString("id"),navController)
        }
    }

}