package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.repository.KitchenRepository
import com.example.ui.admin.AdminChefScreen
import com.example.ui.auth.AuthPinScreen
import com.example.ui.auth.AuthViewModel
import com.example.ui.history.HistoryScreen
import com.example.ui.orders.OrdersScreen
import com.example.ui.orders.OrdersViewModel
import com.example.ui.receiving.ReceivingScreen
import com.example.ui.receiving.ReceivingViewModel
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.Cyan500
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Stone100
import com.example.ui.theme.Stone400
import com.example.ui.theme.Stone500
import com.example.ui.theme.Stone700
import com.example.ui.theme.Stone800
import com.example.ui.theme.Stone900
import com.example.ui.theme.Stone950

enum class KomsTab(val title: String) {
    ORDERS("الطلبيات"),
    RECEIVING("الاستلام"),
    HISTORY("السجل"),
    ADMIN("الإعدادات")
}

@Composable
fun KomsMainScreen(
    repository: KitchenRepository,
    authViewModel: AuthViewModel,
    ordersViewModel: OrdersViewModel,
    receivingViewModel: ReceivingViewModel,
    modifier: Modifier = Modifier
) {
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()
    val activeOrders by ordersViewModel.activeOrders.collectAsStateWithLifecycle()

    val awaitingReceivingCount = remember(activeOrders) {
        activeOrders.count { it.status in listOf("ORDERED", "AWAITING_DELIVERY", "RECEIVING") }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        if (!authState.isLoggedIn || authState.currentUser == null) {
            AuthPinScreen(viewModel = authViewModel)
        } else {
            val currentUser = authState.currentUser!!
            var currentTab by remember { mutableStateOf(KomsTab.ORDERS) }

            Scaffold(
                modifier = modifier
                    .fillMaxSize()
                    .background(Stone950),
                bottomBar = {
                    NavigationBar(
                        containerColor = Stone900,
                        contentColor = Stone100,
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                    ) {
                        // Orders Tab
                        NavigationBarItem(
                            selected = currentTab == KomsTab.ORDERS,
                            onClick = { currentTab = KomsTab.ORDERS },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "الطلبيات",
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = "الطلبيات",
                                    fontSize = 11.sp,
                                    fontWeight = if (currentTab == KomsTab.ORDERS) FontWeight.Black else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Stone950,
                                selectedTextColor = Amber400,
                                unselectedIconColor = Stone400,
                                unselectedTextColor = Stone500,
                                indicatorColor = Amber500
                            )
                        )

                        // Receiving & QA Tab
                        NavigationBarItem(
                            selected = currentTab == KomsTab.RECEIVING,
                            onClick = { currentTab = KomsTab.RECEIVING },
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (awaitingReceivingCount > 0) {
                                            Badge(
                                                containerColor = Cyan500,
                                                contentColor = Stone950
                                            ) {
                                                Text(
                                                    text = "$awaitingReceivingCount",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 9.sp
                                                )
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = "فحص الاستلام",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = "الاستلام",
                                    fontSize = 11.sp,
                                    fontWeight = if (currentTab == KomsTab.RECEIVING) FontWeight.Black else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Stone950,
                                selectedTextColor = Cyan500,
                                unselectedIconColor = Stone400,
                                unselectedTextColor = Stone500,
                                indicatorColor = Cyan500
                            )
                        )

                        // History Tab
                        NavigationBarItem(
                            selected = currentTab == KomsTab.HISTORY,
                            onClick = { currentTab = KomsTab.HISTORY },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = "السجل",
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = "السجل",
                                    fontSize = 11.sp,
                                    fontWeight = if (currentTab == KomsTab.HISTORY) FontWeight.Black else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Stone950,
                                selectedTextColor = Amber400,
                                unselectedIconColor = Stone400,
                                unselectedTextColor = Stone500,
                                indicatorColor = Amber500
                            )
                        )

                        // Settings / Admin Tab
                        NavigationBarItem(
                            selected = currentTab == KomsTab.ADMIN,
                            onClick = { currentTab = KomsTab.ADMIN },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "الإعدادات",
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = "الإعدادات",
                                    fontSize = 11.sp,
                                    fontWeight = if (currentTab == KomsTab.ADMIN) FontWeight.Black else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Stone950,
                                selectedTextColor = Amber400,
                                unselectedIconColor = Stone400,
                                unselectedTextColor = Stone500,
                                indicatorColor = Amber500
                            )
                        )
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Stone950)
                ) {
                    AnimatedContent(
                        targetState = currentTab,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "KomsTabTransition"
                    ) { targetTab ->
                        when (targetTab) {
                            KomsTab.ORDERS -> OrdersScreen(
                                viewModel = ordersViewModel,
                                currentUser = currentUser
                            )
                            KomsTab.RECEIVING -> ReceivingScreen(
                                viewModel = receivingViewModel,
                                currentUser = currentUser
                            )
                            KomsTab.HISTORY -> HistoryScreen(
                                repository = repository
                            )
                            KomsTab.ADMIN -> AdminChefScreen(
                                repository = repository,
                                currentUser = currentUser,
                                onLogout = { authViewModel.logout() }
                            )
                        }
                    }
                }
            }
        }
    }
}
