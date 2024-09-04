package com.pss_dev.chatapp.feature.auth.signin

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<SignInStates>(SignInStates.Nothing)
    val state = _state.asStateFlow()

    fun signIn(email: String, password: String) {
        _state.value = SignInStates.Loading
        // Perform sign-in logic here
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let {

                        _state.value = SignInStates.Success
                        return@addOnCompleteListener
                    }
                    _state.value = SignInStates.Error
                } else {
                    _state.value = SignInStates.Error
                }
            }
        //check

    }

}

sealed class SignInStates {
    object Nothing : SignInStates()
    object Loading : SignInStates()
    object Success : SignInStates()
    object Error : SignInStates()
}