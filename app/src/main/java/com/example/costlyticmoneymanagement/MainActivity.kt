package com.example.costlyticmoneymanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.costlyticmoneymanagement.ui.theme.CostlyticMoneyManagementTheme
import com.example.costlyticmoneymanagement.view.navigation.Screen
import com.example.costlyticmoneymanagement.view.screen.LoginScreen
import com.example.costlyticmoneymanagement.view.screen.SignUpScreen
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.Login.route
            ) {
                composable(Screen.Login.route) {
                    LoginScreen(
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onLoginSuccess = {
                            // Navigate to Dashboard and remove Login from backstack
                            // so pressing Back on Dashboard doesn't return to Login
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        },
                        onSignUpClick = {
                            navController.navigate(Screen.SignUp.route)
                        },
                        onForgetPasswordClick = {
                            navController.navigate(Screen.ForgetPassword.route)
                        }
                    )
                }

                composable(Screen.SignUp.route) {
                    // SignUpScreen(navController = navController)
                    SignUpScreen(
                        onBackClick = { navController.popBackStack() },
                        onLoginClick = {
//                            navController.navigate(Screen.Login.route) {
//                                popUpTo(Screen.Login.route) { inclusive = true }
//                            }
                            navController.popBackStack()
                        },
                        onNavigateToVerification = { otp ->
                            navController.navigate(Screen.Verification.createRoute(otp))
                        }
                    )
                }

                // 3. Forget Password Screen (Placeholder until you share it)
                composable(Screen.ForgetPassword.route) {
                    // ForgetPasswordScreen(navController = navController)
                }

                // 4. Dashboard Screen (Placeholder until you share it)
                composable(Screen.Dashboard.route) {
                    // DashboardScreen(navController = navController)
                }
            }
//            CostlyticMoneyManagementTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CostlyticMoneyManagementTheme {
        Greeting("Android")
    }
}