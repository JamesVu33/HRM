package com.example.ihrm.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ihrm.core.errorHandler.GlobalErrorHandler
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch

@Composable
fun ErrorAlert(
    onErrorAlertClose: (() -> Unit)? = null,
) {
    val coroutineScope = rememberCoroutineScope()

    var errorMsg by remember {
        mutableStateOf<String?>(null)
    }

    var errorVisibility by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        GlobalErrorHandler.globalError.conflate().collect {
            errorMsg = it
            errorVisibility = true
        }
    }

    val onClose: () -> Unit = remember(Unit) {
        {
            coroutineScope.launch {
                errorVisibility = false
                onErrorAlertClose?.invoke()
                errorMsg = null
            }
        }
    }

    if (errorMsg.isNullOrEmpty()) return

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false,
            usePlatformDefaultWidth = false
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = errorVisibility,
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    tonalElevation = 4.dp
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp)
                            .padding(horizontal = 24.dp)
                    ) {
                        Text(errorMsg ?: "")
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = onClose) {
                            Text("Close")
                        }
                    }
                }
            }
        }
    }
}
