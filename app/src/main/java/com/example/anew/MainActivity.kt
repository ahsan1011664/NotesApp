package com.example.anew

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.anew.screens.LoginScreen
import com.example.anew.screens.NotesInfo
import com.example.anew.screens.NotesScreen
import com.example.anew.screens.RegisterScreen
import com.example.anew.screens.UpdateNoteScreen
import com.example.anew.ui.theme.NEWTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        var isLoading = true

        lifecycleScope.launch {
            delay(5000L) // 2 seconds splash
            isLoading = false
        }

        splashScreen.setKeepOnScreenCondition { isLoading }

        setContent {
            NEWTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {

                    composable(
                        route = "login?email={email}&password={password}",
                        arguments = listOf(
                            navArgument("email") {
                                type = NavType.StringType
                                defaultValue = ""
                            },
                            navArgument("password") {
                                type = NavType.StringType
                                defaultValue = ""
                            }
                        )
                    ) { backStackEntry ->
                        val email = backStackEntry.arguments?.getString("email") ?: ""
                        val password = backStackEntry.arguments?.getString("password") ?: ""
                        LoginScreen(
                            navHostController = navController,
                            initialemail = email,
                            initialpassword = password
                        )
                    }

                    composable("register") {
                        RegisterScreen(navController)
                    }

                    composable(
                        route = "Notes/{userId}/{username}",
                        arguments = listOf(
                            navArgument("userId") {
                                type = NavType.IntType
                            },
                            navArgument("username") {
                                type = NavType.StringType
                            }
                        )
                    ) { backStackEntry ->
                        val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
                        val usernameEncoded = backStackEntry.arguments?.getString("username") ?: ""
                        val username = Uri.decode(usernameEncoded)
                        NotesScreen(navController, userId, username)
                    }

                    composable("Info/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                        if (id != null) {
                            NotesInfo(navController, id)
                        }
                    }

                    composable("Update/{noteId}") { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
                        if (noteId != null) {
                            UpdateNoteScreen(navController, noteId)
                        }
                    }
                }
            }
        }
    }
}
