package com.example.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.UserEntity
import com.example.data.repository.KitchenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AuthUiState(
    val currentUser: UserEntity? = null,
    val selectedUserForPin: UserEntity? = null,
    val pinInput: String = "",
    val pinError: Boolean = false,
    val isManager: Boolean = false,
    val isLoggedIn: Boolean = false
)

class AuthViewModel(private val repository: KitchenRepository) : ViewModel() {

    val users: StateFlow<List<UserEntity>> = repository.allActiveUsers
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureDataInitialized()
        }
    }

    fun selectUserForPin(user: UserEntity) {
        _uiState.value = _uiState.value.copy(
            selectedUserForPin = user,
            pinInput = "",
            pinError = false
        )
    }

    fun clearSelectedUser() {
        _uiState.value = _uiState.value.copy(
            selectedUserForPin = null,
            pinInput = "",
            pinError = false
        )
    }

    fun enterPinDigit(digit: String) {
        val currentPin = _uiState.value.pinInput
        if (currentPin.length < 4) {
            val newPin = currentPin + digit
            _uiState.value = _uiState.value.copy(pinInput = newPin, pinError = false)
            if (newPin.length == 4) {
                verifyPinAndLogin(newPin)
            }
        }
    }

    fun deletePinDigit() {
        val currentPin = _uiState.value.pinInput
        if (currentPin.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(
                pinInput = currentPin.dropLast(1),
                pinError = false
            )
        }
    }

    private fun verifyPinAndLogin(pin: String) {
        val user = _uiState.value.selectedUserForPin ?: return
        // Check PIN or default demo master pin "1234"
        if (user.pin == pin || pin == "1234" || user.pin.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                currentUser = user,
                isManager = user.role == "manager",
                isLoggedIn = true,
                selectedUserForPin = null,
                pinInput = "",
                pinError = false
            )
        } else {
            _uiState.value = _uiState.value.copy(pinError = true, pinInput = "")
        }
    }

    fun quickDirectLogin(user: UserEntity) {
        _uiState.value = _uiState.value.copy(
            currentUser = user,
            isManager = user.role == "manager",
            isLoggedIn = true,
            selectedUserForPin = null,
            pinInput = "",
            pinError = false
        )
    }

    fun logout() {
        _uiState.value = AuthUiState(
            currentUser = null,
            isLoggedIn = false
        )
    }
}

class AuthViewModelFactory(private val repository: KitchenRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
