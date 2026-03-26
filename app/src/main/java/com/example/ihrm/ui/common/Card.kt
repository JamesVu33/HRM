package com.example.ihrm.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.ihrm.R
import com.example.ihrm.ui.stats.SummaryCardData
import com.example.ihrm.util.AnalyticsTrendLabelTextStyle13

@Composable
fun TopSummaryCard(
    modifier: Modifier = Modifier,
    data: SummaryCardData,
) {
    val border = if (data.borderColor.alpha < 0.01f) {
        null
    } else {
        BorderStroke(1.dp, data.borderColor)
    }
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = border
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = data.containerBrush ?: SolidColor(data.containerColor))
        ) {
            if (data.plainHeaderIcon) {
                TopSummaryCardPlainHeaderContent(data)
            } else {
                TopSummaryCardBadgeHeaderContent(data)
            }
        }
    }
}

@Composable
private fun TopSummaryCardPlainHeaderContent(data: SummaryCardData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = data.label,
                style = data.textStyleLabel,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = ImageVector.vectorResource(data.iconRes),
                contentDescription = null,
                tint = data.headerIconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = data.value, style = data.textStyleValue)
            val trend = data.trendText
            if (trend != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.icon_trend_arrow),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = data.trendTint
                    )
                    Text(
                        text = trend,
                        style = AnalyticsTrendLabelTextStyle13.copy(color = data.trendTint)
                    )
                }
            }
        }
        if (data.caption.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = data.caption,
                style = data.textStyleCaption,
                modifier = Modifier.alpha(0.8f)
            )
        }
    }
}

@Composable
private fun TopSummaryCardBadgeHeaderContent(data: SummaryCardData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(17.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = data.label, style = data.textStyleLabel)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = data.value, style = data.textStyleValue)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = data.caption, style = data.textStyleCaption)
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(data.iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(data.iconRes),
                contentDescription = null,
                tint = data.iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
