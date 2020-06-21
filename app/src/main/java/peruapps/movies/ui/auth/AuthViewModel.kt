package peruapps.movies.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import peruapps.movies.domain.auth.Auth
import peruapps.movies.domain.auth.AuthUseCase
import peruapps.movies.ui.util.isNotValidEmail
import java.lang.Exception

class AuthViewModel(private val authUseCase: AuthUseCase) : ViewModel() {

    private val _screenStateLiveData = MutableLiveData<AuthScreenState>()

    val authScreenStateLiveData: LiveData<AuthScreenState>
        get() = _screenStateLiveData

    private val _authStateLiveData = MutableLiveData<AuthState>()

    val authStateLiveData: LiveData<AuthState>
        get() = _authStateLiveData

    private val _showMessageErrorLiveData = MutableLiveData<String>()

    val showMessageErrorLiveData: LiveData<String>
        get() = _showMessageErrorLiveData

    var user = AuthModel()

    fun auth() {
        _authStateLiveData.value = AuthState.ErrorDisabled

        if (user.email.isNotValidEmail()) {
            _authStateLiveData.value = AuthState.EmailInvalid
            return
        }

        if (user.password.isBlank()) {
            _authStateLiveData.value = AuthState.PasswordInvalid
            return
        }

        _screenStateLiveData.value = AuthScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authUseCase.auth(
                    Auth(
                        user.email,
                        user.password
                    )
                )
                launch(Dispatchers.Main) {
                    _screenStateLiveData.value = AuthScreenState.Success
                }
            } catch (ex: Exception) {
                launch(Dispatchers.Main) {
                    _screenStateLiveData.value = AuthScreenState.NotLoading
                    _showMessageErrorLiveData.value = ex.message
                }
            }
        }
    }
}