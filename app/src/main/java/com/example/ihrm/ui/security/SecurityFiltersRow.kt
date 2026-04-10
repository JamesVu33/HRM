package com.example.ihrm.ui.security

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.R
import com.example.ihrm.util.singleClick

@Composable
fun SecurityFiltersRow(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    filterActive: Boolean,
    onFilterClick: () -> Unit,
    searchPlaceholder: String,
    filterContentDescription: String,
    modifier: Modifier = Modifier,
) {
    val textStyle = TextStyle(
        color = Color.Black,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF2F2F7))
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_search),
            contentDescription = null,
            tint = Color(0xFF6B7280),
            modifier = Modifier.size(16.dp)
        )
        BasicTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = textStyle,
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = searchPlaceholder,
                            style = textStyle,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                    innerTextField()
                }
            }
        )
        if (searchQuery.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF969BA4))
                    .clickable { onSearchQueryChange("") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
        VerticalDivider(
            modifier = Modifier.height(20.dp),
            thickness = 1.dp,
            color = Color(0xFFE5E7EB)
        )
        Box(modifier = Modifier.size(28.dp), contentAlignment = Alignment.Center) {
            IconButton(
                onClick = onFilterClick.singleClick(),
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_filter),
                    contentDescription = filterContentDescription,
                    tint = Color(0xFF6B7280),
                    modifier = Modifier.size(16.dp)
                )
            }
            if (filterActive) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 2.dp)
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2563EB))
                )
            }
        }
    }
}
