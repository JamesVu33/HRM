package com.example.ihrm.ui.common.toast

import androidx.annotation.FloatRange
import androidx.compose.runtime.Immutable

sealed class ToastPosition {
    data object TOP : ToastPosition()
    data object BOTTOM : ToastPosition()
    data object CENTER : ToastPosition()
    data class Custom(
        @FloatRange(0.0, 1.0)
        val horizontalBias: Float,
        @FloatRange(0.0, 1.0)
        val verticalBias: Float,
    ): ToastPosition()
}

sealed class ToastType {
    data object DEFAULT : ToastType()
    data object ERROR : ToastType()
    data object NOTICE : ToastType()
    data object INFORMATION : ToastType()
    data object SUCCESS : ToastType()
    data class CustomIcon(val iconRes: Int) : ToastType()
}

@Immutable
data class ToastState(
    val message: String,
    val type: ToastType = ToastType.DEFAULT,
    val position: ToastPosition = ToastPosition.TOP,
    val timeout: Long = 3000
)