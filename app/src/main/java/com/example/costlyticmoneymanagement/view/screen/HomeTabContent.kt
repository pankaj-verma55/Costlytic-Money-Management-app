package com.example.costlyticmoneymanagement.view.screen


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Costlytic.Utils.Constant
import com.example.costlyticmoneymanagement.R
import com.example.costlyticmoneymanagement.model.data.DashboardHistoryDataItem
import com.example.costlyticmoneymanagement.view.uicomponent.CircularImageView
import com.example.costlyticmoneymanagement.view.uicomponent.LineChartView

@Composable
fun HomeTabContent(
    manropeRegular: FontFamily,
    manropeSemiBold: FontFamily,
    manropeBold: FontFamily,
    manropeExtraBold: FontFamily,
    grayText: Color,
    yellowColor: Color,
    redColor: Color,
    onNotificationClick: () -> Unit,
    onNavigateToMoney: (typeKey: String, typeValue: String) -> Unit
) {
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

    val dummyChartData = remember {
        listOf(120f, 250f, 180f, 300f, 900f, 220f, 350f)
    }

    val monthOptions = remember {
        listOf(
            "Yearly", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
    }
    var selectedMonth by remember { mutableStateOf("Month") }
    var isMonthSliderVisible by remember { mutableStateOf(false) }
    val purpleBlue = colorResource(id = R.color.purple_blue)

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

    var selectedFilterTab by remember { mutableStateOf("Today") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top Gradient Detail Header
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (selectedMonth != "Month") Color(0xFFEEE5FF) else Color.Transparent
                        )
                        .border(
                            border = BorderStroke(
                                1.dp,
                                if (selectedMonth != "Month") purpleBlue else Color(0xFFF1F1FA)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { isMonthSliderVisible = !isMonthSliderVisible }
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.dimen_12),
                            vertical = 5.dp
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.down_arrow),
                        contentDescription = "down Arrow",
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Text(
                        text = selectedMonth,
                        color = if (selectedMonth != "Month") purpleBlue else Color.Black,
                        fontSize = 16.sp,
                        fontFamily = manropeSemiBold
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

            AnimatedVisibility(
                visible = isMonthSliderVisible,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(monthOptions) { month ->
                        val isSelected = selectedMonth == month || (selectedMonth == "Month" && month == "All")

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) purpleBlue else Color(0xFFEEE5FF))
                                .clickable {
                                    selectedMonth = month
                                    isMonthSliderVisible = false
                                    // Filter dashboard transactions / chart data by `month` here
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = month,
                                fontSize = 14.sp,
                                fontFamily = manropeSemiBold,
                                color = if (isSelected) Color.White else purpleBlue
                            )
                        }
                    }
                }
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
                            .padding(
                                horizontal = dimensionResource(id = R.dimen.dimen_2),
                                vertical = dimensionResource(id = R.dimen.dimen_2)
                            )
                            .weight(1f)
                            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.dimen_16)))
                            .background(if (isSelected) Color(0xFFFCEED4) else Color.Transparent)
                            .clickable { selectedFilterTab = tab }
                            .padding(
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

        // Transaction History List
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