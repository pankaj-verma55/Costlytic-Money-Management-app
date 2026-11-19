package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.costlyticmoneymanagement.R

@Composable
fun ForgetPasswordScreen(
    onBackClick: () -> Unit,
    onContinueClick: (newPassword: String, retypePassword: String) -> Unit = { _, _ -> }
) {
    // Form States
    var newPassword by remember { mutableStateOf("") }
    var retypePassword by remember { mutableStateOf("") }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var retypePasswordVisible by remember { mutableStateOf(false) }

    // Fonts & Colors
    val interMedium = FontFamily(Font(R.font.inter_medium))
    val interBold = FontFamily(Font(R.font.inter_bold))
    val blackText = colorResource(id = R.color.black_text)
    val hintText = colorResource(id = R.color.hint_text)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ) {
        // Top Bar (Back Button + Centered "Forget Password" Title)
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
                text = "Forget Password",
                fontFamily = interMedium,
                fontSize = 18.sp,
                color = blackText,
                textAlign = TextAlign.Center
            )
        }

        // 1. New Password Input
        TextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            placeholder = {
                Text(
                    text = "New Password",
                    fontFamily = interMedium,
                    fontSize = 16.sp,
                    color = hintText
                )
            },
            textStyle = TextStyle(
                fontFamily = interMedium,
                fontSize = 16.sp,
                color = blackText
            ),
            singleLine = true,
            visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                PasswordVisibilityToggle(
                    isVisible = newPasswordVisible,
                    tint = hintText,
                    onToggle = { newPasswordVisible = !newPasswordVisible }
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
                .padding(top = dimensionResource(id = R.dimen.dimen_60))
                .border(
                    border = BorderStroke(1.dp, Color(0xFFF1F1FA)),
                    shape = RoundedCornerShape(16.dp)
                )
        )

        // 2. Retype New Password Input
        TextField(
            value = retypePassword,
            onValueChange = { retypePassword = it },
            placeholder = {
                Text(
                    text = "Retype new Password",
                    fontFamily = interMedium,
                    fontSize = 16.sp,
                    color = hintText
                )
            },
            textStyle = TextStyle(
                fontFamily = interMedium,
                fontSize = 16.sp,
                color = blackText
            ),
            singleLine = true,
            visualTransformation = if (retypePasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                PasswordVisibilityToggle(
                    isVisible = retypePasswordVisible,
                    tint = hintText,
                    onToggle = { retypePasswordVisible = !retypePasswordVisible }
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

        // Continue Button
        Button(
            onClick = { onContinueClick(newPassword, retypePassword) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7F3DFF),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = dimensionResource(id = R.dimen.dimen_27))
        ) {
            Text(
                text = "Continue",
                fontFamily = interBold,
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun PasswordVisibilityToggle(
    isVisible: Boolean,
    tint: Color,
    onToggle: () -> Unit
) {
    IconButton(onClick = onToggle) {
        Canvas(modifier = Modifier.size(22.dp)) {
            val w = size.width
            val h = size.height
            val eyePath = Path().apply {
                moveTo(w * 0.08f, h * 0.5f)
                quadraticTo(w * 0.5f, h * 0.1f, w * 0.92f, h * 0.5f)
                quadraticTo(w * 0.5f, h * 0.9f, w * 0.08f, h * 0.5f)
                close()
            }
            drawPath(
                path = eyePath,
                color = tint,
                style = Stroke(width = 1.8.dp.toPx())
            )
            drawCircle(
                color = tint,
                radius = w * 0.14f,
                center = center
            )
            if (!isVisible) {
                drawLine(
                    color = tint,
                    start = Offset(w * 0.15f, h * 0.85f),
                    end = Offset(w * 0.85f, h * 0.15f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}