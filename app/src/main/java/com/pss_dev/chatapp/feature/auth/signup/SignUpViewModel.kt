package com.pss_dev.chatapp.feature.auth.signup

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<SignUpState>(SignUpState.Nothing)
    val state = _state.asStateFlow()

    fun signUp(name: String, email: String, password: String) {
        _state.value = SignUpState.Loading
        // Perform sign-in logic here
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // User account created successfully
                    val user = FirebaseAuth.getInstance().currentUser

                    // Update the user's profile with the provided name
                    val profileUpdates = userProfileChangeRequest {
                        displayName = name
                    }

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profiletask ->
                            if (profiletask.isSuccessful) {
                                _state.value = SignUpState.Success
                            } else {
                                _state.value = SignUpState.Error
                            }

                        }
                    //check

                } else {
                    _state.value = SignUpState.Error
                }

            }
    }
}

sealed class SignUpState {
    object Nothing : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    object Error : SignUpState()
}