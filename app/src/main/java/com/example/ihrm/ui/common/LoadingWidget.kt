package com.example.ihrm.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ihrm.ui.theme.Neutral200
import com.example.ihrm.ui.theme.Primary400

@Composable
fun LoadingWidget(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(Neutral200),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            color = Primary400,
            trackColor = Neutral200
        )
    }

}