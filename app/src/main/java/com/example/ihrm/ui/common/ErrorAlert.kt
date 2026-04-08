package com.example.ihrm.ui.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ihrm.R
import com.example.ihrm.core.errorHandler.GlobalErrorHandler
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.ui.theme.InterFontFamily
import kotlinx.coroutines.flow.conflate
import com.example.ihrm.ui.localization.tr

private val SheetTopCorner = 28.dp
private val SheetShadowAmbient = Color.Black.copy(alpha = 0.15f)
private val DragHandleColor = Color(0xFFD1D5DB)
private val CloseCircleBg = Color(0xFFF3F4F6)
private val CloseIconTint = Color(0xFF9CA3AF)
private val ErrorBodyColor = Color(0xFF6A7282)
private val PrimaryActionBlue = Color(0xFF155DFC)

/**
 * Bottom sheet error UI theo Figma (node 1121:1221): bo góc trên 28dp, drag handle, nút đóng tròn, divider, nội dung + nút Close.
 */
@Composable
fun ErrorAlert(
    viewmodel: BaseViewmodel,
    onErrorAlertClose: (() -> Unit)? = null,
) {
    val errorState by viewmodel.errorEvent.collectAsState()
    var errorMsg by remember { mutableStateOf<String?>(null) }
    Log.d("apiFlows", "ErrorAlert: $errorState")
    if (errorState == null) return

    LaunchedEffect(Unit) {
        GlobalErrorHandler.globalError.conflate().collect {
            errorMsg = it
        }
    }

    val onClose: () -> Unit = {
        onErrorAlertClose?.invoke()
        viewmodel.clearErrorEvent()
        errorMsg = null
    }
    val onCloseThrottled = rememberThrottledClick(onClick = onClose)

    LaunchedEffect(key1 = errorState) {
        val state = errorState ?: return@LaunchedEffect
        errorMsg = state.errorMsg?.takeIf { it.isNotBlank() }
            ?: state.message?.takeIf { it.isNotBlank() }
            ?: state.errorKey.takeIf { it.isNotBlank() }
    }

    if (errorMsg.isNullOrEmpty()) return

    val sheetShape = RoundedCornerShape(topStart = SheetTopCorner, topEnd = SheetTopCorner)

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 24.dp,
                        shape = sheetShape,
                        ambientColor = SheetShadowAmbient,
                        spotColor = SheetShadowAmbient,
                    )
                    .clip(sheetShape)
                    .background(Color.White)
                    .navigationBarsPadding(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(5.dp)
                            .clip(RoundedCornerShape(50))
                            .background(DragHandleColor),
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(CloseCircleBg)
                            .clickable(onClick = onCloseThrottled),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = tr(R.string.error_alert_close_cd),
                            tint = CloseIconTint,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 20.dp, bottom = 24.dp),
                ) {
                    Text(
                        text = errorMsg.orEmpty(),
                        style = TextStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = ErrorBodyColor,
                            letterSpacing = (-0.31).sp,
                        ),
                        textAlign = TextAlign.Start,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onCloseThrottled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryActionBlue,
                            contentColor = Color.White,
                        ),
                    ) {
                        Text(
                            text = tr(R.string.error_alert_close),
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                            ),
                        )
                    }
                }
            }
        }
    }
}
