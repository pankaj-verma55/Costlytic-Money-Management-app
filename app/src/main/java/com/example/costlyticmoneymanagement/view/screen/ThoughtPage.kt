package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.costlyticmoneymanagement.R

@Composable
fun ThoughtPage(
    purpleBlue: Color,
    manropeExtraBold: FontFamily,
    manropeSemiBold: FontFamily,
    onSeeDetailClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(purpleBlue)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 200.dp)
                .padding(horizontal = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "“Financial freedom \n freedom from fear.”",
                    fontFamily = manropeExtraBold,
                    color = Color.White,
                    fontSize = 32.sp,
                    lineHeight = 40.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
//                Image(
//                    painter = painterResource(id = R.drawable.ic_cross_bg),
//                    contentDescription = null
//                )
            }

            Text(
                text = "-Pankaj Verma",
                color = Color.White,
                fontSize = 24.sp,
                fontFamily = manropeExtraBold,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onSeeDetailClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = purpleBlue
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "See the full Detial",
                color = purpleBlue,
                fontSize = 18.sp,
                fontFamily = manropeSemiBold
            )
        }
    }
}