package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.costlyticmoneymanagement.R
import com.example.costlyticmoneymanagement.view.uicomponent.ScrollProgressView

@Composable
fun CreateBudgetScreen(
    onBackClick: () -> Unit,
    onContinueClick: (category: String, receiveAlert: Boolean, alertPercentage: Float) -> Unit = { _, _, _ ->
        onBackClick()
    }
) {
    var category by remember { mutableStateOf("") }
    var receiveAlert by remember { mutableStateOf(false) }
    var alertPercentage by remember { mutableFloatStateOf(80f) }

    // Fonts & Colors
    val interMedium = FontFamily(Font(R.font.inter_medium))
    val manropeBold = FontFamily(Font(R.font.manrope_bold))

    val purpleBlue = colorResource(id = R.color.purple_blue)
    val hintText = colorResource(id = R.color.hint_text)
    val darkBlack = colorResource(id = R.color.dark_Black)
    val grayText = colorResource(id = R.color.gray_Text)
    val lightWhite = colorResource(id = R.color.light_white)
    val lightPurpleBlue2 = colorResource(id = R.color.light_purple_blue2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(purpleBlue)
            .systemBarsPadding()
    ) {
        // 1. Top Title Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.dimen_16)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_left_arrow),
                contentDescription = "Back",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { onBackClick() }
            )

            Text(
                text = "Create Budget",
                fontFamily = interMedium,
                fontSize = 18.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // 2. Bottom White Curved Container (Replaces curvedLayout with money_curve_bg)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White)
        ) {
            // Category Field
            TextField(
                value = category,
                onValueChange = { category = it },
                placeholder = {
                    Text(
                        text = "Category",
                        fontFamily = interMedium,
                        fontSize = 16.sp,
                        color = hintText
                    )
                },
                textStyle = TextStyle(
                    fontFamily = interMedium,
                    fontSize = 16.sp,
                    color = darkBlack
                ),
                singleLine = true,
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.down_arrow_gray),
                        contentDescription = "Select Category"
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                    .padding(top = dimensionResource(id = R.dimen.dimen_24))
                    .border(
                        border = BorderStroke(1.dp, Color(0xFFF1F1FA)),
                        shape = RoundedCornerShape(16.dp)
                    )
            )

            // "Recive Alert" Toggle Row (Replaces repeatLayout + switchIncome)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.dimen_16))
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                ) {
                    Text(
                        text = "Recive Alert",
                        color = darkBlack,
                        fontSize = 18.sp,
                        fontFamily = manropeBold
                    )
                    Text(
                        text = "Receive alert when it reaches some point.",
                        color = grayText,
                        fontSize = 18.sp,
                        fontFamily = manropeBold
                    )
                }

                Switch(
                    checked = receiveAlert,
                    onCheckedChange = { receiveAlert = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = lightWhite,
                        uncheckedThumbColor = lightWhite,
                        checkedTrackColor = purpleBlue,
                        uncheckedTrackColor = lightPurpleBlue2,
                        uncheckedBorderColor = Color.Transparent,
                        checkedBorderColor = Color.Transparent
                    )
                )
            }

            // Conditional ScrollProgressView Slider (visible only when receiveAlert is true)
            if (receiveAlert) {
                ScrollProgressView(
                    progress = alertPercentage,
                    onProgressChanged = { alertPercentage = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(horizontal = 8.dp)
                        .padding(top = dimensionResource(id = R.dimen.dimen_16))
                )
            }

            // Continue Button
            Button(
                onClick = {
                    onContinueClick(category, receiveAlert, alertPercentage)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                    .padding(top = dimensionResource(id = R.dimen.dimen_16), bottom = 32.dp)
            ) {
                Text(
                    text = "Continue",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
}