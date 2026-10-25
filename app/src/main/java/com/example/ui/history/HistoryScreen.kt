package com.example.ui.history

import android.content.Context
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.data.repository.KitchenRepository
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
import com.example.util.WhatsAppUtil
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(
    repository: KitchenRepository,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val allOrders by repository.allActiveOrders.collectAsStateWithLifecycle(initialValue = emptyList())
    var selectedStatusFilter by remember { mutableStateOf("ALL") }
    var selectedOrderForDetail by remember { mutableStateOf<OrderEntity?>(null) }
    var orderItemsForDetail by remember { mutableStateOf<List<OrderItemEntity>>(emptyList()) }

    val filteredOrders = remember(allOrders, selectedStatusFilter) {
        if (selectedStatusFilter == "ALL") allOrders
        else allOrders.filter { it.status == selectedStatusFilter }
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
                            Icon(Icons.Default.History, contentDescription = null, tint = Amber500, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "سجل الطلبيات والمتابعة",
                                color = Stone100,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                        }
                        Text(
                            text = "تتبع مراحل دورة حياة طلبيات المطبخ والموردين",
                            color = Stone400,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Amber500.copy(alpha = 0.15f))
                            .border(1.dp, Amber500.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${filteredOrders.size} طلبية",
                            color = Amber400,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Status Filter Chips
                val statusFilters = listOf(
                    "ALL" to "الكل",
                    "ORDERED" to "تم الطلب",
                    "AWAITING_DELIVERY" to "بانتظار التوصيل",
                    "RECEIVING" to "قيد الاستلام",
                    "COMPLETED" to "مكتمل",
                    "PARTIALLY_RECEIVED" to "استلام جزئي"
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(statusFilters) { (code, label) ->
                        val isSelected = selectedStatusFilter == code
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Amber500 else Stone900)
                                .border(1.dp, if (isSelected) Amber400 else Stone800, RoundedCornerShape(10.dp))
                                .clickable { selectedStatusFilter = code }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Stone950 else Stone300,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Orders List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (filteredOrders.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.ListAlt, contentDescription = null, tint = Stone500, modifier = Modifier.size(36.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("لا توجد طلبيات مطابقة لهذا الفلتر", color = Stone400, fontSize = 13.sp)
                                }
                            }
                        }
                    } else {
                        items(filteredOrders, key = { it.id }) { order ->
                            OrderCard(
                                order = order,
                                onClick = {
                                    scope.launch {
                                        val items = repository.getOrderItemsSync(order.id)
                                        orderItemsForDetail = items
                                        selectedOrderForDetail = order
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // Order Detail Dialog
            selectedOrderForDetail?.let { order ->
                OrderDetailDialog(
                    order = order,
                    items = orderItemsForDetail,
                    onArchive = {
                        scope.launch {
                            repository.archiveOrder(order.id)
                            selectedOrderForDetail = null
                        }
                    },
                    onReshareWhatsApp = {
                        val dispatched = orderItemsForDetail.map {
                            WhatsAppUtil.DispatchedOrderItem(it.productName, it.orderedQty, it.unit)
                        }
                        val msg = WhatsAppUtil.buildKitchenOrderMessage(
                            orderCode = order.id,
                            categoryName = order.categoryNameAr,
                            requesterName = order.primaryAssigneeName,
                            items = dispatched,
                            notes = order.notes
                        )
                        WhatsAppUtil.openWhatsApp(context, order.recipientPhone, msg)
                    },
                    onDismiss = { selectedOrderForDetail = null }
                )
            }
        }
    }
}

@Composable
private fun OrderCard(
    order: OrderEntity,
    onClick: () -> Unit
) {
    val statusColor = when (order.status) {
        "COMPLETED" -> Emerald500
        "RECEIVING", "PARTIALLY_RECEIVED" -> Cyan500
        "AWAITING_DELIVERY" -> Amber500
        "ORDERED" -> Color(0xFF60A5FA) // Blue
        "PARTIALLY_REJECTED", "CANCELLED" -> Rose500
        else -> Stone400
    }

    val statusLabel = when (order.status) {
        "ORDERED" -> "تم الطلب"
        "AWAITING_DELIVERY" -> "بانتظار التوصيل"
        "RECEIVING" -> "جاري الاستلام"
        "COMPLETED" -> "مكتمل ومستلم"
        "PARTIALLY_RECEIVED" -> "استلام جزئي"
        "PARTIALLY_REJECTED" -> "مرفوض جزئياً"
        "CANCELLED" -> "ملغي"
        else -> order.status
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Stone900),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(Stone800, statusColor.copy(alpha = 0.3f)))
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = order.categoryNameAr,
                        color = Stone100,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = order.id,
                        color = Amber400,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .border(1.dp, statusColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = statusLabel,
                        color = statusColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Stone500, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(order.primaryAssigneeName, color = Stone400, fontSize = 11.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Stone500, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(order.orderDate, color = Stone400, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}

@Composable
private fun OrderDetailDialog(
    order: OrderEntity,
    items: List<OrderItemEntity>,
    onArchive: () -> Unit,
    onReshareWhatsApp: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(22.dp),
            color = Stone900,
            border = androidx.compose.foundation.BorderStroke(1.dp, Stone700)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header
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
                            text = order.categoryNameAr,
                            color = Amber400,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${order.id} • ${order.orderDate}",
                            color = Stone400,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Box(modifier = Modifier.size(40.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Items List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .height(280.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(items, key = { it.id }) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Stone950),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Stone800, Stone800)))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.productName, color = Stone200, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "مطلوب: ${item.orderedQty} ${item.unit}",
                                        color = Amber400,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (item.receivedQty > 0) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "مستلم: ${item.receivedQty}",
                                            color = Emerald400,
                                            fontSize = 11.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onReshareWhatsApp,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald600)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("مشاركة واتساب", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    if (order.status == "COMPLETED") {
                        Button(
                            onClick = onArchive,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Stone800)
                        ) {
                            Icon(Icons.Default.Archive, contentDescription = null, tint = Stone400, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("أرشفة", color = Stone300, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
