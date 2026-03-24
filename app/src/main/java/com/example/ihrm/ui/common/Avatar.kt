package com.example.ihrm.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ihrm.R
import com.example.ihrm.ui.theme.DashboardOnlineGreen
import com.example.ihrm.ui.theme.Primary500
import com.example.ihrm.ui.theme.PrimaryTint
import com.example.ihrm.util.dropShadow

@Composable
fun Avatar(
    imageUrl: String?,
    initials: String,
    modifier: Modifier = Modifier,
    placeholderImage: Painter = painterResource(R.drawable.avarta),
    errorImage: Painter = painterResource(R.drawable.avarta),
    size: Dp = 64.dp,
    isOnline: Boolean = false,
    showBorder: Boolean = true,
    isSecurity: Boolean = false,
) {
    Box(modifier = modifier.size(size)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .dropShadow(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.2f),
                    blur = 12.dp,
                    offsetY = 4.dp
                )
                .clip(CircleShape)
                .then(
                    if (showBorder) Modifier.border(1.dp, Color.White, CircleShape)
                    else Modifier
                )
                .background(
                    Brush.linearGradient(
                        listOf(PrimaryTint, Color(0xFFECFEFF))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            if (!imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit,
                    placeholder = placeholderImage,
                    error = errorImage
                )
            } else {
                Text(
                    text = initials,
                    fontSize = (size.value * 0.3).sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary500
                )
            }
        }

        // Online status
        if (isOnline) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(size * 0.25f)
                    .clip(CircleShape)
                    .background(DashboardOnlineGreen)
                    .border(1.dp, Color.White, CircleShape)
            )
        }
        // Security status
        if (isSecurity) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(size * 0.25f)
                    .dropShadow(
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.2f),
                        blur = 12.dp,
                        offsetY = 4.dp
                    )
                    .clip(CircleShape)
                    .then(
                        if (showBorder) Modifier.border(1.dp, Color.White, CircleShape)
                        else Modifier
                    )
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF2B7FFF), Color(0xFF155DFC))
                        )
                    ),
            ) {
                Image(
                    modifier = Modifier.size(size * 0.18f).align(Alignment.Center),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_security),
                    contentDescription = ""
                )
            }
        }
    }
}