package com.example.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.UserEntity
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Rose400
import com.example.ui.theme.Rose500
import com.example.ui.theme.Stone100
import com.example.ui.theme.Stone300
import com.example.ui.theme.Stone400
import com.example.ui.theme.Stone500
import com.example.ui.theme.Stone700
import com.example.ui.theme.Stone800
import com.example.ui.theme.Stone850
import com.example.ui.theme.Stone900
import com.example.ui.theme.Stone950

@Composable
fun AuthPinScreen(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val users by viewModel.users.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Stone950)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Brand Header Emblem
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(listOf(Amber500, Amber400))
                        )
                        .border(1.5.dp, Amber400, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = "KOMS Logo",
                        tint = Stone950,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "نظام إدارة المطبخ | KOMS",
                    color = Stone100,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "منظومة تسجيل وتوريد واستلام طلبيات المطاعم الاحترافية",
                    color = Stone400,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                )

                // Instruction Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Stone900),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Stone800, Amber500.copy(alpha = 0.3f))))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "اختر حسابك لبدء وردية العمل",
                                color = Amber400,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "تسجيل فوري وسريع برمز PIN المكون من 4 أرقام",
                                color = Stone400,
                                fontSize = 11.sp
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = Amber500,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Staff Cards List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(users) { user ->
                        StaffCard(
                            user = user,
                            onSelect = { viewModel.selectUserForPin(user) },
                            onQuickLogin = { viewModel.quickDirectLogin(user) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "النسخة المجانية للمطابخ التجارية • تعمل بدون إنترنت 100%",
                    color = Stone500,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
            }

            // PIN Entry Dialog
            if (uiState.selectedUserForPin != null) {
                PinDialog(
                    user = uiState.selectedUserForPin!!,
                    pinInput = uiState.pinInput,
                    pinError = uiState.pinError,
                    onDigitClick = { viewModel.enterPinDigit(it) },
                    onDeleteClick = { viewModel.deletePinDigit() },
                    onDismiss = { viewModel.clearSelectedUser() },
                    onBypass = { viewModel.quickDirectLogin(uiState.selectedUserForPin!!) }
                )
            }
        }
    }
}

@Composable
private fun StaffCard(
    user: UserEntity,
    onSelect: () -> Unit,
    onQuickLogin: () -> Unit
) {
    val isManager = user.role == "manager"
    val isReceiver = user.role == "receiver"

    val badgeColor = when {
        isManager -> Amber500
        isReceiver -> Color(0xFF06B6D4) // Cyan
        else -> Emerald500
    }

    val roleLabel = when {
        isManager -> "الشيف العمومي (Admin)"
        isReceiver -> "مسؤول الاستلام وضبط الجودة"
        else -> "مساعد شيف قسم"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onSelect() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Stone900),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(Stone800, badgeColor.copy(alpha = 0.4f))
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Avatar Badge
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(badgeColor.copy(alpha = 0.15f))
                        .border(1.dp, badgeColor.copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.name.firstOrNull()?.toString() ?: "ش",
                        color = badgeColor,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = user.name,
                            color = Stone100,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Stone950)
                                .border(0.8.dp, Stone700, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = user.employeeNumber,
                                color = Amber400,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = roleLabel,
                        color = badgeColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // Enter Button
            Button(
                onClick = onSelect,
                colors = ButtonDefaults.buttonColors(containerColor = Amber500),
                shape = RoundedCornerShape(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "دخول",
                    color = Stone950,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun PinDialog(
    user: UserEntity,
    pinInput: String,
    pinError: Boolean,
    onDigitClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onDismiss: () -> Unit,
    onBypass: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            color = Stone900,
            border = androidx.compose.foundation.BorderStroke(1.dp, Stone700)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dialog Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = Stone400
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "رمز الدخول PIN",
                            color = Amber400,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = user.name,
                            color = Stone100,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Box(modifier = Modifier.size(40.dp)) // balancing spacer
                }

                Spacer(modifier = Modifier.height(16.dp))

                // PIN Dots Display
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {
                    repeat(4) { index ->
                        val isFilled = index < pinInput.length
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        pinError -> Rose500
                                        isFilled -> Amber500
                                        else -> Stone800
                                    }
                                )
                                .border(
                                    1.dp,
                                    if (pinError) Rose400 else if (isFilled) Amber400 else Stone700,
                                    CircleShape
                                )
                        )
                    }
                }

                if (pinError) {
                    Text(
                        text = "رمز PIN غير صحيح. جرب 1234 أو الدخول المباشر",
                        color = Rose400,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                    )
                } else {
                    Text(
                        text = "الرمز الافتراضي: 1234 أو انقر للدخول المباشر",
                        color = Stone400,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                    )
                }

                // Numeric Keypad (1 to 9, 0, delete)
                val keypadRows = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("bypass", "0", "delete")
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    for (row in keypadRows) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (key in row) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(56.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(
                                            when (key) {
                                                "delete" -> Stone850
                                                "bypass" -> Amber500.copy(alpha = 0.15f)
                                                else -> Stone800
                                            }
                                        )
                                        .border(
                                            1.dp,
                                            when (key) {
                                                "bypass" -> Amber500.copy(alpha = 0.4f)
                                                else -> Stone700
                                            },
                                            RoundedCornerShape(14.dp)
                                        )
                                        .clickable {
                                            when (key) {
                                                "delete" -> onDeleteClick()
                                                "bypass" -> onBypass()
                                                else -> onDigitClick(key)
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    when (key) {
                                        "delete" -> {
                                            Icon(
                                                imageVector = Icons.Default.Backspace,
                                                contentDescription = "مسح",
                                                tint = Rose400,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        "bypass" -> {
                                            Text(
                                                text = "مباشر",
                                                color = Amber400,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        else -> {
                                            Text(
                                                text = key,
                                                color = Stone100,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
