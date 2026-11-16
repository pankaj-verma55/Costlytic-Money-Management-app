package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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


/**
 * Replaces SettingFragment and fragment_setting.xml.
 */
@Composable
fun SettingScreen(
    onBackClick: () -> Unit,
    onCurrencyClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onThemeClick: () -> Unit = {},
    onSecurityClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onHelpClick: () -> Unit = {}
) {
    val interMedium = FontFamily(Font(R.font.inter_medium))
    val manropeMedium = FontFamily(Font(R.font.manrope_medium))

    val blackText = colorResource(id = R.color.black_text)
    val grayText = colorResource(id = R.color.gray_Text)
    val lightGray = colorResource(id = R.color.ligth_gray)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ) {
        // Top Title Bar (Replaces title_back_layout)
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
                text = "Settings",
                fontFamily = interMedium,
                fontSize = 18.sp,
                color = blackText,
                textAlign = TextAlign.Center
            )
        }

        // Divider line
        HorizontalDivider(
            thickness = dimensionResource(id = R.dimen.dimen_1),
            color = lightGray
        )

        // Settings Items List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            SettingOptionRow(
                title = "Currency",
                subtitleValue = "USD",
                fontFamily = manropeMedium,
                grayText = grayText,
                onClick = onCurrencyClick
            )

            SettingOptionRow(
                title = "Language",
                subtitleValue = "English",
                fontFamily = manropeMedium,
                grayText = grayText,
                onClick = onLanguageClick
            )

            SettingOptionRow(
                title = "Theme",
                subtitleValue = "Light",
                fontFamily = manropeMedium,
                grayText = grayText,
                onClick = onThemeClick
            )

            SettingOptionRow(
                title = "Security",
                subtitleValue = "FingerPrint",
                fontFamily = manropeMedium,
                grayText = grayText,
                onClick = onSecurityClick
            )

            SettingOptionRow(
                title = "Notification",
                subtitleValue = null,
                fontFamily = manropeMedium,
                grayText = grayText,
                onClick = onNotificationClick
            )

            SettingOptionRow(
                title = "About",
                subtitleValue = null,
                fontFamily = manropeMedium,
                grayText = grayText,
                onClick = onAboutClick
            )

            SettingOptionRow(
                title = "Help",
                subtitleValue = null,
                fontFamily = manropeMedium,
                grayText = grayText,
                onClick = onHelpClick
            )
        }
    }
}



@Composable
private fun SettingOptionRow(
    title: String,
    subtitleValue: String?,
    fontFamily: FontFamily,
    grayText: Color,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Text(
            text = title,
            color = Color.Black,
            fontSize = 16.sp,
            fontFamily = fontFamily,
            modifier = Modifier.weight(1f)
        )

        if (subtitleValue != null) {
            Text(
                text = subtitleValue,
                color = grayText,
                fontSize = 16.sp,
                fontFamily = fontFamily,
                modifier = Modifier.padding(end = dimensionResource(id = R.dimen.dimen_8))
            )
        }

        Image(
            painter = painterResource(id = R.drawable.down_arrow),
            contentDescription = title,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.dimen_24))
                .rotate(-90f)
        )
    }
}