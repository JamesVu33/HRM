package com.example.ihrm.ui.common.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.ui.common.rememberThrottledClick
import com.example.ihrm.ui.theme.Primary500

@Composable
fun BaseHeader(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleContent: (@Composable () -> Unit)? = null,
    showNavigationIcon: Boolean = false,
    onNavigationClick: (() -> Unit)? = null,
    navigationIcon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = Color.White
        )
    },
    actions: (@Composable () -> Unit)? = null,
    containerColor: Color = Primary500,
    containerBrush: Brush? = null,
    contentColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = containerColor)
            .then(
                if (containerBrush != null) {
                    Modifier.background(brush = containerBrush)
                } else {
                    Modifier
                }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (showNavigationIcon && onNavigationClick != null) {
                    val onNavThrottled = rememberThrottledClick(onClick = onNavigationClick)
                    IconButton(
                        onClick = onNavThrottled,
                        modifier = Modifier.size(40.dp)
                    ) {
                        navigationIcon()
                    }
                }
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (titleContent != null) {
                    titleContent()
                } else if (!title.isNullOrBlank()) {
                    Text(
                        text = title,
                        color = contentColor,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                actions?.invoke()
            }
        }
    }
}