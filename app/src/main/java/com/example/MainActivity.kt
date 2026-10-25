package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.data.local.KomsDatabase
import com.example.data.repository.KitchenRepository
import com.example.ui.KomsMainScreen
import com.example.ui.auth.AuthViewModel
import com.example.ui.auth.AuthViewModelFactory
import com.example.ui.orders.OrdersViewModel
import com.example.ui.orders.OrdersViewModelFactory
import com.example.ui.receiving.ReceivingViewModel
import com.example.ui.receiving.ReceivingViewModelFactory
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val database by lazy { KomsDatabase.getDatabase(this) }
    private val repository by lazy { KitchenRepository(database) }

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(repository)
    }

    private val ordersViewModel: OrdersViewModel by viewModels {
        OrdersViewModelFactory(repository)
    }

    private val receivingViewModel: ReceivingViewModel by viewModels {
        ReceivingViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                KomsMainScreen(
                    repository = repository,
                    authViewModel = authViewModel,
                    ordersViewModel = ordersViewModel,
                    receivingViewModel = receivingViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
