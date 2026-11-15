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
import com.example.costlyticmoneymanagement.view.screen.NavigationScreen
import com.example.costlyticmoneymanagement.view.screen.SignUpScreen
import com.example.costlyticmoneymanagement.view.screen.SplashScreen
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
                startDestination = Screen.Splash.route
            ) {
                composable(Screen.Splash.route) {
                    SplashScreen(
                        onNavigateNext = {
                            navController.navigate(Screen.Navigation.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }
                    )
                }

                // 2. Navigation / Onboarding Screen (Placeholder until you share NavigationActivity)
                composable(Screen.Navigation.route) {
                    NavigationScreen(
                        onNavigateToSignUp = {
                            navController.navigate(Screen.SignUp.route)
                        },
                        onNavigateToLogin = {
                            navController.navigate(Screen.Login.route)
                        },
                        onAutoLoginToDashboard = {
                            // Replaces FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK + finish()
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Navigation.route) { inclusive = true }
                            }
                        }
                    )
                }
                composable(Screen.Login.route) {
                    LoginScreen(
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onLoginSuccess = {
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