package com.example.ui.orders

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.UserEntity
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.Amber600
import com.example.ui.theme.Cyan500
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Rose400
import com.example.ui.theme.Rose500
import com.example.ui.theme.Stone100
import com.example.ui.theme.Stone200
import com.example.ui.theme.Stone300
import com.example.ui.theme.Stone400
import com.example.ui.theme.Stone500
import com.example.ui.theme.Stone700
import com.example.ui.theme.Stone800
import com.example.ui.theme.Stone850
import com.example.ui.theme.Stone900
import com.example.ui.theme.Stone950
import com.example.util.WorkWeekUtil

@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel,
    currentUser: UserEntity,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val allProducts by viewModel.allProducts.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val currentCategory = categories.find { it.id == uiState.selectedCategoryId }
        ?: categories.firstOrNull()

    // Filter products for category and search
    val categoryProducts = remember(allProducts, currentCategory, uiState.searchQuery, uiState.showOnlyFilled, uiState.quantities) {
        if (currentCategory == null) emptyList()
        else {
            allProducts.filter { p ->
                val matchesCategory = p.categoryId == currentCategory.id && p.isActive
                val matchesSearch = uiState.searchQuery.isBlank() || p.nameAr.contains(uiState.searchQuery.trim(), ignoreCase = true)
                val matchesFilled = !uiState.showOnlyFilled || (uiState.quantities[p.id] ?: 0.0) > 0.0
                matchesCategory && matchesSearch && matchesFilled
            }
        }
    }

    val filledCountInCategory = remember(categoryProducts, uiState.quantities) {
        categoryProducts.count { (uiState.quantities[it.id] ?: 0.0) > 0.0 }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Stone950)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 76.dp) // leave space for bottom navigation
            ) {
                // Top Work Week & Policy Bar
                TopKitchenBar(currentUser = currentUser)

                // Search & Filter Row
                SearchBarRow(
                    searchQuery = uiState.searchQuery,
                    onSearchChange = { viewModel.setSearchQuery(it) },
                    showOnlyFilled = uiState.showOnlyFilled,
                    onToggleFilled = { viewModel.toggleShowOnlyFilled() },
                    onFillParLevels = { currentCategory?.let { viewModel.fillParLevelsForCategory(it.id) } },
                    onClearQuantities = { currentCategory?.let { viewModel.clearCategoryQuantities(it.id) } }
                )

                // Department / Category Horizontal Scroll Tabs
                CategoryTabsRow(
                    categories = categories,
                    selectedCategoryId = currentCategory?.id ?: "",
                    onCategorySelect = { viewModel.selectCategory(it) },
                    quantities = uiState.quantities,
                    allProducts = allProducts
                )

                // Fixed Arrival 01:00 PM Policy Reminder Badge
                FixedArrivalPolicyBanner()

                // Products Items List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (categoryProducts.isEmpty()) {
                        item {
                            EmptyProductsNotice(searchActive = uiState.searchQuery.isNotBlank())
                        }
                    } else {
                        items(categoryProducts, key = { it.id }) { product ->
                            val currentQty = uiState.quantities[product.id] ?: 0.0
                            ProductItemRow(
                                product = product,
                                currentQty = currentQty,
                                onQuantityChange = { viewModel.setQuantity(product.id, it) },
                                onStepQuantity = { viewModel.stepQuantity(product.id, it) }
                            )
                        }
                    }
                }

                // Bottom Dispatch Dock Bar
                BottomDispatchDock(
                    categoryName = currentCategory?.nameAr ?: "القسم",
                    filledCount = filledCountInCategory,
                    recipientType = uiState.recipientTypes[currentCategory?.id ?: ""] ?: "supplier",
                    onRecipientChange = { currentCategory?.let { cat -> viewModel.setRecipientType(cat.id, it) } },
                    onDispatchWhatsApp = {
                        currentCategory?.let { cat ->
                            viewModel.prepareAndDispatchOrder(context, currentUser, cat.id)
                        }
                    }
                )
            }

            // WhatsApp Dispatch Success & CC Confirmation Modal
            if (uiState.showDispatchDialog) {
                DispatchConfirmationDialog(
                    orderCode = uiState.recentCreatedOrderId ?: "",
                    categoryName = uiState.dispatchCategoryName,
                    targetPhone = uiState.dispatchTargetPhone,
                    chefPhone = uiState.dispatchChefPhone,
                    totalItems = uiState.dispatchTotalItems,
                    messageText = uiState.lastDispatchedWhatsAppMessage ?: "",
                    onChefCcClick = { viewModel.sendChefCcWhatsApp(context) },
                    onDismiss = { viewModel.dismissDispatchDialog() }
                )
            }

            // Toast feedback
            uiState.toastMessage?.let { msg ->
                ToastBanner(message = msg, onDismiss = { viewModel.clearToast() })
            }
        }
    }
}

