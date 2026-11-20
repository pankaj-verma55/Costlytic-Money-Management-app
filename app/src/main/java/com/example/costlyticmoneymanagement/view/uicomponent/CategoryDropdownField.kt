package com.example.costlyticmoneymanagement.view.uicomponent

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.costlyticmoneymanagement.R
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdownField(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    placeholderText: String = "Category",
    modifier: Modifier = Modifier
) {

    var isExpanded by remember { mutableStateOf(false) }

    val interMedium = FontFamily(Font(R.font.inter_medium))
    val hintText = colorResource(id = R.color.hint_text)
    val darkBlack = colorResource(id = R.color.dark_Black)

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
            .padding(top = dimensionResource(id = R.dimen.dimen_24))
    ) {
        TextField(
            value = selectedCategory,
            onValueChange = {},
            readOnly = true,
            placeholder = {
                Text(
                    text = placeholderText,
                    fontFamily = interMedium,
                    fontSize = 16.sp,
                    color = hintText
                )
            },
            textStyle = TextStyle(
                fontFamily = interMedium,
                fontSize = 16.sp,
                color = darkBlack
            ),
            singleLine = true,
            trailingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.down_arrow_gray),
                    contentDescription = "Select $placeholderText",
                    modifier = Modifier.rotate(if (isExpanded) 180f else 0f)
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
                .border(
                    border = BorderStroke(1.dp, Color(0xFFF1F1FA)),
                    shape = RoundedCornerShape(16.dp)
                )
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.border(
                border = BorderStroke(1.dp, Color(0xFFF1F1FA)),
                shape = RoundedCornerShape(16.dp)
            )
        ) {
            categories.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            fontFamily = interMedium,
                            fontSize = 16.sp,
                            color = darkBlack
                        )
                    },
                    onClick = {
                        onCategorySelected(item)
                        isExpanded = false
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}