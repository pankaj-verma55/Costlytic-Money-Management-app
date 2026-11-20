package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.costlyticmoneymanagement.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgetPasswordClick: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    // Form States
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()

    // Error States
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Fonts & Colors
    val interMedium = FontFamily(Font(R.font.inter_medium))
    val interBold = FontFamily(Font(R.font.inter_bold))
    val purpleBlue = colorResource(id = R.color.purple_blue)
    val blackText = colorResource(id = R.color.black_text)
    val hintText = colorResource(id = R.color.hint_text)

    var isLoading by remember { mutableStateOf(false) }

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun loginUser(
        emailInput: String,
        passInput: String,
        onEmailError: (String) -> Unit,
        onPasswordError: (String) -> Unit
    ) {
        isLoading = true
        val timeoutJob = coroutineScope.launch {
            kotlinx.coroutines.delay(15_000L)
            if (isLoading) {
                isLoading = false
                onPasswordError("Network timeout. Check your internet connection and try again.")
            }
        }
        auth.signInWithEmailAndPassword(emailInput, passInput).addOnSuccessListener {
            timeoutJob.cancel()
            isLoading = false
                Toast.makeText(context, "Welcome back!", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
            }.addOnFailureListener { exception ->
                timeoutJob.cancel()
            isLoading = false
                when (exception) {
                    is FirebaseAuthInvalidUserException -> {
                        onEmailError("Email is not registered")
                    }

                    is FirebaseAuthInvalidCredentialsException -> {
                        onPasswordError("Incorrect password")
                    }

                    else -> {
                        onPasswordError(exception.localizedMessage ?: "Authentication failed")
                    }
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ) {
        // Top Bar
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
                    .clickable { onBackClick() })

            Text(
                text = "Login",
                fontFamily = interMedium,
                fontSize = 18.sp,
                color = blackText,
                textAlign = TextAlign.Center
            )
        }

        // Email Input
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                .padding(top = dimensionResource(id = R.dimen.dimen_72))
        ) {
            TextField(
                value = email, onValueChange = {
                email = it
                if (emailError != null) emailError = null
            }, placeholder = {
                Text(
                    text = "Email", fontFamily = interMedium, fontSize = 16.sp, color = hintText
                )
            }, textStyle = TextStyle(
                fontFamily = interMedium, fontSize = 16.sp, color = blackText
            ), singleLine = true, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email, autoCorrectEnabled = true
            ), colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ), shape = RoundedCornerShape(16.dp), modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (emailError != null) MaterialTheme.colorScheme.error else Color(
                                0xFFF1F1FA
                            )
                        ), shape = RoundedCornerShape(16.dp)
                    )
            )

            if (emailError != null) {
                Text(
                    text = emailError!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }
        }

        // Password Input
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                .padding(top = dimensionResource(id = R.dimen.dimen_24))
        ) {
            TextField(
                value = password,
                onValueChange = {
                    password = it
                    if (passwordError != null) passwordError = null
                },
                placeholder = {
                    Text(
                        text = "Password",
                        fontFamily = interMedium,
                        fontSize = 16.sp,
                        color = hintText
                    )
                },
                textStyle = TextStyle(
                    fontFamily = interMedium, fontSize = 16.sp, color = blackText
                ),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = hintText
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (passwordError != null) MaterialTheme.colorScheme.error else Color(
                                0xFFF1F1FA
                            )
                        ), shape = RoundedCornerShape(16.dp)
                    )
            )

            if (passwordError != null) {
                Text(
                    text = passwordError!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }
        }

        // Login Button
        Button(
            enabled = !isLoading,
            onClick = {
                emailError = null
                passwordError = null
                val trimmedEmail = email.trim()

                if (trimmedEmail.isEmpty()) {
                    emailError = "Email is required"
                } else if (!isValidEmail(trimmedEmail.trim())) {
                    emailError = "InValid email address"
                } else if (password.isEmpty()) {
                    passwordError = "Password is required"
                } else if (password.length !in 8..16) {
                    passwordError = "Password must be at least 8 to 16 characters long"
                } else {
                    loginUser(
                        emailInput = trimmedEmail,
                        passInput = password,
                        onEmailError = { msg -> emailError = msg },
                        onPasswordError = { msg -> passwordError = msg })
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7F3DFF), contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = dimensionResource(id = R.dimen.dimen_27), bottom = 20.dp)
        ) {

            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Text(
                    text = "Login",
                    fontFamily = interBold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
//            Text(
//                text = "Login", fontFamily = interBold, fontSize = 18.sp, color = Color.White
//            )
        }

        // Forget Password Text
        Text(
            text = "Forget Password?",
            fontFamily = interBold,
            fontSize = 18.sp,
            color = purpleBlue,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = dimensionResource(id = R.dimen.dimen_33))
                .clickable { onForgetPasswordClick() })

        // Sign Up Spannable Text
        val signUpAnnotatedText = buildAnnotatedString {
            append("Don’t have an account yet? ")
            withStyle(
                style = SpanStyle(
                    color = purpleBlue,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Sign Up")
            }
        }

        Text(
            text = signUpAnnotatedText,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = dimensionResource(id = R.dimen.dimen_20))
                .clickable { onSignUpClick() })
    }
}