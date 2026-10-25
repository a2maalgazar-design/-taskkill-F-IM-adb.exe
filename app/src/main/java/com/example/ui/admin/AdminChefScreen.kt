package com.example.ui.admin

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.SystemSettingsEntity
import com.example.data.local.entity.UserEntity
import com.example.data.repository.KitchenRepository
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
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
import kotlinx.coroutines.launch

@Composable
fun AdminChefScreen(
    repository: KitchenRepository,
    currentUser: UserEntity,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val categories by repository.allCategories.collectAsStateWithLifecycle(initialValue = emptyList())
    val users by repository.allActiveUsers.collectAsStateWithLifecycle(initialValue = emptyList())
    val settingsEntity by repository.settingsFlow.collectAsStateWithLifecycle(initialValue = null)

    var purchasingPhoneInput by remember(settingsEntity) {
        mutableStateOf(settingsEntity?.purchasingPhone ?: "+201099887766")
    }
    var chefPhoneInput by remember(settingsEntity) {
        mutableStateOf(settingsEntity?.headChefPhone ?: "+201000000000")
    }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Stone950)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .padding(bottom = 76.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Settings, contentDescription = null, tint = Amber500, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "لوحة تحكم الشيف وإعدادات النظام",
                                    color = Stone100,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 17.sp
                                )
                            }
                            Text(
                                text = "توزيع المهام، مواعيد التوريد، وأرقام هواتف الواتساب",
                                color = Stone400,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        // Logout Button
                        Button(
                            onClick = onLogout,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Stone800),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Rose400, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("خروج", color = Rose400, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Status Message Toast
                statusMessage?.let { msg ->
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Emerald500.copy(alpha = 0.15f)),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Emerald500, Emerald500)))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald400, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = msg, color = Stone100, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Master Contact Settings Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Stone900),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Stone800, Amber500.copy(alpha = 0.3f))))
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = Amber400, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("أرقام الواتساب المركزية للمطبخ:", color = Amber400, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            Column {
                                Text("رقم مسؤول المشتريات المركزي:", color = Stone300, fontSize = 11.sp)
                                OutlinedTextField(
                                    value = purchasingPhoneInput,
                                    onValueChange = { purchasingPhoneInput = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Stone950,
                                        unfocusedContainerColor = Stone950,
                                        focusedBorderColor = Amber500,
                                        unfocusedBorderColor = Stone800,
                                        focusedTextColor = Stone100,
                                        unfocusedTextColor = Stone100
                                    ),
                                    singleLine = true
                                )
                            }

                            Column {
                                Text("رقم هاتف الشيف العمومي (نسخة الواتساب الإلزامية CC):", color = Stone300, fontSize = 11.sp)
                                OutlinedTextField(
                                    value = chefPhoneInput,
                                    onValueChange = { chefPhoneInput = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Stone950,
                                        unfocusedContainerColor = Stone950,
                                        focusedBorderColor = Amber500,
                                        unfocusedBorderColor = Stone800,
                                        focusedTextColor = Stone100,
                                        unfocusedTextColor = Stone100
                                    ),
                                    singleLine = true
                                )
                            }

                            Button(
                                onClick = {
                                    scope.launch {
                                        repository.saveSettings(
                                            SystemSettingsEntity(
                                                key = "default",
                                                purchasingPhone = purchasingPhoneInput.trim(),
                                                headChefPhone = chefPhoneInput.trim()
                                            )
                                        )
                                        statusMessage = "تم حفظ أرقام هواتف التواصل المركزية"
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Amber500)
                            ) {
                                Icon(Icons.Default.Save, contentDescription = null, tint = Stone950, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("حفظ أرقام الهواتف", color = Stone950, fontWeight = FontWeight.Black, fontSize = 12.sp)
                            }
                        }
                    }
                }

                // Department Recurrence & Delegation Management
                item {
                    Text(
                        text = "جدولة وتوزيع أقسام المطبخ الـ 12 على المساعدين:",
                        color = Amber400,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(categories, key = { it.id }) { cat ->
                    CategoryAdminCard(
                        category = cat,
                        users = users,
                        onUpdate = { updated ->
                            scope.launch {
                                repository.saveCategory(updated)
                                statusMessage = "تم تحديث قسم ${cat.nameAr}"
                            }
                        }
                    )
                }

                // Master Reset Button
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Stone900),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Stone800, Rose500.copy(alpha = 0.2f))))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "إعادة ضبط الكتالوج القياسي الافتراضي:",
                                color = Rose400,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "إعادة تحميل الـ 180+ صنف الأصلية والأقسام الـ 12 المعتمدة من المطعم",
                                color = Stone400,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                            )
                            Button(
                                onClick = {
                                    scope.launch {
                                        repository.resetCatalogToDefault()
                                        statusMessage = "تم إعادة تعيين الكتالوج والمعدلات بنجاح"
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Stone800)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, tint = Amber400, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("استعادة الكتالوج الافتراضي الكامل", color = Stone200, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryAdminCard(
    category: CategoryEntity,
    users: List<UserEntity>,
    onUpdate: (CategoryEntity) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Stone900),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Stone800, Stone800)))
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(category.icon, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(category.nameAr, color = Stone100, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(category.scheduleTiming, color = Amber400, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }
                }

                // Recurrence Chips
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    val recurrences = listOf("daily" to "يومي", "weekly" to "أسبوعي", "monthly" to "شهري")
                    recurrences.forEach { (code, label) ->
                        val isSel = category.recurrence == code
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSel) Amber500 else Stone950)
                                .clickable { onUpdate(category.copy(recurrence = code)) }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = label,
                                color = if (isSel) Stone950 else Stone400,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Assignee Delegation
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val assignedUser = users.find { it.id == category.defaultAssigneeId }
                Text(
                    text = "المساعد المكلف: ${assignedUser?.name ?: "الشيف العمومي"}",
                    color = Stone300,
                    fontSize = 11.sp
                )

                // Quick delegate to next assistant
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Stone800)
                        .clickable {
                            val assistants = users.filter { it.role == "assistant" }
                            val currentIndex = assistants.indexOfFirst { it.id == category.defaultAssigneeId }
                            val nextAssistant = if (currentIndex == -1 || currentIndex == assistants.size - 1) {
                                assistants.firstOrNull()
                            } else {
                                assistants[currentIndex + 1]
                            }
                            if (nextAssistant != null) {
                                onUpdate(category.copy(defaultAssigneeId = nextAssistant.id))
                            }
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("تبديل المكلف", color = Amber400, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
