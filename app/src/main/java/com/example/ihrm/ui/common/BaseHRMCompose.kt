package com.example.ihrm.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.example.ihrm.core.viewmodel.BaseViewmodel

@Composable
fun BaseHRMCompose(
    content: @Composable () -> Unit,
    viewmodel: BaseViewmodel,
    onErrorAlertClose: (() -> Unit)? = null
) {
    val currentContent by rememberUpdatedState(newValue = content)
    Box(modifier = Modifier.fillMaxSize()) {
        currentContent()
        //loading
        ErrorAlert(
            viewmodel = viewmodel,
            onErrorAlertClose = onErrorAlertClose
        )

    }
}