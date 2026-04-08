package com.example.ihrm.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ihrm.R
import com.example.ihrm.ui.theme.DashboardTabActiveBlue
import com.example.ihrm.ui.theme.DrawerItemSelected
import com.example.ihrm.ui.theme.Neutral200
import com.example.ihrm.ui.theme.Neutral500
import com.example.ihrm.ui.theme.Neutral700
import com.example.ihrm.ui.theme.Neutral50
import com.example.ihrm.util.singleClick
import com.example.ihrm.ui.localization.tr

private val LanguageRadioBorderUnselected = Color(0xFFd1d5db)

private data class LanguageRow(
    val code: String,
    val titleRes: Int,
    val subtitleRes: Int
)

private val languageRows = listOf(
    LanguageRow("en", R.string.language_english_name, R.string.language_english_native),
    LanguageRow("vi", R.string.language_vietnamese_name, R.string.language_vietnamese_native),
    LanguageRow("ko", R.string.language_korean_name, R.string.language_korean_native)
)

/**
 * Bottom-sheet style dialog for changing app language (Figma node 871:32097).
 * Wire [onSaveLanguage] to API when ready; currently safe to no-op or only dismiss from caller.
 */
@Composable
fun ChangeLanguageDialog(
    initialSelectedCode: String = "en",
    onDismiss: () -> Unit,
    onSaveLanguage: (selectedLanguageCode: String) -> Unit,
) {
    var selectedCode by remember(initialSelectedCode) { mutableStateOf(initialSelectedCode) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(32.dp),
                    spotColor = Color.Black.copy(alpha = 0.3f),
                    ambientColor = Color.Black.copy(alpha = 0.2f)
                )
                .clip(RoundedCornerShape(32.dp))
                .background(Color.White)
        ) {
            ChangeLanguageHeader(onDismiss = onDismiss)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 32.dp)
            ) {
                ChangeLanguageSectionLabel()
                Spacer(modifier = Modifier.height(12.dp))
                languageRows.forEachIndexed { index, row ->
                    if (index > 0) Spacer(modifier = Modifier.height(12.dp))
                    LanguageOptionRow(
                        title = tr(row.titleRes),
                        subtitle = tr(row.subtitleRes),
                        selected = selectedCode == row.code,
                        onClick = { selectedCode = row.code }
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                SaveLanguageButton(
                    onClick = { onSaveLanguage(selectedCode) }
                )
            }
        }
    }
}

@Composable
private fun ChangeLanguageHeader(onDismiss: () -> Unit) {
    val closeCd = tr(R.string.change_language_cd_close)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF0747A6),
                        0.35f to Color(0xFF155DFC),
                        0.65f to Color(0xFF2684FF),
                        1f to Color(0xFF7CB3FF)
                    )
                )
            )
            .padding(start = 32.dp, end = 20.dp, top = 32.dp, bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(end = 48.dp)
        ) {
            Text(
                text = tr(R.string.change_language_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = (-0.53).sp,
                lineHeight = 36.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tr(R.string.change_language_subtitle),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.8f),
                letterSpacing = (-0.5).sp,
                lineHeight = 21.sp
            )
        }
        IconButton(
            onClick = onDismiss.singleClick(),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f)),
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = closeCd,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ChangeLanguageSectionLabel() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = Icons.Outlined.Language,
            contentDescription = null,
            tint = DrawerItemSelected,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = tr(R.string.change_language_section_label),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Neutral500,
            letterSpacing = 0.6.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun LanguageOptionRow(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val primary = if (selected) DrawerItemSelected else Neutral700
    val secondary = if (selected) DrawerItemSelected else Neutral500
    val rowCd = tr(R.string.change_language_option_cd, title)
    val bgBrush = if (selected) {
        Brush.linearGradient(
            colors = listOf(
                DrawerItemSelected.copy(alpha = 0.10f),
                DrawerItemSelected.copy(alpha = 0.05f)
            )
        )
    } else {
        Brush.linearGradient(colors = listOf(Neutral50, Neutral50))
    }
    val borderColor = if (selected) DrawerItemSelected else Neutral200

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .background(bgBrush)
            .clickable(onClick = onClick.singleClick())
            .semantics {
                role = Role.RadioButton
                contentDescription = rowCd
            }
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = primary,
                letterSpacing = (-0.5).sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = secondary,
                letterSpacing = (-0.3).sp
            )
        }
        LanguageRadioIndicator(selected = selected)
    }
}

@Composable
private fun LanguageRadioIndicator(selected: Boolean) {
    if (selected) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(DrawerItemSelected)
                .border(2.dp, DrawerItemSelected, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
        }
    } else {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .border(2.dp, LanguageRadioBorderUnselected, CircleShape)
        )
    }
}

@Composable
private fun SaveLanguageButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = DrawerItemSelected.copy(alpha = 0.35f),
                ambientColor = DrawerItemSelected.copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(DrawerItemSelected, DashboardTabActiveBlue)
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick.singleClick()),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tr(R.string.change_language_save),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            letterSpacing = (-0.5).sp,
            textAlign = TextAlign.Center
        )
    }
}
