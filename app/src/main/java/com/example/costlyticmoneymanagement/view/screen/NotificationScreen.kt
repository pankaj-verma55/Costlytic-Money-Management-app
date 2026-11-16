package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.costlyticmoneymanagement.R

@Composable
fun NotificationScreen(
    onBackClick: () -> Unit,
    notifications: List<Any> = emptyList(),
    onMarkAllReadClick: () -> Unit = {},
    onRemoveAllClick: () -> Unit = {}
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    // Fonts & Colors
    val interMedium = FontFamily(Font(R.font.inter_medium))
    val manropeRegular = FontFamily(Font(R.font.manrope_regular))
    val blackText = colorResource(id = R.color.black_text)
    val grayText = colorResource(id = R.color.gray_Text)
    val lightGray = colorResource(id = R.color.ligth_gray)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (isMenuExpanded) isMenuExpanded = false
            }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar (Replaces title_back_layout.xml)
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
                    text = "Notification",
                    fontFamily = interMedium,
                    fontSize = 18.sp,
                    color = blackText,
                    textAlign = TextAlign.Center
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_three_dot),
                    contentDescription = "Options",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { isMenuExpanded = !isMenuExpanded }
                )
            }

            // Notification List (Replaces notificatoinRv)
            if (notifications.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(notifications) { _ ->
                        // Ready for notification item composable when adapter/model is added
                    }
                }
            }
        }

        // Empty State Text (Replaces no_notif centered in ConstraintLayout)
        if (notifications.isEmpty()) {
            Text(
                text = "There is no notification for now",
                fontSize = 16.sp,
                color = grayText,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Popup Card Menu (Replaces notifLayout CardView)
        if (isMenuExpanded) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = dimensionResource(id = R.dimen.dimen_50),
                        end = dimensionResource(id = R.dimen.dimen_20)
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(150.dp)
                ) {
                    Text(
                        text = "Mark all read",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontFamily = manropeRegular,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isMenuExpanded = false
                                onMarkAllReadClick()
                            }
                            .padding(
                                vertical = dimensionResource(id = R.dimen.dimen_16),
                                horizontal = dimensionResource(id = R.dimen.dimen_24)
                            )
                    )

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = lightGray
                    )

                    Text(
                        text = "Remove all",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontFamily = manropeRegular,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isMenuExpanded = false
                                onRemoveAllClick()
                            }
                            .padding(
                                vertical = dimensionResource(id = R.dimen.dimen_16),
                                horizontal = dimensionResource(id = R.dimen.dimen_24)
                            )
                    )
                }
            }
        }
    }
}