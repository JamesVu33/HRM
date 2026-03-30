package com.example.ihrm.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ihrm.core.viewmodel.BaseViewmodel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun BaseHRMCompose(
    content: @Composable () -> Unit,
    viewmodel: BaseViewmodel,
    onErrorAlertClose: (() -> Unit)? = null
) {
    val currentContent by rememberUpdatedState(newValue = content)
    Box(modifier = Modifier.fillMaxSize()) {
        currentContent()
        Loading(viewmodel.isLoading)
        ErrorAlert(
            viewmodel = viewmodel,
            onErrorAlertClose = onErrorAlertClose
        )

    }
}
@Composable
private fun Loading(state: StateFlow<Boolean>) {
    val isLoading by state.collectAsState(false)

    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
