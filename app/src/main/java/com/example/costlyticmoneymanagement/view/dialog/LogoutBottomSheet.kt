package com.example.costlyticmoneymanagement.view.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.costlyticmoneymanagement.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutBottomSheet(
    onDismiss: () -> Unit,
    onConfirmLogout: () -> Unit
) {
    val manropeBold = FontFamily(Font(R.font.manrope_bold))
    val manropeMedium = FontFamily(Font(R.font.manrope_medium))
    val manropeSemiBold = FontFamily(Font(R.font.manrope_semibold))

    val purpleBlue = colorResource(id = R.color.purple_blue)
    val grayText = colorResource(id = R.color.gray_Text)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFD3BDFF))
            )
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 28.dp)
        ) {
            Text(
                text = "Logout?",
                fontFamily = manropeBold,
                fontSize = 18.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Are you sure do you wanna logout?",
                fontFamily = manropeMedium,
                fontSize = 16.sp,
                color = grayText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEEE5FF),
                        contentColor = purpleBlue
                    ),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "No",
                        fontFamily = manropeSemiBold,
                        fontSize = 18.sp,
                        color = purpleBlue
                    )
                }

                Button(
                    onClick = onConfirmLogout,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = purpleBlue,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Yes",
                        fontFamily = manropeSemiBold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}