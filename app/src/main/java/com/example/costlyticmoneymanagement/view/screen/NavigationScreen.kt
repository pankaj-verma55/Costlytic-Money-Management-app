package com.example.costlyticmoneymanagement.view.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.costlyticmoneymanagement.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlin.math.abs

private data class OnboardingPage(
    @DrawableRes val imageRes: Int,
    val title: String,
    val description: String
)

@Composable
fun NavigationScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToLogin: () -> Unit,
//    onAutoLoginToDashboard: () -> Unit
) {
//    // Replaces onStart() Firebase currentUser check
//    LaunchedEffect(Unit) {
//        val currentUser = FirebaseAuth.getInstance().currentUser
//        if (currentUser != null) {
//            onAutoLoginToDashboard()
//        }
//    }

    val interBold = FontFamily(Font(R.font.inter_bold))

    // Exact data from first_screen_fragment, second_screen_fragment, third_screen_fragment
    val pages = listOf(
        OnboardingPage(
            imageRes = R.drawable.ic_onboarding1,
            title = "Gain total control\n of your money",
            description = "Become your own money manager \nand make every cent count"
        ),
        OnboardingPage(
            imageRes = R.drawable.ic_onboarding2,
            title = "Know where your\nmoney goes",
            description = "Track your transaction easily,\nwith categories and financial report "
        ),
        OnboardingPage(
            imageRes = R.drawable.ic_onboarding3,
            title = "Planning ahead",
            description = "Setup your budget for each category\nso you in control"
        )
    )

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pages.size })

    // Replaces Handler().postDelayed(runnable, 3000) auto-scroll loop
    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000L)
            val nextPage = if (pagerState.currentPage < pages.size - 1) {
                pagerState.currentPage + 1
            } else {
                0
            }
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ViewPager2 replacement with your exact alpha PageTransformer
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        // Replaces page.alpha = 1 - abs(position)
                        alpha = (1f - abs(pageOffset)).coerceIn(0f, 1f)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = pages[page].imageRes),
                    contentDescription = null,
                    modifier = Modifier.padding(top = 32.dp)
                )

                Text(
                    text = pages[page].title,
                    fontFamily = interBold,
                    fontSize = 32.sp,
                    lineHeight = 39.sp,
                    color = Color(0xFF212325),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .padding(horizontal = 49.dp)
                )

                Text(
                    text = pages[page].description,
                    fontFamily = interBold,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    color = Color(0xFF91919F),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(horizontal = 49.dp)
                )
            }
        }

        // 3-Dot Page Indicator (Replaces dottedIndicatorLayout)
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(24.dp)
        ) {
            repeat(pages.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) Color(0xFF7F3DFF) else Color(0xFFEEE5FF)
                        )
                )
            }
        }

        // Sign Up Button (DarkPurpleButton)
        Button(
            onClick = onNavigateToSignUp,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7F3DFF),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp)
        ) {
            Text(
                text = "Sign Up",
                fontFamily = interBold,
                fontSize = 18.sp,
                color = Color.White
            )
        }

        // Login Button (LightPurpleButton)
        Button(
            onClick = onNavigateToLogin,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEEE5FF),
                contentColor = Color(0xFF7F3DFF)
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = "Login",
                fontFamily = interBold,
                fontSize = 18.sp,
                color = Color(0xFF7F3DFF)
            )
        }
    }
}