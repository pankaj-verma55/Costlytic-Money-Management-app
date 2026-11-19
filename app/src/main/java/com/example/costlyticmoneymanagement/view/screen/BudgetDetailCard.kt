package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.costlyticmoneymanagement.R
import com.example.costlyticmoneymanagement.data.BudgetItem
import com.example.costlyticmoneymanagement.view.uicomponent.RoundedProgressBar

@Composable
fun BudgetDetailCard(
    item: BudgetItem,
    manropeRegular: FontFamily,
    manropeMedium: FontFamily,
    manropeBold: FontFamily,
    grayText: Color,
    redColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.dimen_16))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(
                border = BorderStroke(1.dp, Color(0xFFF1F1FA)),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(dimensionResource(id = R.dimen.dimen_16))
    ) {
        // Top Row: Category Pill + Warning Icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .border(
                        border = BorderStroke(1.dp, Color(0xFFF1F1FA)),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(vertical = 8.dp, horizontal = dimensionResource(id = R.dimen.dimen_12))
            ) {
                Box(
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.dimen_16))
                        .clip(CircleShape)
                        .background(item.categoryColor)
                )
                Text(
                    text = item.category,
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontFamily = manropeMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            if (item.isExceeded) {
                Image(
                    painter = painterResource(id = R.drawable.ic_warning),
                    contentDescription = "Limit Exceeded"
                )
            }
        }

        // Remaining Price
        Text(
            text = "Remaining ${item.remainingAmount}",
            fontFamily = manropeBold,
            fontSize = 26.sp,
            color = Color.Black,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Rounded Progress Bar
        RoundedProgressBar(
            progress = item.progress,
            progressColor = item.categoryColor,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.dimen_20))
                .padding(top = dimensionResource(id = R.dimen.dimen_8))
        )

        // Spent of Total Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.dimen_8))
        ) {
            Text(
                text = item.spentAmount,
                color = grayText,
                fontSize = 18.sp,
                fontFamily = manropeMedium
            )
            Text(
                text = "of",
                color = grayText,
                fontSize = 18.sp,
                fontFamily = manropeMedium,
                modifier = Modifier.padding(start = 5.dp)
            )
            Text(
                text = item.totalAmount,
                color = grayText,
                fontSize = 18.sp,
                fontFamily = manropeMedium,
                modifier = Modifier.padding(start = 5.dp)
            )
        }

        // Exceeded Warning Text
        if (item.isExceeded) {
            Text(
                text = "You’ve exceed the limit!",
                color = redColor,
                fontSize = 18.sp,
                fontFamily = manropeRegular,
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dimen_8))
            )
        }
    }
}