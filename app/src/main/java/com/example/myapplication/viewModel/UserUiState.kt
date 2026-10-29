package com.example.myapplication.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.User
import com.example.myapplication.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface UserUiState {
    object Loading : UserUiState
    data class Success(val users: List<User>) : UserUiState
    data class Error(val message: String) : UserUiState
    object Empty : UserUiState
}
class UserViewModel (private val repository: UserRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UserUiState> = _searchQuery
        .flatMapLatest { query -> repository.getUsers(query) }
        .map { users ->
            if (users.isEmpty() && _searchQuery.value.isEmpty()) UserUiState.Loading
            else if (users.isEmpty()) UserUiState.Empty
            else UserUiState.Success(users)
        }
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5000), UserUiState.Loading)

    init {
        refresh()
    }

    fun onSearch(query: String) {
        _searchQuery.value = query
    }

    suspend fun getUserById(id: Int): User? {
    return repository.getUserById(id)
}

    class Factory(private val repository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserViewModel(repository) as T
        }
    }

fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                repository.refreshUsers()
            } catch (e: Exception) {
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}