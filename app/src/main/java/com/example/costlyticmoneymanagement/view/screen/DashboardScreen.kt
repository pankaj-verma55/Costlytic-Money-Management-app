package com.example.costlyticmoneymanagement.view.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.costlyticmoneymanagement.R
import com.example.costlyticmoneymanagement.model.data.DashboardHistoryDataItem
import com.example.costlyticmoneymanagement.model.data.TransactionRecord
import com.example.costlyticmoneymanagement.utils.RenderUrlImage
import com.example.costlyticmoneymanagement.viewmodel.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    onNotificationClick: () -> Unit,
    onNavigateToMoney: (typeKey: String, typeValue: String) -> Unit,
    onNavigateToDetail: (key: String, value: String) -> Unit = { _, _ -> },
    onLogoutSuccess: () -> Unit = {},
    onNavigateToCreateBudget: () -> Unit = {},
    onNavigateToFinancialReport: () -> Unit = {},
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    // 0 = Home, 1 = Transaction, 2 = Center '+' (Transfer), 3 = Budget, 4 = Profile
    val selectedBottomTab = dashboardViewModel.selectedBottomTab
    val uiState by dashboardViewModel.uiState.collectAsState()

    // Fonts & Colors
    val manropeRegular = FontFamily(Font(R.font.manrope_regular))
    val manropeSemiBold = FontFamily(Font(R.font.manrope_semibold))
    val manropeBold = FontFamily(Font(R.font.manrope_bold))
    val manropeExtraBold = FontFamily(Font(R.font.man_rope_extra_bold))

    val grayText = colorResource(id = R.color.gray_Text)
    val purpleBlue = colorResource(id = R.color.purple_blue)
    val yellowColor = colorResource(id = R.color.yellow)
    val redColor = colorResource(id = R.color.red)

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            DashboardBottomBar(
                selectedIndex = selectedBottomTab,
                purpleBlue = purpleBlue,
                grayText = grayText,
                manropeSemiBold = manropeSemiBold,
                onTabSelected = { index -> dashboardViewModel.selectBottomTab(index) },
                onFabClick = { dashboardViewModel.selectBottomTab(2) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedBottomTab) {
                0 -> {
                    HomeTabContent(
                        uiState = uiState,
                        selectedMonth = dashboardViewModel.selectedMonth,
                        selectedFilterTab = dashboardViewModel.selectedFilterTab,
                        onMonthSelected = { dashboardViewModel.selectMonth(it) },
                        onFilterTabSelected = { dashboardViewModel.selectFilterTab(it) },
                        manropeRegular = manropeRegular,
                        manropeSemiBold = manropeSemiBold,
                        manropeBold = manropeBold,
                        manropeExtraBold = manropeExtraBold,
                        grayText = grayText,
                        yellowColor = yellowColor,
                        redColor = redColor,
                        onNotificationClick = onNotificationClick,
                        onNavigateToProfile = { dashboardViewModel.selectBottomTab(4) },
                        onNavigateToMoney = onNavigateToMoney
                    )
                }

                1 -> {
                    TransactionScreen(
                        onNavigateToFinancialReport = onNavigateToFinancialReport
                    )
                }

                2 -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+ Add screen",
                            fontFamily = manropeBold,
                            fontSize = 18.sp,
                            color = grayText
                        )
                    }
                }

                3 -> {
                    // 4. BUDGET TAB (Drop your BudgetScreen() here once created)

                    BudgetScreen(
                        onNavigateToCreateBudget = onNavigateToCreateBudget
                    )
                }

                4 -> {
                    ProfileScreen(
                        onNavigateToDetail = onNavigateToDetail,
                        onLogoutSuccess = onLogoutSuccess
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardHistoryRow(
    item: TransactionRecord,
    manropeBold: FontFamily,
    manropeSemiBold: FontFamily,
    grayText: Color,
    redColor: Color
) {
    val isIncome = item.type.equals("Income", ignoreCase = true)
    val amountColor = if (isIncome) Color(0xFF00A86B) else redColor
    val amountPrefix = if (isIncome) "+ ₹" else "- ₹"
    val formattedTime = remember(item.timestamp) {
        if (item.timestamp > 0L) {
            SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(item.timestamp))
        } else {
            "Just now"
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.dimen_8))
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFFCFCFC))
            .padding(
                horizontal = dimensionResource(id = R.dimen.dimen_16),
                vertical = 14.dp
            )
    ) {
        Image(
            painter = painterResource(
                id = if (isIncome) R.drawable.ic_income else R.drawable.expense
            ),
            contentDescription = item.category,
            modifier = Modifier.size(48.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = dimensionResource(id = R.dimen.dimen_10))
        ) {
            Text(
                text = item.category.ifBlank { item.type },
                fontFamily = manropeBold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = item.description.ifBlank { item.wallet },
                fontFamily = manropeSemiBold,
                fontSize = 14.sp,
                color = grayText
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$amountPrefix${item.numericAmount().toInt()}",
                fontFamily = manropeBold,
                fontSize = 18.sp,
                color = amountColor
            )
            Text(
                text = formattedTime,
                fontFamily = manropeSemiBold,
                fontSize = 14.sp,
                color = grayText
            )
        }
    }
}

@Composable
fun BottomNavItem(
    @DrawableRes iconRes: Int,
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    fontFamily: FontFamily,
    onClick: () -> Unit
) {
    val tint = if (isSelected) activeColor else inactiveColor
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 6.dp, horizontal = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            fontFamily = fontFamily,
            fontSize = 11.sp,
            color = tint
        )
    }
}

