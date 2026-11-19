package com.example.costlyticmoneymanagement.view.screen

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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.example.Costlytic.Utils.Constant
import com.example.costlyticmoneymanagement.R

/**
 * Replaces MoneyActivity (routes between ExpenseScreen and IncomeScreen based on Constant.EXPENSE / Constant.INCOME).
 */
@Composable
fun MoneyScreen(
    typeKey: String,
    typeValue: String,
    onBackToDashboard: () -> Unit,
    onContinueClick: (amount: String, category: String, description: String, wallet: String, isRepeat: Boolean) -> Unit = { _, _, _, _, _ -> }
) {
    when {
        typeKey == Constant.EXPENSE || typeValue == Constant.EXPENSE -> {
            ExpenseScreen(
                onBackToDashboard = onBackToDashboard,
                onContinueClick = onContinueClick
            )
        }
        typeKey == Constant.INCOME || typeValue == Constant.INCOME -> {
            IncomeScreen(
                onBackToDashboard = onBackToDashboard,
                onContinueClick = onContinueClick
            )
        }
    }
}
