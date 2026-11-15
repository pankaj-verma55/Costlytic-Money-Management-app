package com.example.costlyticmoneymanagement.view.navigation

sealed class Screen(val route: String) {
    object Navigation : Screen("navigation")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object ForgetPassword : Screen("forget_password")
    object Verification : Screen("verification/{otp}") {
        fun createRoute(otp: String) = "verification/$otp"
    }
    object Dashboard : Screen("dashboard")
    object Notification : Screen("notification")
    object Money : Screen("money/{typeKey}/{typeValue}") {
        fun createRoute(typeKey: String, typeValue: String) = "money/$typeKey/$typeValue"
    }
}