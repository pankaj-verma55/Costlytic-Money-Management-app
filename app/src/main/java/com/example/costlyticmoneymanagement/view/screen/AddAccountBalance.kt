package com.example.costlyticmoneymanagement.view.screen

import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.costlyticmoneymanagement.R
import com.example.costlyticmoneymanagement.view.navigation.Screen
import com.example.costlyticmoneymanagement.viewmodel.WalletViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountBalance(
    onBackClick: () -> Unit,
    walletViewModel: WalletViewModel = viewModel()
) {
    val context = LocalContext.current
    val bankList by walletViewModel.banks.collectAsState()
    val isSaving = walletViewModel.isSavingWallet
    val errorMessage = walletViewModel.walletError

    // Form States
    var rawBalance by remember { mutableStateOf("00.00") }
    var walletName by remember { mutableStateOf("") }
    var selectedAccountType by remember { mutableStateOf("Bank") }
    var isAccountTypeExpanded by remember { mutableStateOf(false) }
    var selectedBank by remember { mutableStateOf("") }

    val accountTypes = remember { listOf("Bank", "Wallet", "Cash", "Credit Card", "UPI") }

    // Fonts & Colors
    val interMedium = FontFamily(Font(R.font.inter_medium))
    val interSemiBold = FontFamily(Font(R.font.man_rope_semi_bold))
    val manropeMedium = FontFamily(Font(R.font.manrope_medium))
    val manropeBold = FontFamily(Font(R.font.manrope_bold))
    val purpleBlue = colorResource(id = R.color.purple_blue)

    // Show validation / Firebase errors
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            walletViewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(purpleBlue)
            .statusBarsPadding()
    ) {
        // 1. Top Bar
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.dimen_16))
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_left_arrow),
                contentDescription = "Back",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { onBackClick() }
                    .padding(4.dp)
            )

            Text(
                text = "Add new wallet",
                fontFamily = interSemiBold,
                fontSize = 18.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // 2. Balance Section (on Purple Header)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Balance",
                fontFamily = interMedium,
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.75f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "₹",
                    fontFamily = manropeBold,
                    fontSize = 56.sp,
                    color = Color.White
                )

                BasicTextField(
                    value = rawBalance,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() || it == '.' }) {
                            rawBalance = input
                        }
                    },
                    singleLine = true,
                    cursorBrush = SolidColor(Color.White),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    textStyle = TextStyle(
                        fontFamily = manropeBold,
                        fontSize = 56.sp,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp)
                )
            }
        }

        // 3. White Bottom Form Sheet
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            // Name Input
            OutlinedTextField(
                value = walletName,
                onValueChange = { walletName = it },
                placeholder = {
                    Text(
                        text = "Name",
                        fontFamily = manropeMedium,
                        color = Color(0xFF91919F)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = purpleBlue,
                    unfocusedBorderColor = Color(0xFFF1F1FA),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Account Type Dropdown
            ExposedDropdownMenuBox(
                expanded = isAccountTypeExpanded,
                onExpandedChange = { isAccountTypeExpanded = !isAccountTypeExpanded }
            ) {
                OutlinedTextField(
                    value = selectedAccountType,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isAccountTypeExpanded)
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = purpleBlue,
                        unfocusedBorderColor = Color(0xFFF1F1FA),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = isAccountTypeExpanded,
                    onDismissRequest = { isAccountTypeExpanded = false },
                    containerColor = Color.White
                ) {
                    accountTypes.forEach { typeOption ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = typeOption,
                                    fontFamily = manropeMedium,
                                    color = Color.Black
                                )
                            },
                            onClick = {
                                selectedAccountType = typeOption
                                isAccountTypeExpanded = false
                            }
                        )
                    }
                }
            }

            // Bank Grid Section (Visible when Account Type is "Bank")
            if (selectedAccountType.equals("Bank", ignoreCase = true)) {
                Text(
                    text = "Bank",
                    fontFamily = interSemiBold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 20.dp, bottom = 12.dp)
                )

                val chunkedBanks = bankList.chunked(4)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    chunkedBanks.forEach { rowBanks ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            rowBanks.forEach { bankName ->
                                val isSelected = selectedBank == bankName
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (isSelected) Color(0xFFEEE5FF)
                                            else Color(0xFFF1F1FA)
                                        )
                                        .border(
                                            border = BorderStroke(
                                                width = if (isSelected) 1.5.dp else 0.dp,
                                                color = if (isSelected) purpleBlue else Color.Transparent
                                            ),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable {
                                            selectedBank = bankName
                                            if (walletName.isBlank() && bankName != "See Other") {
                                                walletName = bankName
                                            }
                                        }
                                        .padding(horizontal = 4.dp)
                                ) {
                                    Text(
                                        text = bankName,
                                        fontFamily = manropeBold,
                                        fontSize = 11.sp,
                                        maxLines = 1,
                                        textAlign = TextAlign.Center,
                                        color = if (isSelected) purpleBlue else Color.Black
                                    )
                                }
                            }
                            // Fill empty slots if last row has < 4 items
                            repeat(4 - rowBanks.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Continue Button
            Button(
                enabled = !isSaving,
                onClick = {
                    val finalWalletName = when {
                        walletName.isNotBlank() -> walletName
                        selectedBank.isNotBlank() && selectedBank != "See Other" -> selectedBank
                        else -> ""
                    }

                    walletViewModel.createWallet(
                        name = finalWalletName,
                        type = if (selectedBank.isNotBlank() && selectedBank != "See Other") {
                            "$selectedAccountType • $selectedBank"
                        } else {
                            selectedAccountType
                        },
                        rawBalance = rawBalance,
                        onSuccess = {
                            Toast.makeText(context, "Wallet added!", Toast.LENGTH_SHORT).show()
                            onBackClick()
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text(
                        text = "Continue",
                        fontFamily = interSemiBold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}