package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import kotlin.math.abs

@Composable
fun FinancialReportScreen(
    onNavigateToFinancialDetailReport: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })

    // Fonts & Colors
    val manropeBold = FontFamily(Font(R.font.manrope_bold))
    val manropeExtraBold = FontFamily(Font(R.font.man_rope_extra_bold))
    val manropeSemiBold = FontFamily(Font(R.font.manrope_semibold))

    val redColor = colorResource(id = R.color.red)
    val greenColor = colorResource(id = R.color.green)
    val purpleBlue = colorResource(id = R.color.purple_blue)

    // Exact top indicator bar background color mapping from FinancialReportActivity.setDotPosition()
    val indicatorBarColor = when (pagerState.currentPage) {
        0 -> redColor
        1 -> greenColor
        2 -> purpleBlue
        else -> redColor
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(indicatorBarColor)
            .systemBarsPadding()
    ) {
        // 1. Top 3-Segment Story Progress Indicator (Replaces lineIndicatorLayout)
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(indicatorBarColor)
                .padding(24.dp)
        ) {
            repeat(3) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSelected) Color.White else Color.White.copy(alpha = 0.24f)
                        )
                )
            }
        }

        // 2. 3-Page HorizontalPager (Replaces ViewPager2 + NavigationFragmentAdapter)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        // Replaces page.alpha = 1 - abs(position)
                        alpha = (1f - abs(pageOffset)).coerceIn(0f, 1f)
                    }
            ) {
                when (page) {
                    0 -> MoneySpendPage(
                        redColor = redColor,
                        manropeBold = manropeBold,
                        manropeExtraBold = manropeExtraBold
                    )

                    1 -> MoneyEarnedPage(
                        greenColor = greenColor,
                        manropeBold = manropeBold,
                        manropeExtraBold = manropeExtraBold
                    )

                    2 -> ThoughtPage(
                        purpleBlue = purpleBlue,
                        manropeExtraBold = manropeExtraBold,
                        manropeSemiBold = manropeSemiBold,
                        onSeeDetailClick = onNavigateToFinancialDetailReport
                    )
                }
            }
        }
    }
}

