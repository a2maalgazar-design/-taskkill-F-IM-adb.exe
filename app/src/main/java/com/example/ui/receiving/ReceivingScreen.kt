package com.example.ui.receiving

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.OrderItemEntity
import com.example.data.local.entity.ReceivingInspectionEntity
import com.example.data.local.entity.UserEntity
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
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
fun ReceivingScreen(
    viewModel: ReceivingViewModel,
    currentUser: UserEntity,
    modifier: Modifier = Modifier
) {
    val activeOrders by viewModel.activeOrders.collectAsStateWithLifecycle()
    val inspections by viewModel.allInspections.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Filter orders ready for receiving
    val receivingOrders = activeOrders.filter {
        it.status in listOf("ORDERED", "AWAITING_DELIVERY", "RECEIVING", "PARTIALLY_RECEIVED")
    }

    // Check for vegetable 24h delay
    val delayedVegOrders = receivingOrders.filter {
        (it.categoryId == "cat-veg" || it.id.contains("VEG")) &&
                WorkWeekUtil.isVegOrderDelayedOver24h(it.orderDate)
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
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .padding(bottom = 76.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = Cyan500, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "فحص الجودة والاستلام | HACCP",
                                color = Stone100,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                        }
                        Text(
                            text = "توثيق استلام الشحنات ومطابقة درجات الحرارة والمواصفات",
                            color = Stone400,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Cyan500.copy(alpha = 0.15f))
                            .border(1.dp, Cyan500.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${receivingOrders.size} في الانتظار",
                            color = Cyan500,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 24-Hour Vegetable Delay Critical Alarm
                if (delayedVegOrders.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Rose500.copy(alpha = 0.15f)),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(listOf(Rose500, Rose500.copy(alpha = 0.5f)))
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Alarm, contentDescription = null, tint = Rose400, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "إنذار حرج: تأخير توريد الخضار أكثر من 24 ساعة!",
                                    color = Rose400,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "يوجد ${delayedVegOrders.size} طلبيات خضار تجاوزت موعد 01:00 ظهراً لأكثر من 24 ساعة.",
                                    color = Stone300,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                // Success Message Banner
                uiState.successMessage?.let { msg ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Emerald500.copy(alpha = 0.15f)),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Emerald500, Emerald500)))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald400, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = msg, color = Stone100, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            IconButton(onClick = { viewModel.clearSuccessMessage() }) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = Stone400, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                // Orders Pending Receiving List
                Text(
                    text = "شحنات التوريد قيد الانتظار اليوم:",
                    color = Amber400,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (receivingOrders.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.DoneAll, contentDescription = null, tint = Emerald400, modifier = Modifier.size(36.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "لا توجد شحنات معلقة قيد الاستلام حالياً",
                                        color = Stone400,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "جميع طلبيات المطبخ مستلمة أو تم فحصها",
                                        color = Stone500,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    } else {
                        items(receivingOrders, key = { it.id }) { order ->
                            ReceivingOrderCard(
                                order = order,
                                onInspectClick = { viewModel.selectOrderForReceiving(order) }
                            )
                        }
                    }
                }
            }

            // Receiving Inspection Dialog Modal
            if (uiState.selectedOrder != null) {
                InspectionDialog(
                    order = uiState.selectedOrder!!,
                    items = uiState.selectedOrderItems,
                    packageCondition = uiState.packageCondition,
                    productCondition = uiState.productCondition,
                    temperatureCelsius = uiState.temperatureCelsius,
                    inspectionResult = uiState.inspectionResult,
                    rejectionReason = uiState.rejectionReason,
                    notes = uiState.notes,
                    receivedQuantities = uiState.receivedQuantities,
                    rejectedQuantities = uiState.rejectedQuantities,
                    isSubmitting = uiState.isSubmitting,
                    onPackageConditionChange = { viewModel.setPackageCondition(it) },
                    onProductConditionChange = { viewModel.setProductCondition(it) },
                    onTemperatureChange = { viewModel.setTemperature(it) },
                    onInspectionResultChange = { viewModel.setInspectionResult(it) },
                    onRejectionReasonChange = { viewModel.setRejectionReason(it) },
                    onNotesChange = { viewModel.setNotes(it) },
                    onItemQuantityChange = { id, rec, rej -> viewModel.updateItemQuantities(id, rec, rej) },
                    onSubmit = { viewModel.submitInspection(currentUser) },
                    onDismiss = { viewModel.clearSelectedOrder() }
                )
            }
        }
    }
}

