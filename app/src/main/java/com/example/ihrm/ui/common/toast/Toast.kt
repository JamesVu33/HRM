package com.example.ihrm.ui.common.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.ihrm.R
import com.example.ihrm.ui.theme.ApprovedText
import com.example.ihrm.ui.theme.AvatarGreenBg
import com.example.ihrm.ui.theme.BgErrorMessage
import com.example.ihrm.ui.theme.ErrorText

@Composable
fun Toast(
    text: String,
    timeout: Long = 3000L,
    position: ToastPosition = ToastPosition.TOP,
    type: ToastType = ToastType.DEFAULT
) {
    val (visible, setVisible) = remember {
        mutableStateOf(true)
    }

    var iconId = R.drawable.icon_fill_error_24
    var background = Color(0xE6222428)
    var textColor = Color.White
    when (type) {
        ToastType.DEFAULT -> {
            iconId = R.drawable.icon_fill_informative_24
        }

        ToastType.ERROR -> {
            iconId = R.drawable.icon_fill_error_24
            background = BgErrorMessage
            textColor = ErrorText
        }

        ToastType.NOTICE -> {
            iconId = com.example.ihrm.R.drawable.icon_fill_notice_24
        }

        ToastType.INFORMATION -> {
            iconId = R.drawable.icon_fill_informative_24
        }

        ToastType.SUCCESS -> {
            iconId = R.drawable.icon_fill_check_24
            background = AvatarGreenBg
            textColor = ApprovedText
        }

        is ToastType.CustomIcon -> {
            iconId = type.iconRes
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = keyframes {
                this.durationMillis = 500
            }
        ),
        exit = fadeOut(
            animationSpec = keyframes {
                this.durationMillis = 500
            }
        )
    ) {
        ToastLayout(
            textColor = textColor,
            background = background,
            text = text,
            position = position,
            type = type,
            content = {
                Image(
                    painter = painterResource(id = iconId),
                    contentDescription = null,
                )
            })
    }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        coroutineScope.launch {
            delay(timeout)
            setVisible(false)
        }
    }
}

@Preview
@Composable
fun ToastTest() {
    Toast(
        text = "Hello Shinhan Global Banking. This is Toast Sample.",
        position = ToastPosition.TOP
    )
}