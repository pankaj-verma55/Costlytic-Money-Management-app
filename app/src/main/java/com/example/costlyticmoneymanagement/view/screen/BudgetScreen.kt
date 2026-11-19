package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.example.costlyticmoneymanagement.data.BudgetItem
import com.example.costlyticmoneymanagement.view.uicomponent.RoundedProgressBar


@Composable
fun BudgetScreen(
    budgets: List<BudgetItem> = emptyList(),
    onNavigateToCreateBudget: () -> Unit,
    onBackClick: (() -> Unit)? = null
) {
    val months = remember {
        listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
    }
    // Defaults to "May" (index 4) as in fragment_budget.xml
    var monthIndex by remember { mutableIntStateOf(4) }

    // Fonts & Colors
    val interMedium = FontFamily(Font(R.font.inter_medium))
    val manropeRegular = FontFamily(Font(R.font.manrope_regular))
    val manropeMedium = FontFamily(Font(R.font.manrope_medium))
    val manropeBold = FontFamily(Font(R.font.manrope_bold))

    val purpleBlue = colorResource(id = R.color.purple_blue)
    val lightWhite = colorResource(id = R.color.light_white)
    val grayText = colorResource(id = R.color.gray_Text)
    val redColor = colorResource(id = R.color.red)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(purpleBlue)
            .systemBarsPadding()
    ) {
        // 1. Top Title Bar (Optional back button if opened outside bottom nav)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.dimen_16)),
            contentAlignment = Alignment.Center
        ) {
            if (onBackClick != null) {
                Image(
                    painter = painterResource(id = R.drawable.ic_left_arrow),
                    contentDescription = "Back",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable { onBackClick() }
                )
            }

            Text(
                text = "Budget",
                fontFamily = interMedium,
                fontSize = 18.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        // 2. Month Switcher Row (left_arrow_btn, "May", right_arrow_btn)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        monthIndex = if (monthIndex > 0) monthIndex - 1 else months.lastIndex
                    }
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_10))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.left_arrow_btn),
                    contentDescription = "Previous Month"
                )
            }

            Text(
                text = months[monthIndex],
                fontFamily = manropeMedium,
                fontSize = 24.sp,
                color = Color(0xFFFCFCFC),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(3f)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        monthIndex = (monthIndex + 1) % months.size
                    }
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_10))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.right_arrow_btn),
                    contentDescription = "Next Month"
                )
            }
        }

        // 3. Bottom Rounded Container (Replaces curvedLayout with light_white tint)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = dimensionResource(id = R.dimen.dimen_32))
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(lightWhite)
        ) {
            if (budgets.isEmpty()) {
                // Empty Budget Description State (Replaces descriptionText)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = "You don’t have a budget. Let’s make one so you in control.",
                        fontFamily = interMedium,
                        fontSize = 16.sp,
                        color = grayText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(id = R.dimen.dimen_40))
                            .padding(dimensionResource(id = R.dimen.dimen_16))
                    )
                }
            } else {
                // Budget List (Replaces rvList + budget_detail_list_layout.xml)
                LazyColumn(
                    contentPadding = PaddingValues(top = 30.dp, bottom = 20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                ) {
                    items(budgets) { item ->
                        BudgetDetailCard(
                            item = item,
                            manropeRegular = manropeRegular,
                            manropeMedium = manropeMedium,
                            manropeBold = manropeBold,
                            grayText = grayText,
                            redColor = redColor
                        )
                    }
                }
            }

            // Create Budget Button
            Button(
                onClick = onNavigateToCreateBudget,
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                    .padding(bottom = 24.dp)
            ) {
                Text(
                    text = "Create Budget",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
}
