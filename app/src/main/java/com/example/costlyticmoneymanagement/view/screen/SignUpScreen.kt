package com.example.costlyticmoneymanagement.view.screen

import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToVerification: (String) -> Unit,
    onGoogleSignUpClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }
    val db = remember { FirebaseFirestore.getInstance() }
    val databaseReference = remember {
        FirebaseDatabase.getInstance().getReference("table_User")
    }
    // Form States
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isTermsChecked by remember { mutableStateOf(false) }

    // Error States
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Fonts & Colors
    val interMedium = FontFamily(Font(R.font.inter_medium))
    val interBold = FontFamily(Font(R.font.inter_bold))
    val purpleBlue = colorResource(id = R.color.purple_blue)
    val blackText = colorResource(id = R.color.black_text)
    val hintText = colorResource(id = R.color.hint_text)
    val grayText = colorResource(id = R.color.gray_Text)

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun createUser(emailInput: String, passInput: String, nameInput: String, phoneInput: String) {
        // Step 1: Create the account in Firebase Auth
        auth.createUserWithEmailAndPassword(emailInput, passInput)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid

                    // Step 2: Create a Map of the extra details
                    val userProfile = hashMapOf(
                        "name" to nameInput,
                        "phone" to phoneInput,
                        "email" to emailInput
                    )

                    // Step 3: Save to Firestore using the UID as the Document ID
                    userId?.let { uid ->
                        databaseReference.child(userId).setValue(userProfile)
                        Log.d("Firebase--->", "User profile created for $userProfile")
                        db.collection("users").document(uid)
                            .set(userProfile)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    "Otp Send Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val otp = (100000..999999).random().toString()
                                Log.d("OTP", "Generated OTP: $otp")
                                Toast.makeText(context, "OTP (UAT): $otp", Toast.LENGTH_LONG).show()
                                Log.d("TAG", "User profile created for $uid")

                                onNavigateToVerification(otp)
                            }
                            .addOnFailureListener { e ->
                                Log.w("TAG", "Error writing document", e)
                            }
                    }
                } else {
                    // Handle Auth failure (e.g., password too short, email exists)
                    Toast.makeText(
                        context,
                        "Auth Failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
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
                    .clickable { onBackClick() }
            )

            Text(
                text = "Sign Up",
                fontFamily = interMedium,
                fontSize = 18.sp,
                color = blackText,
                textAlign = TextAlign.Center
            )
        }

        // 1. Name Input
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                .padding(top = dimensionResource(id = R.dimen.dimen_60))
        ) {
            TextField(
                value = name,
                onValueChange = {
                    name = it
                    if (nameError != null) nameError = null
                },
                placeholder = {
                    Text(
                        text = "Name",
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    autoCorrectEnabled = true
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (nameError != null) MaterialTheme.colorScheme.error else Color(0xFFF1F1FA)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
            )
            if (nameError != null) {
                Text(
                    text = nameError!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }
        }

        // 2. Email Input
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                .padding(top = dimensionResource(id = R.dimen.dimen_24))
        ) {
            TextField(
                value = email,
                onValueChange = {
                    email = it
                    if (emailError != null) emailError = null
                },
                placeholder = {
                    Text(
                        text = "Email",
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (emailError != null) MaterialTheme.colorScheme.error else Color(0xFFF1F1FA)
                        ),
                        shape = RoundedCornerShape(16.dp)
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

        // 3. Phone Number Input (maxLength = 10 preserved)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                .padding(top = dimensionResource(id = R.dimen.dimen_24))
        ) {
            TextField(
                value = phone,
                onValueChange = { input ->
                    if (input.length <= 10) {
                        phone = input
                        if (phoneError != null) phoneError = null
                    }
                },
                placeholder = {
                    Text(
                        text = "Phone Number",
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (phoneError != null) MaterialTheme.colorScheme.error else Color(0xFFF1F1FA)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
            )
            if (phoneError != null) {
                Text(
                    text = phoneError!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }
        }

        // 4. Password Input (TextWatcher auto-clear between 8 and 16 chars preserved)
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
                    if (password.length in 8..16) {
                        passwordError = null
                    }
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
                    fontFamily = interMedium,
                    fontSize = 16.sp,
                    color = blackText
                ),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
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
                                color = hintText,
                                style = Stroke(width = 1.8.dp.toPx())
                            )
                            drawCircle(
                                color = hintText,
                                radius = w * 0.14f,
                                center = center
                            )
                            if (!passwordVisible) {
                                drawLine(
                                    color = hintText,
                                    start = Offset(w * 0.15f, h * 0.85f),
                                    end = Offset(w * 0.85f, h * 0.15f),
                                    strokeWidth = 2.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            }
                        }
                    }
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
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (passwordError != null) MaterialTheme.colorScheme.error else Color(0xFFF1F1FA)
                        ),
                        shape = RoundedCornerShape(16.dp)
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

        // Terms & Conditions Checkbox + Styled Text
        val termsAnnotatedText = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontFamily = interMedium,
                    color = blackText
                )
            ) {
                append("By signing up, you agree to the ")
            }
            withStyle(
                style = SpanStyle(
                    fontFamily = interMedium,
                    color = purpleBlue
                )
            ) {
                append("Terms of Service \nand Privacy Policy")
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                .padding(top = dimensionResource(id = R.dimen.dimen_22))
        ) {
            Image(
                painter = painterResource(
                    id = if (isTermsChecked) R.drawable.ic_checked else R.drawable.ic_uncheckbox_purple
                ),
                contentDescription = "Accept Terms",
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.dimen_4))
                    .size(dimensionResource(id = R.dimen.dimen_24))
                    .clickable {
                        isTermsChecked = !isTermsChecked
                        Log.d("CheckboxClick", "Checkbox was clicked")
                    }
            )

            Text(
                text = termsAnnotatedText,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.dimen_14))
            )
        }

        // Sign Up Button (Enabled/Disabled & Tint controlled by isTermsChecked)
        Button(
            onClick = {
                if (name.isEmpty()) {
                    nameError = "Name is required"
                } else if (email.isEmpty()) {
                    emailError = "Email is required"
                } else if (!isValidEmail(email.trim())) {
                    emailError = "InValid email address"
                } else if (phone.isEmpty()) {
                    phoneError = "Phone number is required"
                } else if (phone.length != 10) {
                    phoneError = "Phone number must be 10 digits"
                } else if (password.isEmpty()) {
                    passwordError = "Password is required"
                } else if (password.length !in 8..16) {
                    passwordError = "Password must be at least 8 to 16 characters long"
                } else {
                    createUser(email, password, name, phone)
                }
            },
            enabled = isTermsChecked,
            colors = ButtonDefaults.buttonColors(
                containerColor = purpleBlue,
                contentColor = Color.White,
                disabledContainerColor = grayText,
                disabledContentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.dimen_16)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = dimensionResource(id = R.dimen.dimen_27), bottom = 20.dp)
        ) {
            Text(
                text = "Sign Up",
                fontFamily = interBold,
                fontSize = 18.sp,
                color = Color.White
            )
        }

        // "or with" Text
        Text(
            text = "or with",
            fontFamily = interBold,
            fontSize = 14.sp,
            color = grayText,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = dimensionResource(id = R.dimen.dimen_12))
        )

        // Sign Up with Google Button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = dimensionResource(id = R.dimen.dimen_12))
                .border(
                    border = BorderStroke(1.dp, Color(0xFFF1F1FA)),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onGoogleSignUpClick() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google Icon",
                modifier = Modifier
                    .padding(end = 5.dp)
                    .padding(5.dp)
            )

            Text(
                text = "Sign Up with Google",
                fontFamily = interBold,
                fontSize = 18.sp,
                color = blackText,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        // "Already have an account? Login" Text
        val loginAnnotatedText = buildAnnotatedString {
            append("Already have an account? ")
            withStyle(
                style = SpanStyle(
                    color = purpleBlue,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Login")
            }
        }

        Text(
            text = loginAnnotatedText,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = dimensionResource(id = R.dimen.dimen_20), bottom = 24.dp)
                .clickable { onLoginClick() }
        )
    }
}