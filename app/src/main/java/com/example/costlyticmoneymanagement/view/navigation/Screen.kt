package com.example.costlyticmoneymanagement.view.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Navigation : Screen("navigation")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object ForgetPassword : Screen("forget_password")
    object Verification : Screen("verification/{otp}") {
        fun createRoute(otp: String) = "verification/$otp"
    }
    object Dashboard : Screen("dashboard")
}