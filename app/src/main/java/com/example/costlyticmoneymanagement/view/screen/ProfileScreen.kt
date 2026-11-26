package com.example.costlyticmoneymanagement.view.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.costlyticmoneymanagement.view.dialog.LogoutBottomSheet
import com.example.costlyticmoneymanagement.view.uicomponent.CircularImageView
import com.example.costlyticmoneymanagement.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onNavigateToDetail: (key: String, value: String) -> Unit,
    onLogoutSuccess: () -> Unit,
    onEditProfileClick: () -> Unit = {},
    profileViewModel: ProfileViewModel = viewModel()
) {
// Collect ViewModel UI State
    val uiState by profileViewModel.uiState.collectAsState()
    val userProfile = uiState.userProfile
    // Fonts & Colors
    val manropeRegular = FontFamily(Font(R.font.manrope_regular))
    val manropeMedium = FontFamily(Font(R.font.manrope_medium))
    val lightGray1 = colorResource(id = R.color.light_gray1)
    val grayText = colorResource(id = R.color.gray_Text)

    // Pure UI state for bottom sheet visibility
    var showLogoutSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightGray1)
    ) {
        // 1. Profile Header Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.dimen_40))
                .padding(start = 35.dp, end = 16.dp)
        ) {
            CircularImageView(
                resId = R.drawable.profile,
                borderWidth = 2.dp,
                borderColor = Color(0xFFEEE5FF),
                modifier = Modifier.size(dimensionResource(id = R.dimen.dimen_80))
            )

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = dimensionResource(id = R.dimen.dimen_20))
            ) {
                Text(
                    text = userProfile.email,
                    fontSize = 16.sp,
                    fontFamily = manropeRegular,
                    color = grayText
                )

                Text(
                    text = userProfile.name,
                    fontFamily = manropeMedium,
                    fontSize = 26.sp,
                    color = Color.Black
                )
            }

            // Edit Icon Button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .border(
                        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onEditProfileClick() }
                    .padding(5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit Profile"
                )
            }
        }

        // 2. Options Card (Account, Settings, Logout)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = dimensionResource(id = R.dimen.dimen_40))
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
        ) {
            ProfileMenuItem(
                iconRes = R.drawable.ic_account,
                iconContainerColor = Color(0xFFEEE5FF),
                title = "Account",
                fontFamily = manropeMedium,
                onClick = { onNavigateToDetail("account", "account") }
            )

            HorizontalDivider(
                thickness = dimensionResource(id = R.dimen.dimen_1),
                color = lightGray1
            )

            ProfileMenuItem(
                iconRes = R.drawable.ic_setting,
                iconContainerColor = Color(0xFFEEE5FF),
                title = "Settings",
                fontFamily = manropeMedium,
                onClick = { onNavigateToDetail("setting", "setting") }
            )

            HorizontalDivider(
                thickness = dimensionResource(id = R.dimen.dimen_1),
                color = lightGray1
            )

            ProfileMenuItem(
                iconRes = R.drawable.ic_logout,
                iconContainerColor = Color(0xFFFFE2E4),
                title = "Logout",
                fontFamily = manropeMedium,
                onClick = { showLogoutSheet = true }
            )

            if (showLogoutSheet) {
                LogoutBottomSheet(
                    onDismiss = { showLogoutSheet = false },
                    onConfirmLogout = {
                        showLogoutSheet = false
                        profileViewModel.logout(onLogoutSuccess)
                    }
                )
            }
        }
    }
}

@Composable
private fun ProfileMenuItem(
    @DrawableRes iconRes: Int,
    iconContainerColor: Color,
    title: String,
    fontFamily: FontFamily,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(iconContainerColor)
                .padding(dimensionResource(id = R.dimen.dimen_14))
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title
            )
        }

        Text(
            text = title,
            color = Color.Black,
            fontSize = 18.sp,
            fontFamily = fontFamily,
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.dimen_10))
        )
    }
}