package com.example.costlyticmoneymanagement.view.screen

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Costlytic.Utils.Constant
import com.example.costlyticmoneymanagement.R
import com.example.costlyticmoneymanagement.data.DashboardHistoryDataItem
import com.example.costlyticmoneymanagement.utils.RenderUrlImage
import com.example.costlyticmoneymanagement.view.uicomponent.CircularImageView
import com.example.costlyticmoneymanagement.view.uicomponent.LineChartView

@Composable
fun DashboardScreen(
    onNotificationClick: () -> Unit,
    onNavigateToMoney: (typeKey: String, typeValue: String) -> Unit,
    onFabAddClick: () -> Unit = {}
) {
    // Exact dummyList from Dashboard.kt
    val dummyList = remember {
        listOf(
            DashboardHistoryDataItem(
                image = "https://via.placeholder.com/100",
                price = "₹2500",
                time = "10:30 AM"
            ),
            DashboardHistoryDataItem(
                image = "https://via.placeholder.com/100",
                price = "₹2500",
                time = "10:30 AM"
            ),
            DashboardHistoryDataItem(
                image = "https://via.placeholder.com/100",
                price = "₹2500",
                time = "10:30 AM"
            ),
            DashboardHistoryDataItem(
                image = "https://via.placeholder.com/100",
                price = "₹2500",
                time = "10:30 AM"
            ),
            DashboardHistoryDataItem(
                image = "https://via.placeholder.com/100",
                price = "₹1200",
                time = "Yesterday"
            ),
            DashboardHistoryDataItem(
                image = "https://via.placeholder.com/100",
                price = "₹5400",
                time = "12 Feb 2026"
            )
        )
    }

    // Exact dummyData for LineChartView
    val dummyChartData = remember {
        listOf(120f, 250f, 180f, 300f, 900f, 220f, 350f)
    }

    // Chart container entrance animation (alpha 0->1, translationY 100->0 over 600ms)
    var chartVisible by remember { mutableStateOf(false) }
    val chartAlpha by animateFloatAsState(
        targetValue = if (chartVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "chart_alpha"
    )
    val chartTranslationY by animateFloatAsState(
        targetValue = if (chartVisible) 0f else 100f,
        animationSpec = tween(durationMillis = 600),
        label = "chart_translation"
    )
    LaunchedEffect(Unit) {
        chartVisible = true
    }

    // Filter Tab & Bottom Nav Selection States
    var selectedFilterTab by remember { mutableStateOf("Today") }
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
                onTabSelected = { selectedBottomTab = it },
                onFabClick = onFabAddClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Top Gradient Detail Header (Replaces detailLayout)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFFF6E5),
                                Color(0x00F8EDD8)
                            )
                        ),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(bottom = 26.dp)
            ) {
                // Top Row: Profile Image, Month Pill, Notification Bell
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimensionResource(id = R.dimen.dimen_16))
                        .padding(horizontal = 18.dp)
                ) {
                    CircularImageView(
                        resId = R.drawable.profile,
                        modifier = Modifier.size(42.dp)
                    )

                    // Month Selector Pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .border(
                                border = BorderStroke(1.dp, Color(0xFFF1F1FA)),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(
                                horizontal = dimensionResource(id = R.dimen.dimen_12),
                                vertical = 5.dp
                            )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.down_arrow),
                            contentDescription = "Select Month",
                            modifier = Modifier.padding(end = 5.dp)
                        )
                        Text(
                            text = "Month",
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    }

                    // Notification Bell
                    Image(
                        painter = painterResource(id = R.drawable.ic_notification),
                        contentDescription = "Notifications",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onNotificationClick() }
                    )
                }

                // Account Balance Label & Amount
                Text(
                    text = "Account Balance",
                    fontSize = 16.sp,
                    fontFamily = manropeRegular,
                    color = grayText,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                )

                Text(
                    text = "₹ 400",
                    fontSize = 42.sp,
                    fontFamily = manropeExtraBold,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = dimensionResource(id = R.dimen.dimen_8))
                )

                // Income & Expense Cards Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                        .padding(top = dimensionResource(id = R.dimen.dimen_27))
                ) {
                    // Income Card
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = dimensionResource(id = R.dimen.dimen_8))
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color(0xFF00A86B))
                            .clickable {
                                onNavigateToMoney(Constant.INCOME, Constant.INCOME)
                            }
                            .padding(
                                start = dimensionResource(id = R.dimen.dimen_16),
                                top = dimensionResource(id = R.dimen.dimen_16),
                                bottom = dimensionResource(id = R.dimen.dimen_16),
                                end = 8.dp
                            )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_income),
                                contentDescription = "Income"
                            )
                        }

                        Column(
                            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.dimen_10))
                        ) {
                            Text(
                                text = "income",
                                fontFamily = manropeSemiBold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Text(
                                text = "₹ 5000",
                                fontFamily = manropeBold,
                                fontSize = 22.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White
                            )
                        }
                    }

                    // Expense Card
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = dimensionResource(id = R.dimen.dimen_8))
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color(0xFFFD3C4A))
                            .clickable {
                                onNavigateToMoney(Constant.EXPENSE, Constant.EXPENSE)
                            }
                            .padding(
                                start = 16.dp,
                                top = dimensionResource(id = R.dimen.dimen_16),
                                bottom = dimensionResource(id = R.dimen.dimen_16),
                                end = 8.dp
                            )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.expense),
                                contentDescription = "Expense"
                            )
                        }

                        Column(
                            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.dimen_10))
                        ) {
                            Text(
                                text = "expense",
                                fontFamily = manropeSemiBold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Text(
                                text = "₹ 5000",
                                fontFamily = manropeBold,
                                fontSize = 22.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Spend Frequency Title
            Text(
                text = "Spend Frequency",
                color = Color.Black,
                fontFamily = manropeBold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.dimen_16))
                    .padding(top = 14.dp)
            )

            // Animated Line Chart
            LineChartView(
                data = dummyChartData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .graphicsLayer {
                        alpha = chartAlpha
                        translationY = chartTranslationY
                    }
            )

            // Date Filter Card (Today / Week / Month / Year)
            val tabs = listOf("Today", "Week", "Month", "Year")
            Card(
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.dimen_16)),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    tabs.forEach { tab ->
                        val isSelected = selectedFilterTab == tab
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.dimen_16)))
                                .background(if (isSelected) Color(0xFFFCEED4) else Color.Transparent)
                                .clickable { selectedFilterTab = tab }
                                .padding(
                                    horizontal = dimensionResource(id = R.dimen.dimen_12),
                                    vertical = dimensionResource(id = R.dimen.dimen_8)
                                )
                        ) {
                            Text(
                                text = tab,
                                fontFamily = manropeBold,
                                fontSize = 16.sp,
                                maxLines = 1,
                                color = if (isSelected) yellowColor else grayText,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Transaction History List (Replaces DashBoardHistoryAdapter + RecyclerView)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                    .padding(top = 10.dp, bottom = 16.dp)
            ) {
                dummyList.forEach { item ->
                    DashboardHistoryRow(
                        item = item,
                        manropeBold = manropeBold,
                        manropeSemiBold = manropeSemiBold,
                        grayText = grayText,
                        redColor = redColor
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardHistoryRow(
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
        // Icon (Matches DashBoardHistoryAdapter: renders URL if non-null, else placeholder)
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

        // Title & Subtitle ("Shopping" / "Buy some grocery" from dashboard_histrory_item.xml)
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

        // Price & Time
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
private fun DashboardBottomBar(
    selectedIndex: Int,
    purpleBlue: Color,
    grayText: Color,
    manropeSemiBold: FontFamily,
    onTabSelected: (Int) -> Unit,
    onFabClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .background(Color.White)
    ) {
        Surface(
            shadowElevation = 12.dp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .align(Alignment.BottomCenter)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxSize()
            ) {
                BottomNavItem(
                    iconRes = R.drawable.ic_home,
                    label = "Home",
                    isSelected = selectedIndex == 0,
                    activeColor = purpleBlue,
                    inactiveColor = grayText,
                    fontFamily = manropeSemiBold,
                    onClick = { onTabSelected(0) }
                )

                BottomNavItem(
                    iconRes = R.drawable.ic_transaction,
                    label = "Transaction",
                    isSelected = selectedIndex == 1,
                    activeColor = purpleBlue,
                    inactiveColor = grayText,
                    fontFamily = manropeSemiBold,
                    onClick = { onTabSelected(1) }
                )

                // Center spacer for FloatingActionButton (replaces disabled menu[2] placeholder)
                Spacer(modifier = Modifier.width(56.dp))

                BottomNavItem(
                    iconRes = R.drawable.ic_expence,
                    label = "Budget",
                    isSelected = selectedIndex == 2,
                    activeColor = purpleBlue,
                    inactiveColor = grayText,
                    fontFamily = manropeSemiBold,
                    onClick = { onTabSelected(2) }
                )

                BottomNavItem(
                    iconRes = R.drawable.ic_person,
                    label = "Profile",
                    isSelected = selectedIndex == 3,
                    activeColor = purpleBlue,
                    inactiveColor = grayText,
                    fontFamily = manropeSemiBold,
                    onClick = { onTabSelected(3) }
                )
            }
        }

        // Center Floating '+' Button
        FloatingActionButton(
            onClick = onFabClick,
            containerColor = purpleBlue,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .size(56.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-16).dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "Add",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun BottomNavItem(
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