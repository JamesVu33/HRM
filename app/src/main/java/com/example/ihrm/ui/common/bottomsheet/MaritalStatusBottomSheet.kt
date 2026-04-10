package com.example.ihrm.ui.common.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ihrm.R
import com.example.ihrm.domain.model.MaritalStatus
import com.example.ihrm.ui.theme.DashboardFigmaInk
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.ui.localization.tr

@Composable
fun MaritalStatusBottomSheet(
    isVisible: Boolean,
    statuses: List<Pair<MaritalStatus, String>>,
    onDismiss: () -> Unit,
    onStatusSelected: (MaritalStatus) -> Unit,
) {
    if (!isVisible) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() },
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
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
                            .background(Color(0xFFD1D5DB)),
                    )
                }

                Text(
                    text = tr(R.string.my_info_marital_status_picker_title),
                    style = TextStyle(
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        lineHeight = 28.sp,
                        color = DashboardFigmaInk,
                    ),
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 14.dp),
                )

                HorizontalDivider(color = Color(0xFFF3F4F6))

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(items = statuses, key = { it.first.name }) { status ->
                        Text(
                            text = status.second,
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                color = DashboardFigmaInk,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onStatusSelected(status.first)
                                    onDismiss()
                                }
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                        )
                        HorizontalDivider(color = Color(0xFFF3F4F6))
                    }
                }
            }
        }
    }
}
