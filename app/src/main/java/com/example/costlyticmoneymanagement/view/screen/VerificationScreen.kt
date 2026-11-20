package com.example.costlyticmoneymanagement.view.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.costlyticmoneymanagement.R
import kotlinx.coroutines.delay

@Composable
fun VerificationScreen(
    otp: String,
    userEmail: String = "pv84*****@gmail.com",
    onBackClick: () -> Unit,
    onVerificationSuccess: () -> Unit
) {
    val context = LocalContext.current

    // OTP & Resend States
    var currentExpectedOtp by remember { mutableStateOf(otp) }
    val otpDigits = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = remember { List(6) { FocusRequester() } }

    // 15-Second Countdown Timer State (matching VerificationActivity's 15000ms timer)
    var secondsRemaining by remember { mutableIntStateOf(15) }
    var timerTrigger by remember { mutableIntStateOf(0) }
    val resendEnabled = secondsRemaining == 0

    // Fonts & Colors
    val interMedium = FontFamily(Font(R.font.inter_medium))
    val interBold = FontFamily(Font(R.font.inter_bold))
    val blackText = colorResource(id = R.color.black_text)
    val purpleBlue = colorResource(id = R.color.purple_blue)

    // Run 15-second countdown timer whenever screen loads or resend triggers
    LaunchedEffect(timerTrigger) {
        secondsRemaining = 15
        while (secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining--
        }
    }

    // Request focus on the first OTP box initially
    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }

    fun verifyOtpCode(enteredOtp: String) {
        if (enteredOtp.length == 6) {
            if (enteredOtp == currentExpectedOtp) {
                Toast.makeText(context, "OTP Verified!", Toast.LENGTH_SHORT).show()
                onVerificationSuccess()
            } else {
                Toast.makeText(context, "Incorrect OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ) {
        // 1. Top Title Bar (Replaces backButton + titleSignUp)
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
                text = "Verification",
                fontFamily = interMedium,
                fontSize = 18.sp,
                color = blackText,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // 2. Main Heading ("Enter your Verification Code")
        Text(
            text = "Enter your Verification Code",
            fontFamily = interMedium,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            color = blackText,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
        )

        // 3. 6-Digit OTP Input Boxes (Replaces dottedIndicatorLayout & dotScreen1..6)
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(24.dp)
        ) {
            for (index in 0 until 6) {
                val digit = otpDigits[index]
                val isFilled = digit.isNotEmpty()

                BasicTextField(
                    value = digit,
                    onValueChange = { newValue ->
                        val filtered = newValue.filter { it.isDigit() }
                        when {
                            filtered.isEmpty() -> {
                                otpDigits[index] = ""
                            }
                            filtered.length == 1 -> {
                                otpDigits[index] = filtered
                                if (index < 5) {
                                    focusRequesters[index + 1].requestFocus()
                                }
                                val fullCode = otpDigits.joinToString("")
                                if (fullCode.length == 6) {
                                    verifyOtpCode(fullCode)
                                }
                            }
                            else -> {
                                // Handle pasted OTP across boxes
                                val chars = filtered.take(6 - index)
                                chars.forEachIndexed { offset, ch ->
                                    otpDigits[index + offset] = ch.toString()
                                }
                                val nextFocusIndex = (index + chars.length).coerceAtMost(5)
                                focusRequesters[nextFocusIndex].requestFocus()

                                val fullCode = otpDigits.joinToString("")
                                if (fullCode.length == 6) {
                                    verifyOtpCode(fullCode)
                                }
                            }
                        }
                    },
                    singleLine = true,
                    cursorBrush = SolidColor(purpleBlue),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(
                        fontFamily = interBold,
                        fontSize = 24.sp,
                        color = blackText,
                        textAlign = TextAlign.Center
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.dimen_40))
                                .clip(CircleShape)
                                .background(
                                    if (isFilled) Color.Transparent else Color(0xFFE0E2E9)
                                )
                                .then(
                                    if (isFilled) {
                                        Modifier.border(
                                            width = 1.5.dp,
                                            color = purpleBlue,
                                            shape = CircleShape
                                        )
                                    } else {
                                        Modifier
                                    }
                                )
                        ) {
                            innerTextField()
                        }
                    },
                    modifier = Modifier
                        .focusRequester(focusRequesters[index])
                        .onPreviewKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown &&
                                keyEvent.key == Key.Backspace &&
                                otpDigits[index].isEmpty() &&
                                index > 0
                            ) {
                                otpDigits[index - 1] = ""
                                focusRequesters[index - 1].requestFocus()
                                true
                            } else {
                                false
                            }
                        }
                )
            }
        }

        // 4. Countdown Timer Text (Replaces otpTime)
        val formattedTime = String.format("00:%02d", secondsRemaining)
        Text(
            text = formattedTime,
            fontFamily = interMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = purpleBlue,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.dimen_16))
        )

        // 5. Spannable Description Text (Replaces otpDescription)
        val descriptionAnnotated = buildAnnotatedString {
            withStyle(style = SpanStyle(color = blackText, fontWeight = FontWeight.Bold)) {
                append("We send verification code to your email ")
            }
            withStyle(style = SpanStyle(color = purpleBlue, fontWeight = FontWeight.Bold)) {
                append("$userEmail.")
            }
            withStyle(style = SpanStyle(color = blackText, fontWeight = FontWeight.Bold)) {
                append(" You can check your inbox.")
            }
        }

        Text(
            text = descriptionAnnotated,
            fontFamily = interMedium,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                .padding(bottom = dimensionResource(id = R.dimen.dimen_16))
        )

        // 6. Resend Code Text (Replaces codeSendTxt)
        val resendAnnotated = buildAnnotatedString {
            if (!resendEnabled) {
                withStyle(style = SpanStyle(color = blackText, fontWeight = FontWeight.Medium)) {
                    append("Resend available in ${secondsRemaining}s")
                }
            } else {
                withStyle(
                    style = SpanStyle(
                        color = purpleBlue,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("I didn’t receive the code? Send again.")
                }
            }
        }

        Text(
            text = resendAnnotated,
            fontFamily = interMedium,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                .padding(bottom = dimensionResource(id = R.dimen.dimen_45))
                .clickable(enabled = resendEnabled) {
                    val newOtp = (100000..999999).random().toString()
                    currentExpectedOtp = newOtp
                    otpDigits.indices.forEach { otpDigits[it] = "" }
                    focusRequesters[0].requestFocus()
                    Toast.makeText(context, "New OTP (UAT): $newOtp", Toast.LENGTH_SHORT).show()
                    timerTrigger++
                }
        )

        // 7. Bottom "Sign Up" / Verify Button (Replaces btnVerify)
        Button(
            onClick = {
                val fullCode = otpDigits.joinToString("")
                if (fullCode.length < 6) {
                    Toast.makeText(context, "Please enter 6-digit OTP", Toast.LENGTH_SHORT).show()
                } else {
                    verifyOtpCode(fullCode)
                }
            },
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
    }
}