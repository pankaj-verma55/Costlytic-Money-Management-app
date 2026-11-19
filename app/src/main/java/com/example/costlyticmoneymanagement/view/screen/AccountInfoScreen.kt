package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.costlyticmoneymanagement.R

@Composable
fun AccountInfoScreen(
    onBackClick: () -> Unit,
    onAddWalletClick: () -> Unit,
    wallets: List<Any> = emptyList()
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
        // Top Bar (Back Button + Centered "Account" Title)
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

        // Balance Amount (₹ 5000)
        Text(
            text = "₹ 5000",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 48.sp,
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontFamily = manropeBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .padding(top = 40.dp)
        )

        // Subtitle ("Buy some grocery")
        Text(
            text = "Buy some grocery",
            fontSize = 16.sp,
            color = Color.Black,
            fontFamily = manropeMedium,
            modifier = Modifier.padding(top = 10.dp)
        )

        // Wallets List Card Container (Replaces month_bg white LinearLayout + RecyclerView)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 30.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(wallets) { _ ->
                    // Ready for wallet_layout composable once wallet adapter/data is wired
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
            Text(
                text = "+ Add new wallet",
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}