package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.costlyticmoneymanagement.R


@Composable
fun MoneyTransactionContent(
    title: String,
    backgroundColor: Color,
    onBackClick: () -> Unit,
    onContinueClick: (amount: String, category: String, description: String, wallet: String, isRepeat: Boolean) -> Unit
) {
    // Form States (matching fragment_expense.xml & fragment_income.xml)
    var amount by remember { mutableStateOf("₹0") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var wallet by remember { mutableStateOf("") }
    var isRepeat by remember { mutableStateOf(false) }

    // Fonts & Colors
    val interMedium = FontFamily(Font(R.font.inter_medium))
    val manropeSemiBold = FontFamily(Font(R.font.manrope_semibold))
    val manropeBold = FontFamily(Font(R.font.manrope_bold))

    val hintText = colorResource(id = R.color.hint_text)
    val grayText = colorResource(id = R.color.gray_Text)
    val darkBlack = colorResource(id = R.color.dark_Black)
    val purpleBlue = colorResource(id = R.color.purple_blue)
    val lightWhite = colorResource(id = R.color.light_white)
    val lightPurpleBlue2 = colorResource(id = R.color.light_purple_blue2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .systemBarsPadding()
    ) {
        // 1. Top Title Bar (Replaces title_back_layout with white title & white-tinted back icon)
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
                text = title,
                fontFamily = interMedium,
                fontSize = 18.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        // 2. "How much?" Label & Large ₹0 Amount Input
        Text(
            text = "How much?",
            fontFamily = manropeSemiBold,
            fontSize = 20.sp,
            color = Color(0xFFFCFCFC),
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.dimen_26))
                .padding(top = dimensionResource(id = R.dimen.dimen_55))
        )

        BasicTextField(
            value = amount,
            onValueChange = { amount = it },
            singleLine = true,
            cursorBrush = SolidColor(Color.White),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(
                fontFamily = manropeSemiBold,
                fontSize = 64.sp,
                color = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
        )

        // 3. Curved White Bottom Container (Replaces curvedLayout with money_curve_bg)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            // Category Input
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

            // Description Input
            TextField(
                value = description,
                onValueChange = { description = it },
                placeholder = {
                    Text(
                        text = "Description",
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

            // Wallet Input
            TextField(
                value = wallet,
                onValueChange = { wallet = it },
                placeholder = {
                    Text(
                        text = "Wallet",
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
                        contentDescription = "Select Wallet"
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

            // Add Attachment Dashed Box (Replaces fileLayout with upload_file_doted_bg)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                    .padding(top = dimensionResource(id = R.dimen.dimen_16))
                    .drawBehind {
                        drawRoundRect(
                            color = Color(0xFFF1F1FA),
                            style = Stroke(
                                width = 1.5.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                            ),
                            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
                        )
                    }
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { /* Attachment picker hook */ }
                    .padding(vertical = dimensionResource(id = R.dimen.dimen_16))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_file_attach),
                    contentDescription = "Attach File"
                )
                Text(
                    text = "Add attachment",
                    fontSize = 18.sp,
                    color = grayText,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.dimen_10))
                )
            }

            // Repeat Transaction Row + Switch (Replaces repeatLayout)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.dimen_16))
                    .padding(
                        start = dimensionResource(id = R.dimen.dimen_32),
                        end = dimensionResource(id = R.dimen.dimen_16)
                    )
            ) {
                Column {
                    Text(
                        text = "Repeat",
                        color = darkBlack,
                        fontSize = 18.sp,
                        fontFamily = manropeBold
                    )
                    Text(
                        text = "Repeat transaction",
                        color = grayText,
                        fontSize = 18.sp,
                        fontFamily = manropeBold
                    )
                }

                Switch(
                    checked = isRepeat,
                    onCheckedChange = { isRepeat = it },
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

            Spacer(modifier = Modifier.weight(1f))

            // Continue Button
            Button(
                onClick = { onContinueClick(amount, category, description, wallet, isRepeat) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                    .padding(
                        top = dimensionResource(id = R.dimen.dimen_24),
                        bottom = dimensionResource(id = R.dimen.dimen_15)
                    )
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