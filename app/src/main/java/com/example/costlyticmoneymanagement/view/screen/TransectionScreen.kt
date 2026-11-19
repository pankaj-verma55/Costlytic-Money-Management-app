package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun TransferScreen(
    onBackToDashboard: () -> Unit,
    onContinueClick: (amount: String, category: String, description: String) -> Unit = { _, _, _ -> }
) {
    var amount by remember { mutableStateOf("₹0") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val interMedium = FontFamily(Font(R.font.inter_medium))
    val manropeSemiBold = FontFamily(Font(R.font.manrope_semibold))
    val skyBlueDark = colorResource(id = R.color.skyBlueDark)
    val lightWhite = colorResource(id = R.color.light_white)
    val hintText = colorResource(id = R.color.hint_text)
    val grayText = colorResource(id = R.color.gray_Text)
    val darkBlack = colorResource(id = R.color.dark_Black)
    val purpleBlue = colorResource(id = R.color.purple_blue)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(skyBlueDark)
//            .systemBarsPadding()
    ) {
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
                    .clickable { onBackToDashboard() }
            )

            Text(
                text = "Transfer",
                fontFamily = interMedium,
                fontSize = 18.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "How much?",
            fontFamily = manropeSemiBold,
            fontSize = 20.sp,
            color = lightWhite,
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.dimen_26))
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
                .padding(
                    start = 25.dp,
                    end = 16.dp,
                    bottom = dimensionResource(id = R.dimen.dimen_18)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
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

            // Continue Button
            Button(
                onClick = { onContinueClick(amount, category, description) },
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
                        top = dimensionResource(id = R.dimen.dimen_40),
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