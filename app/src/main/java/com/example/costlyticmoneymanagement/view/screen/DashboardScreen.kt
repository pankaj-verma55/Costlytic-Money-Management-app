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
import com.example.costlyticmoneymanagement.R
import com.example.costlyticmoneymanagement.model.data.DashboardHistoryDataItem
import com.example.costlyticmoneymanagement.utils.RenderUrlImage

@Composable
fun DashboardScreen(
    onNotificationClick: () -> Unit,
    onNavigateToMoney: (typeKey: String, typeValue: String) -> Unit,
    onNavigateToDetail: (key: String, value: String) -> Unit = { _, _ -> },
    onLogoutSuccess: () -> Unit = {},
    onNavigateToCreateBudget: () -> Unit = {},
    onNavigateToFinancialReport: () -> Unit = {}
) {
    // 0 = Home, 1 = Transaction, 2 = Center '+' (Transfer), 3 = Budget, 4 = Profile
    var selectedBottomTab by remember { mutableIntStateOf(0) }

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
                onTabSelected = { index -> selectedBottomTab = index },
                onFabClick = { selectedBottomTab = 2 }
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
                        manropeRegular = manropeRegular,
                        manropeSemiBold = manropeSemiBold,
                        manropeBold = manropeBold,
                        manropeExtraBold = manropeExtraBold,
                        grayText = grayText,
                        yellowColor = yellowColor,
                        redColor = redColor,
                        onNotificationClick = onNotificationClick,
                        onNavigateToProfile= {selectedBottomTab = 4},
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
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "Profile Screen",
//                            fontFamily = manropeBold,
//                            fontSize = 18.sp,
//                            color = grayText
//                        )
//                    }
                }
            }
        }
    }
}

@Composable
fun DashboardHistoryRow(
    item: DashboardHistoryDataItem,
    manropeBold: FontFamily,
    manropeSemiBold: FontFamily,
    grayText: Color,
    redColor: Color
) {
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
        if (item.image != null) {
            RenderUrlImage(
                imageUrl = item.image,
                contentDescription = "Category Icon",
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_img_placeholder),
                contentDescription = "Placeholder Icon",
                modifier = Modifier.size(48.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = dimensionResource(id = R.dimen.dimen_10))
        ) {
            Text(
                text = "Shopping",
                fontFamily = manropeBold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = "Buy some grocery",
                fontFamily = manropeSemiBold,
                fontSize = 14.sp,
                color = grayText
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = item.price ?: "- ₹100",
                fontFamily = manropeBold,
                fontSize = 18.sp,
                color = redColor
            )
            Text(
                text = item.time ?: "10:00 AM",
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

