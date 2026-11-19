package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.costlyticmoneymanagement.R


@Composable
fun MoneySpendPage(
    redColor: Color,
    manropeBold: FontFamily,
    manropeExtraBold: FontFamily
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(redColor)
    ) {
        Text(
            text = "This Month",
            color = Color.White,
            fontSize = 26.sp,
            fontFamily = manropeBold,
            modifier = Modifier.padding(top = 100.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

//        CrossBadge()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.dimen_10))
        ) {
            Text(
                text = "You Spend",
                color = Color.White,
                fontFamily = manropeExtraBold,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
//            Image(
//                painter = painterResource(id = R.drawable.ic_cross_bg),
//                contentDescription = null
//            )
        }

        Text(
            text = "₹ 120",
            color = Color.White,
            fontSize = 64.sp,
            fontFamily = manropeExtraBold
        )

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Rounded Summary Card
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = dimensionResource(id = R.dimen.dimen_20))
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
        ) {
            Text(
                text = "You are the biggest\n spending in from",
                fontSize = 24.sp,
                color = Color.Black,
                fontFamily = manropeBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Category Pill (Shopping)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.dimen_14))
                    .border(
                        border = BorderStroke(1.dp, Color(0xFFF1F1FA)),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(dimensionResource(id = R.dimen.dimen_16))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_shopping),
                    contentDescription = "Shopping",
                    modifier = Modifier.size(dimensionResource(id = R.dimen.dimen_32))
                )
                Text(
                    text = "Shopping",
                    fontFamily = manropeBold,
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.dimen_10))
                )
            }

            Text(
                text = "₹ 120",
                fontSize = 36.sp,
                color = Color.Black,
                fontFamily = manropeBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp, bottom = dimensionResource(id = R.dimen.dimen_28))
            )
        }
    }
}




@Composable
private fun CrossBadge() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.24f))
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
    }
}