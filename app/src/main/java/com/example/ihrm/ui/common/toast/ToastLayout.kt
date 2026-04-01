package com.example.ihrm.ui.common.toast

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.ui.theme.ApprovedText
import com.example.ihrm.ui.theme.AvatarGreenBg
import com.example.ihrm.util.ToastErrorTextStyle13Normal
import com.example.ihrm.util.ToastSuccessTextStyle13Normal

@Composable
fun ToastLayout(
    position: ToastPosition = ToastPosition.BOTTOM,
    type: ToastType = ToastType.DEFAULT,
    content: @Composable () -> Unit = {},
    text: String = "text",
    background: Color = AvatarGreenBg,
    textColor: Color = ApprovedText,
) {
    when (position) {
        ToastPosition.TOP -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(230.dp)
                        .wrapContentHeight()
                        .padding(start = 43.dp, end = 43.dp, top = 20.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xE6222428))
                ) {
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(start = 16.dp, end = 20.dp, top = 12.dp, bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        if (type != ToastType.DEFAULT) {
                            Row {
                                Column(
                                    modifier = Modifier
                                        .size(24.dp, 24.dp),
                                    verticalArrangement = Arrangement.Center
//                                    .background(Blue_700)
                                ) {
                                    content()
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        Row(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                        ) {
                            Text(text = text, color = textColor, style = ToastErrorTextStyle13Normal)
                        }
                    }
                }
            }
        }
        ToastPosition.BOTTOM -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(230.dp)
                        .wrapContentHeight()
                        .padding(start = 43.dp, end = 43.dp, bottom = 96.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(width = 1.dp, color = textColor,RoundedCornerShape(8.dp))
                        .background(background)
                ) {
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(start = 16.dp, end = 20.dp, top = 12.dp, bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        if (type != ToastType.DEFAULT) {
                            Row {
                                Column(
                                    modifier = Modifier
                                        .size(24.dp, 24.dp),
                                    verticalArrangement = Arrangement.Center
//                                    .background(Blue_700)
                                ) {
                                    content()
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        Row(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                        ) {
                            Text(text = text, color = textColor, style = ToastSuccessTextStyle13Normal)
                        }
                    }
                }
            }

        }
        ToastPosition.CENTER -> {}
        is ToastPosition.Custom -> {}
    }
}


@Preview
@Composable
fun ToastLayoutTest() {
    ToastLayout(text = "Hello Shinhan Global Banking. This is Toast Sample.", position = ToastPosition.TOP)
}