@Composable
private fun ReceivingOrderCard(
    order: OrderEntity,
    onInspectClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Stone900),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(Stone800, Cyan500.copy(alpha = 0.3f)))
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Cyan500.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, tint = Cyan500, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = order.categoryNameAr,
                            color = Stone100,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                        Text(
                            text = order.id,
                            color = Amber400,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Stone950)
                        .border(1.dp, Stone800, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "وصول: ${order.fixedArrivalHour}",
                        color = Stone300,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "مسؤول الوردية: ${order.primaryAssigneeName}",
                    color = Stone400,
                    fontSize = 11.sp
                )

                Button(
                    onClick = onInspectClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Cyan500),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = Stone950, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "فحص واستلام الشحنة",
                        color = Stone950,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun InspectionDialog(
    order: OrderEntity,
    items: List<OrderItemEntity>,
    packageCondition: String,
    productCondition: String,
    temperatureCelsius: String,
    inspectionResult: String,
    rejectionReason: String,
    notes: String,
    receivedQuantities: Map<Long, Double>,
    rejectedQuantities: Map<Long, Double>,
    isSubmitting: Boolean,
    onPackageConditionChange: (String) -> Unit,
    onProductConditionChange: (String) -> Unit,
    onTemperatureChange: (String) -> Unit,
    onInspectionResultChange: (String) -> Unit,
    onRejectionReasonChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onItemQuantityChange: (Long, Double, Double) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    val tempDouble = temperatureCelsius.toDoubleOrNull()
    val isTempExceeded = tempDouble != null && tempDouble > 4.0

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(22.dp),
            color = Stone900,
            border = androidx.compose.foundation.BorderStroke(1.dp, Stone700)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = Stone400)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "فحص استلام: ${order.categoryNameAr}",
                                color = Cyan500,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                            Text(
                                text = order.id,
                                color = Stone400,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Box(modifier = Modifier.size(40.dp))
                    }
                }

                // Temperature measurement with HACCP Check
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Stone950),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(
                                if (isTempExceeded) listOf(Rose500, Rose500)
                                else listOf(Stone800, Cyan500.copy(alpha = 0.3f))
                            )
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Thermostat,
                                        contentDescription = null,
                                        tint = if (isTempExceeded) Rose400 else Cyan500,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "درجة الحرارة المقاسة (°C):",
                                        color = Stone200,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }

                                OutlinedTextField(
                                    value = temperatureCelsius,
                                    onValueChange = onTemperatureChange,
                                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                                    ),
                                    modifier = Modifier
                                        .width(90.dp)
                                        .height(44.dp),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Stone900,
                                        unfocusedContainerColor = Stone900,
                                        focusedBorderColor = if (isTempExceeded) Rose500 else Cyan500,
                                        unfocusedBorderColor = Stone800,
                                        focusedTextColor = if (isTempExceeded) Rose400 else Stone100,
                                        unfocusedTextColor = Stone100
                                    )
                                )
                            }

                            if (isTempExceeded) {
                                Text(
                                    text = "تحذير هاسب: درجة الحرارة تجاوزت الحد المسموح (4.0°C)",
                                    color = Rose400,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }

                // Packaging condition selector
                item {
                    Text("حالة التغليف والعبوات:", color = Stone300, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val conditions = listOf(
                            "intact" to "سليم",
                            "acceptable" to "مقبول",
                            "opened" to "مفتوح",
                            "damaged" to "تالف"
                        )
                        conditions.forEach { (code, label) ->
                            val isSel = packageCondition == code
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) Amber500 else Stone950)
                                    .border(1.dp, if (isSel) Amber400 else Stone800, RoundedCornerShape(8.dp))
                                    .clickable { onPackageConditionChange(code) }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSel) Stone950 else Stone300,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Overall Inspection Result
                item {
                    Text("القرار النهائي للفحص:", color = Stone300, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val results = listOf(
                            "ACCEPTED" to "قبول بالكامل",
                            "PARTIALLY_ACCEPTED" to "قبول جزئي",
                            "REJECTED" to "رفض الشحنة"
                        )
                        results.forEach { (code, label) ->
                            val isSel = inspectionResult == code
                            val selColor = when (code) {
                                "ACCEPTED" -> Emerald500
                                "PARTIALLY_ACCEPTED" -> Amber500
                                else -> Rose500
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) selColor else Stone950)
                                    .border(1.dp, if (isSel) selColor else Stone800, RoundedCornerShape(8.dp))
                                    .clickable { onInspectionResultChange(code) }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSel) Stone950 else Stone300,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }

                // Items list quantities confirmation
                item {
                    Text("مطابقة كميات الأصناف:", color = Stone300, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                items(items, key = { it.id }) { item ->
                    val recQty = receivedQuantities[item.id] ?: item.orderedQty
                    val rejQty = rejectedQuantities[item.id] ?: 0.0

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Stone950),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Stone800, Stone800)))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.productName, color = Stone100, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("المطلوب: ${item.orderedQty} ${item.unit}", color = Stone400, fontSize = 10.sp)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Accept all shortcut button
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Emerald500.copy(alpha = 0.2f))
                                        .clickable { onItemQuantityChange(item.id, item.orderedQty, 0.0) }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("مطابق", color = Emerald400, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Submit Button
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = onSubmit,
                        enabled = !isSubmitting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald600)
                    ) {
                        Text(
                            text = if (isSubmitting) "جاري التوثيق..." else "اعتماد وتوثيق الفحص في السجل",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
