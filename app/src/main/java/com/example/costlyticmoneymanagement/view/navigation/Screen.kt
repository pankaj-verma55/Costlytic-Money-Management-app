package com.example.costlyticmoneymanagement.view.navigation

sealed class Screen(val route: String) {
    object Navigation : Screen("navigation")
    object Splash : Screen("SplashScreen")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object ForgetPassword : Screen("forget_password")
    object Verification : Screen("verification/{otp}") {
        fun createRoute(otp: String) = "verification/$otp"
    }
    object Dashboard : Screen("dashboard")

    object CreateBudget : Screen("create_budget")
    object Notification : Screen("notification")
    object Money : Screen("money/{typeKey}/{typeValue}") {
        fun createRoute(typeKey: String, typeValue: String) = "money/$typeKey/$typeValue"
    }

    object Transaction : Screen("transaction")

    object DetailTransaction : Screen("detail_transaction/{key}/{value}") {
        fun createRoute(key: String, value: String) = "detail_transaction/$key/$value"
    }

    object AddAccountBalance : Screen("add_account_balance")

    object FinancialReport : Screen("financial_report")

    object FinancialDetailReport : Screen("financial_detail_report")
    object Profile : Screen("profile")
}