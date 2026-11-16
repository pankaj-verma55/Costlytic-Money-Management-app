package com.example.costlyticmoneymanagement.view.screen

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import com.example.costlyticmoneymanagement.R
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyTransactionContent(
    title: String,
    backgroundColor: Color,
    onBackClick: () -> Unit,
    onContinueClick: (amount: String, category: String, description: String, wallet: String, isRepeat: Boolean) -> Unit
) {
    val context = LocalContext.current

    // Form States (matching fragment_expense.xml & fragment_income.xml)
    var amount by remember { mutableStateOf("₹0") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var wallet by remember { mutableStateOf("") }
    var isRepeat by remember { mutableStateOf(false) }

    // Attachment States
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }

    // Dialog / BottomSheet States
    var showAttachmentSheet by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // 1. Camera Launcher (ActivityResultContracts.TakePicture)
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && pendingCameraUri != null) {
            selectedImageUri = pendingCameraUri
            selectedFileName = null
        }
    }

    // 2. Gallery Launcher (ActivityResultContracts.PickVisualMedia)
    val selectPictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            selectedFileName = null
        }
    }

    // 3. Document Launcher (ActivityResultContracts.OpenDocument)
    val selectDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            selectedFileName = getTransactionFileName(context, uri)
            selectedImageUri = null
        }
    }

    // Fonts & Colors
    val interMedium = FontFamily(Font(R.font.inter_medium))
    val manropeSemiBold = FontFamily(Font(R.font.manrope_semibold))
    val manropeBold = FontFamily(Font(R.font.manrope_bold))
    val manropeExtraBold = FontFamily(Font(R.font.manrope_extrabold))

    val hintText = colorResource(id = R.color.hint_text)
    val grayText = colorResource(id = R.color.gray_Text)
    val darkBlack = colorResource(id = R.color.dark_Black)
    val purpleBlue = colorResource(id = R.color.purple_blue)
    val lightWhite = colorResource(id = R.color.light_white)
    val lightPurpleBlue2 = colorResource(id = R.color.light_purple_blue2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .systemBarsPadding()
    ) {
        // 1. Top Title Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.dimen_16)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_left_arrow),
                contentDescription = "Back",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { onBackClick() }
            )

            Text(
                text = title,
                fontFamily = interMedium,
                fontSize = 18.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        // 2. "How much?" Label & Large ₹0 Amount Input
        Text(
            text = "How much?",
            fontFamily = manropeSemiBold,
            fontSize = 20.sp,
            color = Color(0xFFFCFCFC),
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.dimen_26))
                .padding(top = dimensionResource(id = R.dimen.dimen_55))
        )

        BasicTextField(
            value = amount,
            onValueChange = { amount = it },
            singleLine = true,
            cursorBrush = SolidColor(Color.White),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(
                fontFamily = manropeSemiBold,
                fontSize = 64.sp,
                color = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
        )

        // 3. Curved White Bottom Container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            // Category Input
            TransactionInputField(
                value = category,
                onValueChange = { category = it },
                placeholder = "Category",
                fontFamily = interMedium,
                hintColor = hintText,
                textColor = darkBlack,
                showDropdownArrow = true
            )

            // Description Input
            TransactionInputField(
                value = description,
                onValueChange = { description = it },
                placeholder = "Description",
                fontFamily = interMedium,
                hintColor = hintText,
                textColor = darkBlack,
                showDropdownArrow = false
            )

            // Wallet Input
            TransactionInputField(
                value = wallet,
                onValueChange = { wallet = it },
                placeholder = "Wallet",
                fontFamily = interMedium,
                hintColor = hintText,
                textColor = darkBlack,
                showDropdownArrow = true
            )

            // Attachment Section (Selected Image / Selected File / Dotted Add Attachment Box)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                    .padding(top = dimensionResource(id = R.dimen.dimen_16))
            ) {
                when {
                    // Case A: Image Selected from Camera or Gallery
                    selectedImageUri != null -> {
                        val imageBitmap = remember(selectedImageUri) {
                            runCatching {
                                selectedImageUri?.let { uri ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        val source = ImageDecoder.createSource(context.contentResolver, uri)
                                        ImageDecoder.decodeBitmap(source).asImageBitmap()
                                    } else {
                                        context.contentResolver.openInputStream(uri)?.use { stream ->
                                            BitmapFactory.decodeStream(stream)?.asImageBitmap()
                                        }
                                    }
                                }
                            }.getOrNull()
                        }

                        Box(modifier = Modifier.size(width = 106.dp, height = 106.dp)) {
                            if (imageBitmap != null) {
                                Image(
                                    bitmap = imageBitmap,
                                    contentDescription = "Selected Attachment",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .align(Alignment.BottomStart)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                            }

                            // Remove Image Cross Button
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(dimensionResource(id = R.dimen.dimen_18))
                                    .align(Alignment.TopEnd)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.6f))
                                    .clickable { selectedImageUri = null }
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_cross_white),
                                    contentDescription = "Remove Image",
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        }
                    }

                    // Case B: Document Selected
                    selectedFileName != null -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = selectedFileName ?: "fileName",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = darkBlack,
                                fontSize = 14.sp,
                                modifier = Modifier.widthIn(max = 180.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            // Remove Document Cross Button
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(dimensionResource(id = R.dimen.dimen_18))
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.6f))
                                    .clickable { selectedFileName = null }
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_cross_white),
                                    contentDescription = "Remove Document",
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        }
                    }

                    // Case C: Default Dotted "Add attachment" Box
                    else -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .drawBehind {
                                    drawRoundRect(
                                        color = Color(0xFFF1F1FA),
                                        style = Stroke(
                                            width = 1.5.dp.toPx(),
                                            pathEffect = PathEffect.dashPathEffect(
                                                floatArrayOf(12f, 12f),
                                                0f
                                            )
                                        ),
                                        cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
                                    )
                                }
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { showAttachmentSheet = true }
                                .padding(vertical = dimensionResource(id = R.dimen.dimen_16))
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_file_attach),
                                contentDescription = "Attach File"
                            )
                            Text(
                                text = "Add attachment",
                                fontSize = 18.sp,
                                color = grayText,
                                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.dimen_10))
                            )
                        }
                    }
                }
            }

            // Repeat Transaction Row + Switch
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.dimen_16))
                    .padding(
                        start = dimensionResource(id = R.dimen.dimen_32),
                        end = dimensionResource(id = R.dimen.dimen_16)
                    )
            ) {
                Column {
                    Text(
                        text = "Repeat",
                        color = darkBlack,
                        fontSize = 18.sp,
                        fontFamily = manropeBold
                    )
                    Text(
                        text = "Repeat transaction",
                        color = grayText,
                        fontSize = 18.sp,
                        fontFamily = manropeBold
                    )
                }

                Switch(
                    checked = isRepeat,
                    onCheckedChange = { isRepeat = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = lightWhite,
                        uncheckedThumbColor = lightWhite,
                        checkedTrackColor = purpleBlue,
                        uncheckedTrackColor = lightPurpleBlue2,
                        uncheckedBorderColor = Color.Transparent,
                        checkedBorderColor = Color.Transparent
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Continue Button -> triggers callback AND shows TransactionSuccessDone dialog
            Button(
                onClick = {
                    onContinueClick(amount, category, description, wallet, isRepeat)
                    showSuccessDialog = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                    .padding(
                        top = dimensionResource(id = R.dimen.dimen_24),
                        bottom = dimensionResource(id = R.dimen.dimen_15)
                    )
            ) {
                Text(
                    text = "Continue",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }

    // 4. Attachment Bottom Sheet (Replaces AttachmentDialog)
    if (showAttachmentSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAttachmentSheet = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = dimensionResource(id = R.dimen.dimen_16))
                        .size(width = 60.dp, height = 5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFFEEE5FF))
                )
            }
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
                    .padding(top = 30.dp, bottom = 24.dp)
            ) {
                AttachmentOptionCard(
                    iconRes = R.drawable.ic_camera,
                    label = "camera",
                    purpleBlue = purpleBlue,
                    fontFamily = manropeExtraBold,
                    onClick = {
                        showAttachmentSheet = false
                        val uri = createTransactionImageUri(context)
                        pendingCameraUri = uri
                        takePictureLauncher.launch(uri)
                    },
                    modifier = Modifier.weight(1f)
                )

                AttachmentOptionCard(
                    iconRes = R.drawable.ic_gallery,
                    label = "Image",
                    purpleBlue = purpleBlue,
                    fontFamily = manropeExtraBold,
                    onClick = {
                        showAttachmentSheet = false
                        selectPictureLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.weight(1f)
                )

                AttachmentOptionCard(
                    iconRes = R.drawable.ic_doc,
                    label = "Document",
                    purpleBlue = purpleBlue,
                    fontFamily = manropeExtraBold,
                    onClick = {
                        showAttachmentSheet = false
                        selectDocumentLauncher.launch(arrayOf("*/*"))
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    // 5. Transaction Success Dialog (Replaces TransactionSuccessDoneBinding Dialog)
    if (showSuccessDialog) {
        Dialog(
            onDismissRequest = { showSuccessDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(vertical = dimensionResource(id = R.dimen.dimen_20))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.blue_tick),
                    contentDescription = "Success"
                )

                Text(
                    text = "Transaction has been successfully added",
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = manropeSemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(id = R.dimen.dimen_24))
                        .padding(top = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun TransactionInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    fontFamily: FontFamily,
    hintColor: Color,
    textColor: Color,
    showDropdownArrow: Boolean
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                fontFamily = fontFamily,
                fontSize = 16.sp,
                color = hintColor
            )
        },
        textStyle = TextStyle(
            fontFamily = fontFamily,
            fontSize = 16.sp,
            color = textColor
        ),
        singleLine = true,
        trailingIcon = if (showDropdownArrow) {
            {
                Image(
                    painter = painterResource(id = R.drawable.down_arrow_gray),
                    contentDescription = placeholder
                )
            }
        } else null,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.dimen_16))
            .padding(top = dimensionResource(id = R.dimen.dimen_24))
            .border(
                border = BorderStroke(1.dp, Color(0xFFF1F1FA)),
                shape = RoundedCornerShape(16.dp)
            )
    )
}

@Composable
private fun AttachmentOptionCard(
    iconRes: Int,
    label: String,
    purpleBlue: Color,
    fontFamily: FontFamily,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFEEE5FF))
            .clickable { onClick() }
            .padding(dimensionResource(id = R.dimen.dimen_20))
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(35.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontFamily = fontFamily,
            fontSize = 14.sp,
            color = purpleBlue
        )
    }
}

private fun createTransactionImageUri(context: Context): Uri {
    val file = File(context.cacheDir, "camera_image.jpg")
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}

private fun getTransactionFileName(context: Context, uri: Uri): String {
    var name = "Unknown File"
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (it.moveToFirst() && nameIndex != -1) {
            name = it.getString(nameIndex)
        }
    }
    return name
}