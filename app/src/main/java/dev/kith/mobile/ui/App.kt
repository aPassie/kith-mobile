// app navigation root

package dev.kith.mobile.ui

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun App() {
    val nav = rememberNavController()
    Surface {
        NavHost(navController = nav, startDestination = "home") {
            composable("home") {
                Text("kith")
            }
        }
    }
}
