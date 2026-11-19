package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.costlyticmoneymanagement.R

@Composable
fun DashboardBottomBar(
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

                Spacer(modifier = Modifier.width(56.dp))

                BottomNavItem(
                    iconRes = R.drawable.ic_expence,
                    label = "Budget",
                    isSelected = selectedIndex == 3,
                    activeColor = purpleBlue,
                    inactiveColor = grayText,
                    fontFamily = manropeSemiBold,
                    onClick = { onTabSelected(3) }
                )

                BottomNavItem(
                    iconRes = R.drawable.ic_person,
                    label = "Profile",
                    isSelected = selectedIndex == 4,
                    activeColor = purpleBlue,
                    inactiveColor = grayText,
                    fontFamily = manropeSemiBold,
                    onClick = { onTabSelected(4) }
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