@Composable
private fun TopKitchenBar(currentUser: UserEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Stone900),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Stone800, Amber500.copy(alpha = 0.2f))))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Amber500.copy(alpha = 0.2f))
                        .border(1.dp, Amber500.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentUser.name.firstOrNull()?.toString() ?: "م",
                        color = Amber400,
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = currentUser.name,
                        color = Stone100,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "${WorkWeekUtil.getTodayDayNameAr()} • ${WorkWeekUtil.getTodayDateAr()}",
                        color = Stone400,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Amber500.copy(alpha = 0.15f))
                    .border(1.dp, Amber500.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = WorkWeekUtil.getWorkWeekString(),
                    color = Amber400,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SearchBarRow(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    showOnlyFilled: Boolean,
    onToggleFilled: () -> Unit,
    onFillParLevels: () -> Unit,
    onClearQuantities: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("بحث في الأصناف...", color = Stone500, fontSize = 12.sp) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = Stone400, modifier = Modifier.size(18.dp))
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "مسح", tint = Stone400, modifier = Modifier.size(16.dp))
                    }
                }
            },
            modifier = Modifier
                .weight(1f)
                .height(46.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Stone900,
                unfocusedContainerColor = Stone900,
                focusedBorderColor = Amber500,
                unfocusedBorderColor = Stone800,
                focusedTextColor = Stone100,
                unfocusedTextColor = Stone100
            ),
            singleLine = true
        )

        // Show only filled toggle
        Box(
            modifier = Modifier
                .height(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (showOnlyFilled) Emerald500.copy(alpha = 0.2f) else Stone900)
                .border(1.dp, if (showOnlyFilled) Emerald500 else Stone800, RoundedCornerShape(12.dp))
                .clickable { onToggleFilled() }
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.FilterAlt,
                contentDescription = "تصفية الممتلئ",
                tint = if (showOnlyFilled) Emerald400 else Stone400,
                modifier = Modifier.size(18.dp)
            )
        }

        // Quick Par Level Autofill Button
        Box(
            modifier = Modifier
                .height(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Amber500.copy(alpha = 0.15f))
                .border(1.dp, Amber500.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .clickable { onFillParLevels() }
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "ملء المعدل القياسي",
                    tint = Amber400,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("المعدل", color = Amber400, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun CategoryTabsRow(
    categories: List<CategoryEntity>,
    selectedCategoryId: String,
    onCategorySelect: (String) -> Unit,
    quantities: Map<String, Double>,
    allProducts: List<ProductEntity>
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories, key = { it.id }) { cat ->
            val isSelected = cat.id == selectedCategoryId
            val catProducts = allProducts.filter { it.categoryId == cat.id }
            val filledCount = catProducts.count { (quantities[it.id] ?: 0.0) > 0.0 }

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onCategorySelect(cat.id) },
                shape = RoundedCornerShape(14.dp),
                color = if (isSelected) Amber500 else Stone900,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSelected) Amber400 else Stone800
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = cat.icon,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = cat.nameAr,
                        color = if (isSelected) Stone950 else Stone100,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                        fontSize = 12.sp
                    )

                    if (filledCount > 0) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) Stone950 else Emerald500.copy(alpha = 0.2f))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "$filledCount",
                                color = if (isSelected) Amber400 else Emerald400,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FixedArrivalPolicyBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Amber500.copy(alpha = 0.08f))
            .border(1.dp, Amber500.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Alarm, contentDescription = null, tint = Amber400, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "موعد وصول التوريدات المحدد: 01:00 ظهراً",
                color = Amber300(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "إنذار تأخير 24 ساعة",
            color = Rose400,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun Amber300() = Color(0xFFFCD34D)

@Composable
private fun ProductItemRow(
    product: ProductEntity,
    currentQty: Double,
    onQuantityChange: (Double) -> Unit,
    onStepQuantity: (Double) -> Unit
) {
    val isFilled = currentQty > 0.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFilled) Stone900 else Stone900.copy(alpha = 0.7f)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                if (isFilled) listOf(Emerald500.copy(alpha = 0.4f), Amber500.copy(alpha = 0.3f))
                else listOf(Stone800, Stone800)
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Product info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Row Index Badge
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Stone950)
                        .border(1.dp, Stone800, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${product.sheetRow}",
                        color = Stone500,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = product.nameAr,
                        color = Stone100,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Stone950)
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = product.unit,
                                color = Amber400,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "المعدل: ${if (product.parLevel % 1.0 == 0.0) product.parLevel.toLong() else product.parLevel}",
                            color = Stone400,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            // Stepper and Quantity controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Minus button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Stone800)
                        .clickable { onStepQuantity(-1.0) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "تنقيص",
                        tint = Stone300,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Numeric display / input
                var isEditing by remember { mutableStateOf(false) }
                var textValue by remember(currentQty) {
                    mutableStateOf(if (currentQty == 0.0) "" else if (currentQty % 1.0 == 0.0) currentQty.toLong().toString() else currentQty.toString())
                }

                Box(
                    modifier = Modifier
                        .width(52.dp)
                        .height(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isFilled) Amber500.copy(alpha = 0.2f) else Stone950)
                        .border(
                            1.dp,
                            if (isFilled) Amber500.copy(alpha = 0.6f) else Stone800,
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (currentQty == 0.0) "-" else if (currentQty % 1.0 == 0.0) currentQty.toLong().toString() else currentQty.toString(),
                        color = if (isFilled) Amber400 else Stone500,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp
                    )
                }

                // Plus button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Amber500)
                        .clickable { onStepQuantity(1.0) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "زيادة",
                        tint = Stone950,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomDispatchDock(
    categoryName: String,
    filledCount: Int,
    recipientType: String,
    onRecipientChange: (String) -> Unit,
    onDispatchWhatsApp: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        color = Stone900,
        border = androidx.compose.foundation.BorderStroke(1.dp, Stone700)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Recipient Toggle (Supplier vs Purchasing)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "جهة استلام الطلب:",
                    color = Stone400,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Stone950)
                        .border(1.dp, Stone800, RoundedCornerShape(10.dp))
                        .padding(2.dp)
                ) {
                    val isSupplier = recipientType == "supplier"
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSupplier) Amber500 else Color.Transparent)
                            .clickable { onRecipientChange("supplier") }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "المورد المعتمد",
                            color = if (isSupplier) Stone950 else Stone400,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (!isSupplier) Amber500 else Color.Transparent)
                            .clickable { onRecipientChange("purchasing") }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "مسؤول المشتريات",
                            color = if (!isSupplier) Stone950 else Stone400,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            // Big Action WhatsApp Dispatch Button
            Button(
                onClick = onDispatchWhatsApp,
                enabled = filledCount > 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Emerald600,
                    disabledContainerColor = Stone800
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = if (filledCount > 0) Color.White else Stone500,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (filledCount > 0) "إرسال طلبية ($categoryName) عبر WhatsApp ($filledCount صنف)" else "حدد الكميات لتفعيل إرسال الواتساب",
                        color = if (filledCount > 0) Color.White else Stone500,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DispatchConfirmationDialog(
    orderCode: String,
    categoryName: String,
    targetPhone: String,
    chefPhone: String,
    totalItems: Int,
    messageText: String,
    onChefCcClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val clipboard = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }
    var chefCcSent by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(22.dp),
            color = Stone900,
            border = androidx.compose.foundation.BorderStroke(1.dp, Stone700)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = Stone400)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "تم فتح واتساب وتجهيز الطلبية",
                            color = Emerald400,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "كود الطلب: $orderCode • $categoryName",
                            color = Stone300,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Box(modifier = Modifier.size(40.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Summary info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Stone950),
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Stone800, Stone800)))
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("الجهة المستلمة:", color = Stone400, fontSize = 11.sp)
                            Text(targetPhone, color = Emerald400, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("عدد الأصناف:", color = Stone400, fontSize = 11.sp)
                            Text("$totalItems صنف", color = Amber400, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("موعد الوصول الإلزامي:", color = Stone400, fontSize = 11.sp)
                            Text("01:00 ظهراً", color = Stone200, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action 1: Send WhatsApp CC to Head Chef
                Button(
                    onClick = {
                        onChefCcClick()
                        chefCcSent = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (chefCcSent) Stone800 else Amber500
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = if (chefCcSent) Emerald400 else Stone950,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (chefCcSent) "تم إرسال نسخة للشيف العمومي بنجاح" else "إرسال نسخة للشيف العمومي (Chef CC)",
                            color = if (chefCcSent) Emerald400 else Stone950,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action 2: Copy raw message text
                Button(
                    onClick = {
                        clipboard.setText(AnnotatedString(messageText))
                        copied = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Stone800)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                            contentDescription = null,
                            tint = if (copied) Emerald400 else Amber400,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (copied) "تم نسخ نص الرسالة للحافظة!" else "نسخ نص الطلب كاملاً",
                            color = Stone200,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyProductsNotice(searchActive: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 36.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = Stone500, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (searchActive) "لا توجد نتائج مطابقة لبحثك" else "لا توجد أصناف في هذا القسم حالياً",
                color = Stone400,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ToastBanner(message: String, onDismiss: () -> Unit) {
    LaunchedEffect(message) {
        kotlinx.coroutines.delay(3000)
        onDismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Stone900,
            border = androidx.compose.foundation.BorderStroke(1.dp, Emerald500),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald400, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = message, color = Stone100, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
