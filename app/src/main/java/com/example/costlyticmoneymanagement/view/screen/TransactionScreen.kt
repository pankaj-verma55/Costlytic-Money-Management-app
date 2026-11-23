package com.example.costlyticmoneymanagement.view.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.costlyticmoneymanagement.R
import com.example.costlyticmoneymanagement.model.data.TransactionListItem
import com.example.costlyticmoneymanagement.utils.RenderUrlImage
import com.example.costlyticmoneymanagement.viewmodel.TransactionListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    onNavigateToFinancialReport: () -> Unit = {},
    transactionListViewModel: TransactionListViewModel = viewModel()
) {
    val uiState by transactionListViewModel.uiState.collectAsState()
    val selectedMonth = transactionListViewModel.selectedMonth
//    // Exact dummy data from TransactionFragment.prepareData()
//    val transactionList = remember {
//        listOf(
//            TransactionListItem.Header("Today"),
//            TransactionListItem.Transaction(
//                image = "https://via.placeholder.com/100",
//                title = "Shopping",
//                subtitle = "Buy some grocery",
//                price = "-120",
//                time = "11:00 AM"
//            ),
//            TransactionListItem.Transaction(
//                image = "https://via.placeholder.com/100",
//                title = "Food",
//                subtitle = "Buy a ramen",
//                price = "-32",
//                time = "07:30 PM"
//            ),
//            TransactionListItem.Header("Yesterday"),
//            TransactionListItem.Transaction(
//                image = "https://via.placeholder.com/100",
//                title = "Salary",
//                subtitle = "Salary for July",
//                price = "+5000",
//                time = "04:30 PM"
//            )
//        )
//    }

    val monthOptions = remember {
        listOf(
            "Yearly", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
    }
    var isMonthSliderVisible by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }

    // Fonts & Colors
    val manropeLight = FontFamily(Font(R.font.manrope_light))
    val manropeMedium = FontFamily(Font(R.font.manrope_medium))
    val manropeSemiBold = FontFamily(Font(R.font.manrope_semibold))
    val manropeBold = FontFamily(Font(R.font.manrope_bold))
    val interBold = FontFamily(Font(R.font.inter_bold))

    val purpleBlue = colorResource(id = R.color.purple_blue)
    val grayText = colorResource(id = R.color.gray_Text)
    val partialBlack = colorResource(id = R.color.partialBlack)
    val holoRedDark = colorResource(id = android.R.color.holo_red_dark)
    val holoGreenDark = colorResource(id = android.R.color.holo_green_dark)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
//            .systemBarsPadding()
    ) {
        // 1. Top Bar: Month Pill & Filter Icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                .padding(top = dimensionResource(id = R.dimen.dimen_16))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (selectedMonth != "Month") Color(0xFFEEE5FF) else Color.Transparent
                    )
                    .border(
                        border = BorderStroke(
                            1.dp,
                            if (selectedMonth != "Month") purpleBlue else Color(0xFFF1F1FA)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { isMonthSliderVisible = !isMonthSliderVisible }
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.dimen_12),
                        vertical = 5.dp
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.down_arrow),
                    contentDescription = "down Arrow",
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(
                    text = selectedMonth,
                    color = if (selectedMonth != "Month") purpleBlue else Color.Black,
                    fontSize = 16.sp,
                    fontFamily = manropeSemiBold
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_filter_icon),
                contentDescription = "Filter Transactions",
                modifier = Modifier
                    .clickable { showFilterSheet = true }
                    .padding(4.dp)
            )
        }

        AnimatedVisibility(
            visible = isMonthSliderVisible,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(monthOptions) { month ->
                    val isSelected =
                        selectedMonth == month || (selectedMonth == "Month" && month == "Yearly")

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) purpleBlue else Color(0xFFEEE5FF))
                            .clickable {
                                transactionListViewModel.selectMonth(month)
                                isMonthSliderVisible = false
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = month,
                            fontSize = 14.sp,
                            fontFamily = manropeSemiBold,
                            color = if (isSelected) Color.White else purpleBlue
                        )
                    }
                }
            }
        }

        // 2. "See your financial report" Banner
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                .padding(top = dimensionResource(id = R.dimen.dimen_24))
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFEEE5FF))
                .clickable { onNavigateToFinancialReport() }
                .padding(dimensionResource(id = R.dimen.dimen_16))
        ) {
            Text(
                text = "See your financial report",
                fontSize = 16.sp,
                color = purpleBlue,
                fontFamily = manropeLight,
                modifier = Modifier.weight(1f)
            )

            Image(
                painter = painterResource(id = R.drawable.right_arrow),
                contentDescription = "View Financial Report"
            )
        }

        // 3. Multi-Type Live Transaction List
        if (uiState.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                CircularProgressIndicator(color = purpleBlue)
            }
        } else if (uiState.items.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = "No transactions found",
                    fontFamily = manropeSemiBold,
                    fontSize = 16.sp,
                    color = grayText
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
            ) {
                items(uiState.items) { item ->
                    when (item) {
                        is TransactionListItem.Header -> {
                            Text(
                                text = item.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = dimensionResource(id = R.dimen.dimen_16))
                            )
                        }

                        is TransactionListItem.Transaction -> {
                            TransactionRowItem(
                                item = item,
                                manropeBold = manropeBold,
                                manropeSemiBold = manropeSemiBold,
                                grayText = grayText,
                                positiveColor = holoGreenDark,
                                negativeColor = holoRedDark
                            )
                        }
                    }
                }
            }
        }
    }

    // 4. Filter Bottom Sheet
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = dimensionResource(id = R.dimen.dimen_16))
                        .size(width = 60.dp, height = 5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFFD3BDFF))
                )
            }
        ) {
            TransactionFilterSheetContent(
                selectedFilterBy = transactionListViewModel.tempFilterBy,
                selectedSortBy = transactionListViewModel.tempSortBy,
                onFilterBySelected = { transactionListViewModel.selectTempFilterBy(it) },
                onSortBySelected = { transactionListViewModel.selectTempSortBy(it) },
                onResetClick = { transactionListViewModel.resetFilters() },
                onApplyClick = {
                    transactionListViewModel.applyFilters()
                    showFilterSheet = false
                },
                manropeMedium = manropeMedium,
                manropeBold = manropeBold,
                interBold = interBold,
                purpleBlue = purpleBlue,
                partialBlack = partialBlack
            )
        }
    }
}

