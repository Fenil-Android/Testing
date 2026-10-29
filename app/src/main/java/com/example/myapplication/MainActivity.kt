package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.myapplication.compose.UserDetailScreen
import com.example.myapplication.compose.UserListScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewModel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = (application as UserApplication).repository
        val factory = UserViewModel.Factory(repository)

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "userList") {
                    composable("userList") {
                        val vm: UserViewModel = viewModel(factory = factory)
                        UserListScreen(viewModel = vm, onUserClick = { id ->
                            navController.navigate("userDetail/$id")
                        })
                    }
                    composable(
                        "userDetail/{userId}",
                        arguments = listOf(navArgument("userId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("userId") ?: 0
                        val vm: UserViewModel = viewModel(factory = factory)
                        UserDetailScreen(
                            userId = id,
                            viewModel = vm,
                            onBackClick = { navController.navigateUp() }
                        )
                    }
                }
            }
        }
    }
}