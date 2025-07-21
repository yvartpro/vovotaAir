package bi.vovota.vovota.viewmodel

import android.app.Application
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bi.vovota.vovota.AuthRepository
import bi.vovota.vovota.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val token: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(private val tokenManager: TokenManager) : ViewModel() {
    private val repo = AuthRepository()

    val tokenStateFlow: StateFlow<String?> = tokenManager.tokenFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    fun saveToken(token: String) {
        viewModelScope.launch {
            tokenManager.saveToken(token)
        }
    }
    fun clearToken() {
        viewModelScope.launch {
            tokenManager.clearToken()
        }
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(name: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = repo.login(name, password)
                _authState.value = AuthState.Success(response.access)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun register(name: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = repo.register(name, password)
                println("Resp: $response")
                _authState.value = AuthState.Success(response.toString())
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Register failed")
            }
        }
    }
}

