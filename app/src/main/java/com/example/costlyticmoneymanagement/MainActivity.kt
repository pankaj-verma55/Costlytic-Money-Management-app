package com.example.costlyticmoneymanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.costlyticmoneymanagement.ui.theme.CostlyticMoneyManagementTheme
import com.example.costlyticmoneymanagement.view.navigation.Screen
import com.example.costlyticmoneymanagement.view.screen.DashboardScreen
import com.example.costlyticmoneymanagement.view.screen.FinancialDetailReportScreen
import com.example.costlyticmoneymanagement.view.screen.FinancialReportScreen
import com.example.costlyticmoneymanagement.view.screen.ForgetPasswordScreen
import com.example.costlyticmoneymanagement.view.screen.LoginScreen
import com.example.costlyticmoneymanagement.view.screen.MoneyScreen
import com.example.costlyticmoneymanagement.view.screen.NavigationScreen
import com.example.costlyticmoneymanagement.view.screen.NotificationScreen
import com.example.costlyticmoneymanagement.view.screen.ProfileDetailItemScreen
import com.example.costlyticmoneymanagement.view.screen.SignUpScreen
import com.example.costlyticmoneymanagement.view.screen.TransferScreen
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
                startDestination = Screen.Navigation.route
            ) {
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

                composable(Screen.Dashboard.route) {
                    DashboardScreen(
                        onNotificationClick = {
                            navController.navigate(Screen.Notification.route)
                        },
                        onNavigateToMoney = { typeKey, typeValue ->
                            navController.navigate(Screen.Money.createRoute(typeKey, typeValue))
                        }
                    )
                }

                composable(Screen.ForgetPassword.route) {
                    ForgetPasswordScreen(
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
                composable(Screen.Money.route) { backStackEntry ->
                    val typeKey = backStackEntry.arguments?.getString("typeKey") ?: ""
                    val typeValue = backStackEntry.arguments?.getString("typeValue") ?: ""

                    MoneyScreen(
                        typeKey = typeKey,
                        typeValue = typeValue,
                        onBackToDashboard = {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Dashboard.route) { inclusive = true }
                            }
                        }
                    )
                }
                composable(Screen.Notification.route) {
                    NotificationScreen(
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
                composable(Screen.Transaction.route) {
                    TransferScreen(
                        onBackToDashboard = {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Dashboard.route) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Screen.Dashboard.route) {
                    DashboardScreen(
                        onNotificationClick = {
                            navController.navigate(Screen.Notification.route)
                        },
                        onNavigateToMoney = { typeKey, typeValue ->
                            navController.navigate(Screen.Money.createRoute(typeKey, typeValue))
                        },
                        onNavigateToDetail = { key, value ->
                            navController.navigate(Screen.DetailTransaction.createRoute(key, value))
                        },
                        onLogoutSuccess = {
                            // Replaces FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onNavigateToFinancialReport = {
                            navController.navigate(Screen.FinancialReport.route)
                        },
                    )
                }

                composable(Screen.DetailTransaction.route) { backStackEntry ->
                    val key = backStackEntry.arguments?.getString("key") ?: ""
                    val value = backStackEntry.arguments?.getString("value") ?: ""

                    ProfileDetailItemScreen(
                        key = key,
                        value = value,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onNavigateToDetailAccount = {
                            navController.navigate(Screen.DetailAccount.route)
                        },
                    )
                }

                composable(Screen.FinancialReport.route) {
                    FinancialReportScreen(
                        onNavigateToFinancialDetailReport = {
                            navController.navigate(Screen.FinancialDetailReport.route)
                        }
                    )
                }

                composable(Screen.FinancialDetailReport.route) {
                    FinancialDetailReportScreen(
                        onBackClick = {
                            navController.popBackStack()
                        })
                }



            }
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