@Composable
private fun TransactionRowItem(
    item: TransactionListItem.Transaction,
    manropeBold: FontFamily,
    manropeSemiBold: FontFamily,
    grayText: Color,
    positiveColor: Color,
    negativeColor: Color
) {
    val isNegative = item.price.startsWith("-")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.dimen_8))
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFFCFCFC))
            .padding(
                horizontal = dimensionResource(id = R.dimen.dimen_16),
                vertical = 14.dp
            )
    ) {
        if (item.image.isNotBlank()) {
            RenderUrlImage(
                imageUrl = item.image,
                contentDescription = item.title,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        } else {
            Image(
                painter = painterResource(
                    id = if (isNegative) R.drawable.expense else R.drawable.ic_income
                ),
                contentDescription = item.title,
                modifier = Modifier.size(48.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = dimensionResource(id = R.dimen.dimen_10))
        ) {
            Text(
                text = item.title,
                fontFamily = manropeBold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = item.subtitle,
                fontFamily = manropeSemiBold,
                fontSize = 14.sp,
                color = grayText
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = item.price,
                fontFamily = manropeBold,
                fontSize = 18.sp,
                color = if (isNegative) negativeColor else positiveColor
            )
            Text(
                text = item.time,
                fontFamily = manropeSemiBold,
                fontSize = 14.sp,
                color = grayText
            )
        }
    }
}

@Composable
private fun TransactionFilterSheetContent(
    selectedFilterBy: String?,
    selectedSortBy: String?,
    onFilterBySelected: (String) -> Unit,
    onSortBySelected: (String) -> Unit,
    onResetClick: () -> Unit,
    onApplyClick: () -> Unit,
    manropeMedium: FontFamily,
    manropeBold: FontFamily,
    interBold: FontFamily,
    purpleBlue: Color,
    partialBlack: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = dimensionResource(id = R.dimen.dimen_24))
        ) {
            Text(
                text = "Filter Transaction",
                fontFamily = manropeBold,
                fontSize = 18.sp,
                color = Color.Black
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color(0xFFEEE5FF))
                    .clickable { onResetClick() }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Reset",
                    fontFamily = manropeMedium,
                    fontSize = 16.sp,
                    color = purpleBlue
                )
            }
        }

        Text(
            text = "Filted By",
            fontFamily = manropeBold,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier
                .padding(start = 16.dp)
                .padding(top = dimensionResource(id = R.dimen.dimen_20))
        )

        val filterOptions = listOf("Income", "Expense", "Transfer")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            filterOptions.forEach { option ->
                val isSelected = selectedFilterBy == option
                FilterChipPill(
                    label = option,
                    isSelected = isSelected,
                    fontFamily = manropeMedium,
                    purpleBlue = purpleBlue,
                    partialBlack = partialBlack,
                    onClick = { onFilterBySelected(option) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Text(
            text = "Sort By",
            fontFamily = manropeBold,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier
                .padding(start = 16.dp)
                .padding(top = dimensionResource(id = R.dimen.dimen_20))
        )

        val sortOptions = listOf("Highest", "Lowest", "Newest")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            sortOptions.forEach { option ->
                val isSelected = selectedSortBy == option
                FilterChipPill(
                    label = option,
                    isSelected = isSelected,
                    fontFamily = manropeMedium,
                    purpleBlue = purpleBlue,
                    partialBlack = partialBlack,
                    onClick = { onSortBySelected(option) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Button(
            onClick = onApplyClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = purpleBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = dimensionResource(id = R.dimen.dimen_27))
        ) {
            Text(
                text = "Apply",
                fontFamily = interBold,
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun FilterChipPill(
    label: String,
    isSelected: Boolean,
    fontFamily: FontFamily,
    purpleBlue: Color,
    partialBlack: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(if (isSelected) Color(0xFFEEE5FF) else Color.White)
            .border(
                border = BorderStroke(1.dp, if (isSelected) purpleBlue else Color(0xFFE3E5E5)),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() }
            .padding(
                vertical = dimensionResource(id = R.dimen.dimen_12),
                horizontal = 16.dp
            )
    ) {
        Text(
            text = label,
            fontFamily = fontFamily,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = if (isSelected) purpleBlue else partialBlack
        )
    }
}