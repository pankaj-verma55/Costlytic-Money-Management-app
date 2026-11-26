package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.costlyticmoneymanagement.R
import com.example.costlyticmoneymanagement.model.data.WalletItem
import com.example.costlyticmoneymanagement.viewmodel.WalletViewModel

@Composable
fun AccountInfoScreen(
    onBackClick: () -> Unit,
    onAddWalletClick: () -> Unit,
    walletViewModel: WalletViewModel = viewModel()
) {
    val uiState by walletViewModel.uiState.collectAsState()

    AccountInfoContent(
        totalBalance = uiState.totalBalance,
        wallets = uiState.wallets,
        onBackClick = onBackClick,
        onAddWalletClick = onAddWalletClick
    )
}

@Composable
fun AccountInfoContent(
    totalBalance: Double,
    wallets: List<WalletItem>,
    onBackClick: () -> Unit,
    onAddWalletClick: () -> Unit
) {
    val interMedium = FontFamily(Font(R.font.inter_medium))
    val manropeBold = FontFamily(Font(R.font.manrope_bold))
    val manropeMedium = FontFamily(Font(R.font.manrope_medium))
    val purpleBlue = colorResource(id = R.color.purple_blue)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFCFC))
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
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
                text = "Account",
                fontFamily = interMedium,
                fontSize = 18.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }

        // Subtitle
        Text(
            text = "Account Balance",
            fontSize = 16.sp,
            color = Color(0xFF91919F),
            fontFamily = manropeMedium,
            modifier = Modifier.padding(top = 10.dp)
        )

        // Live Total Balance across all Wallets
        Text(
            text = "₹ ${totalBalance.toInt()}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 48.sp,
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontFamily = manropeBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .padding(top = 12.dp)
        )

        // Wallets List Container
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 30.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
        ) {
            if (wallets.isEmpty()) {
                Text(
                    text = "No wallets added yet.\nTap '+ Add new wallet' below.",
                    fontFamily = manropeMedium,
                    fontSize = 15.sp,
                    color = Color(0xFF91919F),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(wallets, key = { _, item -> item.id }) { index, wallet ->
                        WalletRowItem(
                            wallet = wallet,
                            manropeBold = manropeBold,
                            manropeMedium = manropeMedium
                        )
                        if (index < wallets.lastIndex) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color(0xFFF1F1FA),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }

        // "+ Add new wallet" Button
        Button(
            onClick = onAddWalletClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = purpleBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(vertical = 18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                .padding(bottom = dimensionResource(id = R.dimen.dimen_16))
        ) {
            Text(modifier = Modifier,
                text = "+ Add new wallet",
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun WalletRowItem(
    wallet: WalletItem,
    manropeBold: FontFamily,
    manropeMedium: FontFamily
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFEEE5FF))
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_account),
                contentDescription = wallet.name,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = wallet.name,
                fontFamily = manropeBold,
                fontSize = 18.sp,
                color = Color.Black
            )
            if (wallet.type.isNotBlank()) {
                Text(
                    text = wallet.type,
                    fontFamily = manropeMedium,
                    fontSize = 13.sp,
                    color = Color(0xFF91919F)
                )
            }
        }

        Text(
            text = "₹${wallet.balance.toInt()}",
            fontFamily = manropeBold,
            fontSize = 18.sp,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountInfoScreenPreview() {
    AccountInfoContent(
        totalBalance = 15000.0,
        wallets = listOf(
            WalletItem(id = "1", name = "HDFC Bank", type = "Bank", balance = 12000.0),
            WalletItem(id = "2", name = "Cash Wallet", type = "Cash", balance = 3000.0)
        ),
        onBackClick = {},
        onAddWalletClick = {}
    )
}