package com.example.ui.receiving

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.OrderItemEntity
import com.example.data.local.entity.ReceivingInspectionEntity
import com.example.data.local.entity.UserEntity
import com.example.data.repository.KitchenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ReceivingUiState(
    val selectedOrder: OrderEntity? = null,
    val selectedOrderItems: List<OrderItemEntity> = emptyList(),
    val packageCondition: String = "intact", // "intact", "acceptable", "damaged", "opened"
    val productCondition: String = "fresh_compliant", // "fresh_compliant", "minor_issues", "non_compliant_rejected"
    val temperatureCelsius: String = "3.8",
    val inspectionResult: String = "ACCEPTED", // "ACCEPTED", "PARTIALLY_ACCEPTED", "REJECTED"
    val rejectionReason: String = "",
    val notes: String = "",
    val receivedQuantities: Map<Long, Double> = emptyMap(),
    val rejectedQuantities: Map<Long, Double> = emptyMap(),
    val itemRejectionReasons: Map<Long, String> = emptyMap(),
    val isSubmitting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class ReceivingViewModel(private val repository: KitchenRepository) : ViewModel() {

    val activeOrders: StateFlow<List<OrderEntity>> = repository.allActiveOrders
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allInspections: StateFlow<List<ReceivingInspectionEntity>> = repository.allInspections
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow(ReceivingUiState())
    val uiState: StateFlow<ReceivingUiState> = _uiState.asStateFlow()

    fun selectOrderForReceiving(order: OrderEntity) {
        viewModelScope.launch {
            val items = repository.getOrderItemsSync(order.id)
            val recMap = mutableMapOf<Long, Double>()
            val rejMap = mutableMapOf<Long, Double>()
            val reasonMap = mutableMapOf<Long, String>()

            items.forEach { it ->
                recMap[it.id] = it.orderedQty
                rejMap[it.id] = 0.0
                reasonMap[it.id] = ""
            }

            _uiState.value = _uiState.value.copy(
                selectedOrder = order,
                selectedOrderItems = items,
                receivedQuantities = recMap,
                rejectedQuantities = rejMap,
                itemRejectionReasons = reasonMap,
                packageCondition = "intact",
                productCondition = "fresh_compliant",
                temperatureCelsius = "3.8",
                inspectionResult = "ACCEPTED",
                rejectionReason = "",
                notes = "",
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun clearSelectedOrder() {
        _uiState.value = _uiState.value.copy(
            selectedOrder = null,
            selectedOrderItems = emptyList()
        )
    }

    fun setPackageCondition(condition: String) {
        _uiState.value = _uiState.value.copy(packageCondition = condition)
    }

    fun setProductCondition(condition: String) {
        _uiState.value = _uiState.value.copy(productCondition = condition)
    }

    fun setTemperature(temp: String) {
        _uiState.value = _uiState.value.copy(temperatureCelsius = temp)
    }

    fun setInspectionResult(result: String) {
        _uiState.value = _uiState.value.copy(inspectionResult = result)
    }

    fun setRejectionReason(reason: String) {
        _uiState.value = _uiState.value.copy(rejectionReason = reason)
    }

    fun setNotes(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
    }

    fun updateItemQuantities(itemId: Long, receivedQty: Double, rejectedQty: Double, reason: String = "") {
        val rec = _uiState.value.receivedQuantities.toMutableMap()
        val rej = _uiState.value.rejectedQuantities.toMutableMap()
        val rsn = _uiState.value.itemRejectionReasons.toMutableMap()

        rec[itemId] = receivedQty
        rej[itemId] = rejectedQty
        if (reason.isNotBlank()) rsn[itemId] = reason

        _uiState.value = _uiState.value.copy(
            receivedQuantities = rec,
            rejectedQuantities = rej,
            itemRejectionReasons = rsn
        )
    }

    fun submitInspection(inspector: UserEntity) {
        val order = _uiState.value.selectedOrder ?: return
        val items = _uiState.value.selectedOrderItems

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, errorMessage = null)
            try {
                val tempDouble = _uiState.value.temperatureCelsius.toDoubleOrNull()
                val receivedList = items.map { item ->
                    val recQty = _uiState.value.receivedQuantities[item.id] ?: item.orderedQty
                    val rejQty = _uiState.value.rejectedQuantities[item.id] ?: 0.0
                    Triple(item, recQty, rejQty)
                }

                repository.recordReceiving(
                    orderId = order.id,
                    inspector = inspector,
                    packageCondition = _uiState.value.packageCondition,
                    productCondition = _uiState.value.productCondition,
                    temperatureCelsius = tempDouble,
                    inspectionResult = _uiState.value.inspectionResult,
                    rejectionReason = _uiState.value.rejectionReason,
                    notes = _uiState.value.notes,
                    receivedItems = receivedList
                )

                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    selectedOrder = null,
                    selectedOrderItems = emptyList(),
                    successMessage = "تم توثيق فحص استلام الطلبية ${order.id} بنجاح ومطابقة معايير الجودة."
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    errorMessage = "حدث خطأ أثناء حفظ الفحص: ${e.message}"
                )
            }
        }
    }

    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }
}

class ReceivingViewModelFactory(private val repository: KitchenRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReceivingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReceivingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
