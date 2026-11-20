package com.example.costlyticmoneymanagement.view.screen

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
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.costlyticmoneymanagement.R
import com.example.costlyticmoneymanagement.model.data.CircleChartItem
import com.example.costlyticmoneymanagement.view.uicomponent.LineChartView
import com.example.costlyticmoneymanagement.view.uicomponent.MulticolorCircleChart
import com.example.costlyticmoneymanagement.view.uicomponent.RoundedProgressBar

@Composable
fun FinancialDetailReportScreen(
    onBackClick: () -> Unit
) {
    // Chart Type State: true = LineChartFinancialFragment, false = CircleGraphFragment
    var isLineChartSelected by remember { mutableStateOf(true) }

    // Expense vs Income Tab State: true = Expense, false = Income
    var isExpenseSelected by remember { mutableStateOf(true) }

    // Exact dummy data from FinancialDetailReportActivity
    val categoryList = remember {
        listOf(
            CircleChartItem(
                value = 72f,
                color = android.graphics.Color.parseColor("#F9A825"),
                category = "Shopping",
                price = -120
            ),
            CircleChartItem(
                value = 72f,
                color = android.graphics.Color.parseColor("#F9A825"),
                category = "Shopping",
                price = -120
            )
        )
    }

    // Exact dummy data from LineChartFinancialFragment
    val lineChartData = remember {
        listOf(120f, 250f, 180f, 300f, 900f, 220f, 350f)
    }

    // Exact dummy data from CircleGraphFragment
    val donutChartData = remember {
        listOf(
            CircleChartItem(120f, android.graphics.Color.parseColor("#F9A825"), "a", 1),
            CircleChartItem(10f, android.graphics.Color.parseColor("#7B1FA2"), "a", 1),
            CircleChartItem(20f, android.graphics.Color.parseColor("#E53935"), "a", 1),
            CircleChartItem(32f, android.graphics.Color.parseColor("#1E88E5"), "a", 1)
        )
    }

    // LineChart entrance animation (alpha 0->1, translationY 100->0, 600ms)
    var lineChartVisible by remember { mutableStateOf(false) }
    val chartAlpha by animateFloatAsState(
        targetValue = if (lineChartVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "detail_line_alpha"
    )
    val chartTranslationY by animateFloatAsState(
        targetValue = if (lineChartVisible) 0f else 100f,
        animationSpec = tween(durationMillis = 600),
        label = "detail_line_translation"
    )
    LaunchedEffect(isLineChartSelected) {
        if (isLineChartSelected) {
            lineChartVisible = false
            lineChartVisible = true
        }
    }

    // Fonts & Colors
    val interMedium = FontFamily(Font(R.font.inter_medium))
    val manropeMedium = FontFamily(Font(R.font.manrope_medium))
    val manropeSemiBold = FontFamily(Font(R.font.manrope_semibold))
    val manropeExtraBold = FontFamily(Font(R.font.manrope_extrabold))

    val blackText = colorResource(id = R.color.black_text)
    val purpleBlue = colorResource(id = R.color.purple_blue)
    val redColor = colorResource(id = R.color.red)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        // 1. Top Header (Back Button + Centered "Financial Report")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.dimen_16)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_left_arrow),
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { onBackClick() }
            )

            Text(
                text = "Financial Report",
                fontFamily = interMedium,
                fontSize = 18.sp,
                color = blackText,
                textAlign = TextAlign.Center
            )
        }

        // 2. Month Pill & Chart Type Switcher (Line Chart vs Circle Graph)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp)
        ) {
            // Month Pill
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
                    contentDescription = "Month",
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(
                    text = "Month",
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }

            // Line Chart & Circle Graph Toggle Buttons
            Row(
                modifier = Modifier
                    .border(
                        border = BorderStroke(1.dp, Color(0xFFF1F1FA)),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
            ) {
                // Line Chart Button
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(if (isLineChartSelected) purpleBlue else Color.White)
                        .clickable { isLineChartSelected = true }
                        .padding(dimensionResource(id = R.dimen.dimen_12))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_line_chart),
                        contentDescription = "Line Chart",
                        colorFilter = ColorFilter.tint(
                            if (isLineChartSelected) Color.White else purpleBlue
                        ),
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Circle Graph Button
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(if (!isLineChartSelected) purpleBlue else Color.White)
                        .clickable { isLineChartSelected = false }
                        .padding(dimensionResource(id = R.dimen.dimen_12))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_circle_chart),
                        contentDescription = "Circle Graph",
                        colorFilter = ColorFilter.tint(
                            if (!isLineChartSelected) Color.White else purpleBlue
                        ),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // 3. Graph Container (250.dp height matching graphFragment)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(top = 8.dp)
        ) {
            if (isLineChartSelected) {
                LineChartView(
                    data = lineChartData,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            alpha = chartAlpha
                            translationY = chartTranslationY
                        }
                )

                // priceshow ($ 6000) visible only on LineChart
                Text(
                    text = "$ 6000",
                    fontSize = 32.sp,
                    color = Color.Black,
                    fontFamily = manropeExtraBold,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 16.dp)
                )
            } else {
                MulticolorCircleChart(
                    items = donutChartData,
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.Center)
                )
            }
        }

        // 4. Expense / Income Segmented Toggle (Replaces reportLayout)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFFF1F1FA))
                .padding(dimensionResource(id = R.dimen.dimen_4))
        ) {
            // Expense Tab
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(32.dp))
                    .background(if (isExpenseSelected) purpleBlue else Color.Transparent)
                    .clickable { isExpenseSelected = true }
                    .padding(vertical = dimensionResource(id = R.dimen.dimen_15))
            ) {
                Text(
                    text = "Expense",
                    fontSize = 16.sp,
                    color = if (isExpenseSelected) Color.White else Color.Black,
                    fontFamily = manropeSemiBold
                )
            }

            // Income Tab
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(32.dp))
                    .background(if (!isExpenseSelected) purpleBlue else Color.Transparent)
                    .clickable { isExpenseSelected = false }
                    .padding(vertical = dimensionResource(id = R.dimen.dimen_15))
            ) {
                Text(
                    text = "Income",
                    fontSize = 16.sp,
                    color = if (!isExpenseSelected) Color.White else Color.Black,
                    fontFamily = manropeSemiBold
                )
            }
        }

        // 5. Category Filter Pill (Replaces categoryLayout)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.dimen_16))
                .padding(top = dimensionResource(id = R.dimen.dimen_24))
                .border(
                    border = BorderStroke(1.dp, Color(0xFFF1F1FA)),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(vertical = 8.dp, horizontal = dimensionResource(id = R.dimen.dimen_12))
        ) {
            Image(
                painter = painterResource(id = R.drawable.down_arrow),
                contentDescription = "Category",
                modifier = Modifier.size(dimensionResource(id = R.dimen.dimen_16))
            )
            Text(
                text = "Shopping",
                color = Color.Black,
                fontSize = 14.sp,
                fontFamily = manropeMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // 6. Category Breakdown List (Replaces categoryRv + FinancialReportChartAdapter)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.dimen_10), bottom = 24.dp)
        ) {
            categoryList.forEach { item ->
                FinancialReportCategoryRow(
                    item = item,
                    manropeMedium = manropeMedium,
                    redColor = redColor
                )
            }
        }
    }
}

@Composable
private fun FinancialReportCategoryRow(
    item: CircleChartItem,
    manropeMedium: FontFamily,
    redColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                .padding(top = dimensionResource(id = R.dimen.dimen_24))
        ) {
            // Category Pill with Color Dot
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .border(
                        border = BorderStroke(1.dp, Color(0xFFF1F1FA)),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(vertical = 8.dp, horizontal = dimensionResource(id = R.dimen.dimen_12))
            ) {
                Box(
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.dimen_16))
                        .clip(CircleShape)
                        .background(Color(item.color))
                )
                Text(
                    text = item.category,
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontFamily = manropeMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Price Text (Replaces priceTxt)
            Text(
                text = item.price.toString(),
                color = redColor,
                fontSize = 24.sp,
                fontFamily = manropeMedium
            )
        }

        // Animated Rounded Progress Bar (Replaces roundedProgressBar)
        RoundedProgressBar(
            progress = item.value,
            progressColor = Color(item.color),
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.dimen_20))
                .padding(16.dp)
        )
    }
}