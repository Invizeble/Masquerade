package com.invize.masquerade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.invize.masquerade.ui.auth.AuthScreen
import com.invize.masquerade.ui.chat.ChatScreen
import com.invize.masquerade.ui.home.HomeScreen
import com.invize.masquerade.ui.theme.MasqueradeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
            "home"
        } else {
            "auth"
        }

        setContent {
            MasqueradeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MasqueradeTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable("auth") {
                            AuthScreen(
                                onAuthSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("auth") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("home") {
                            HomeScreen(
                                onChatClick = { chatId, chatName ->
                                    navController.navigate("chat/$chatId/$chatName")
                                },
                                onLogout = {
                                    FirebaseAuth.getInstance().signOut()
                                    navController.navigate("auth") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("chat/{chatId}/{chatName}") { backStackEntry ->
                            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                            val chatName = backStackEntry.arguments?.getString("chatName") ?: ""
                            ChatScreen(
                                chatId = chatId,
                                chatName = chatName,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}