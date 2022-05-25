package com.zeroboss.scoring500.presentation.screens.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isValid: Boolean = false
)