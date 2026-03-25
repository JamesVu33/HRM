package com.example.ihrm.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.ui.theme.Error
import com.example.ihrm.ui.theme.Neutral200
import com.example.ihrm.ui.theme.Neutral600
import com.example.ihrm.ui.theme.Primary300
import com.example.ihrm.ui.theme.Primary400
import com.example.ihrm.ui.theme.Primary50
import com.example.ihrm.ui.theme.Primary500

/**
 * Button size variants
 */
enum class ButtonSize {
    Small,
    Medium,
    Large
}

/**
 * Button style variants
 */
enum class ButtonVariant {
    Primary,
    Secondary,
    Outline,
    Ghost,
    Danger
}

@Composable
private fun ButtonContent(
    text: String,
    fontSize: TextUnit,
    icon: ImageVector?,
    variant: ButtonVariant,
    enabled: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = getIconColor(variant, enabled)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Custom button component with multiple variants, sizes, and states
 *
 * @param text The button text
 * @param onClick Callback when button is clicked
 * @param modifier Modifier for the button
 * @param size Button size (Small, Medium, Large)
 * @param variant Button style variant (Primary, Secondary, Outline, Ghost, Danger)
 * @param enabled Whether the button is enabled
 * @param fullWidth Whether the button should take full width
 * @param icon Optional leading icon
 * @param isPill Whether the button should have pill shape (more rounded)
 */
@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: ButtonSize = ButtonSize.Medium,
    variant: ButtonVariant = ButtonVariant.Primary,
    enabled: Boolean = true,
    fullWidth: Boolean = false,
    icon: ImageVector? = null,
    isPill: Boolean = false
) {
    val height: Dp
    val horizontalPadding: Dp
    val verticalPadding: Dp
    val fontSize: TextUnit
    when (size) {
        ButtonSize.Small -> {
            height = 32.dp
            horizontalPadding = 12.dp
            verticalPadding = 6.dp
            fontSize = 14.sp
        }

        ButtonSize.Medium -> {
            height = 40.dp
            horizontalPadding = 16.dp
            verticalPadding = 8.dp
            fontSize = 16.sp
        }

        ButtonSize.Large -> {
            height = 48.dp
            horizontalPadding = 24.dp
            verticalPadding = 12.dp
            fontSize = 18.sp
        }
    }

    val cornerRadius = if (isPill) 24.dp else 8.dp

    // Shadow elevation based on variant and enabled state
    val elevation = when {
        !enabled -> 0.dp
        variant == ButtonVariant.Outline -> 0.dp
        variant == ButtonVariant.Ghost -> 0.dp
        else -> 2.dp
    }

    val buttonModifier = if (fullWidth) {
        modifier.fillMaxWidth()
    } else {
        modifier
    }
        .height(height)
        .shadow(
            elevation = elevation,
            shape = RoundedCornerShape(cornerRadius),
            ambientColor = Color(18, 20, 24).copy(alpha = 0.05f),
            spotColor = Color(18, 20, 24).copy(alpha = 0.2f)
        )

    when (variant) {
        ButtonVariant.Outline -> {
            OutlinedButton(
                onClick = onClick,
                enabled = enabled,
                modifier = buttonModifier,
                colors = getOutlinedButtonColors(enabled),
                shape = RoundedCornerShape(cornerRadius),
                contentPadding = PaddingValues(
                    horizontal = horizontalPadding,
                    vertical = verticalPadding
                ),
                border = if (enabled) {
                    androidx.compose.foundation.BorderStroke(1.dp, Neutral200)
                } else {
                    androidx.compose.foundation.BorderStroke(1.dp, Primary50)
                }
            ) {
                ButtonContent(text, fontSize, icon, variant, enabled)
            }
        }

        ButtonVariant.Ghost -> {
            TextButton(
                onClick = onClick,
                enabled = enabled,
                modifier = buttonModifier,
                colors = getTextButtonColors(enabled),
                shape = RoundedCornerShape(cornerRadius),
                contentPadding = PaddingValues(
                    horizontal = horizontalPadding,
                    vertical = verticalPadding
                )
            ) {
                ButtonContent(text, fontSize, icon, variant, enabled)
            }
        }

        else -> {
            val buttonColors = getButtonColors(variant, enabled)
            Button(
                onClick = onClick,
                enabled = enabled,
                modifier = buttonModifier,
                colors = buttonColors,
                shape = RoundedCornerShape(cornerRadius),
                contentPadding = PaddingValues(
                    horizontal = horizontalPadding,
                    vertical = verticalPadding
                )
            ) {
                ButtonContent(text, fontSize, icon, variant, enabled)
            }
        }
    }
}

/**
 * Get button colors based on variant and enabled state
 */
@Composable
private fun getButtonColors(variant: ButtonVariant, enabled: Boolean): ButtonColors {
    return when {
        !enabled -> ButtonDefaults.buttonColors(
            containerColor = Primary50,
            contentColor = Primary300,
            disabledContainerColor = Primary50,
            disabledContentColor = Color.White
        )

        variant == ButtonVariant.Primary -> ButtonDefaults.buttonColors(
            containerColor = Primary500,
            contentColor = Color.White
        )

        variant == ButtonVariant.Secondary -> ButtonDefaults.buttonColors(
            containerColor = Primary400,
            contentColor = Color.White
        )

        variant == ButtonVariant.Danger -> ButtonDefaults.buttonColors(
            containerColor = Error,
            contentColor = Color.White
        )

        else -> ButtonDefaults.buttonColors()
    }
}

/**
 * Get outlined button colors
 */
@Composable
private fun getOutlinedButtonColors(enabled: Boolean): ButtonColors {
    return ButtonDefaults.outlinedButtonColors(
        containerColor = Color.White,
        contentColor = if (enabled) Neutral600 else Primary300,
        disabledContainerColor = Color.White,
        disabledContentColor = Primary300
    )
}

/**
 * Get text button colors for ghost variant
 */
@Composable
private fun getTextButtonColors(enabled: Boolean): ButtonColors {
    return ButtonDefaults.textButtonColors(
        contentColor = if (enabled) Primary400 else Primary300
    )
}

/**
 * Get icon color based on variant and enabled state
 */
@Composable
private fun getIconColor(variant: ButtonVariant, enabled: Boolean): Color {
    return when {
        !enabled -> Primary300
        variant == ButtonVariant.Primary -> Color.White
        variant == ButtonVariant.Secondary -> Color.White
        variant == ButtonVariant.Outline -> Neutral600
        variant == ButtonVariant.Ghost -> Primary400
        variant == ButtonVariant.Danger -> Color.White
        else -> Color.White
    }
}

/**
 * Pill button component - specialized button with pill shape
 *
 * @param text The button text
 * @param onClick Callback when button is clicked
 * @param modifier Modifier for the button
 * @param selected Whether the button is in selected state
 * @param enabled Whether the button is enabled
 */
@Composable
fun PillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true
) {
    val backgroundColor = if (selected) {
        Primary500
    } else {
        Color.White
    }

    val contentColor = if (selected) {
        Color.White
    } else {
        Primary400
    }

    val pillElevation = if (enabled && selected) 2.dp else 0.dp

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.shadow(
            elevation = 2.dp,
            shape = RoundedCornerShape(24.dp),
            spotColor = Color.Black.copy(alpha = 0.2f),
            ambientColor = Color.White
        ),
//        modifier = modifier.dropShadow(
//            offsetY = 2.dp,
//            shape = RoundedCornerShape(24.dp),
//            blur = 4.dp,
//            spread = (-2).dp,
//        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) backgroundColor else Primary50,
            contentColor = if (enabled) contentColor else Primary300,
            disabledContainerColor = Primary50,
            disabledContentColor = Primary300
        ),
        shape = RoundedCornerShape(24.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonSizesPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomButton(
            text = "Small Button",
            onClick = {},
            size = ButtonSize.Small
        )
        CustomButton(
            text = "Medium Button",
            onClick = {},
            size = ButtonSize.Medium
        )
        CustomButton(
            text = "Large Button",
            onClick = {},
            size = ButtonSize.Large
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonVariantsPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomButton(
            text = "Primary",
            onClick = {},
            variant = ButtonVariant.Primary
        )
        CustomButton(
            text = "Secondary",
            onClick = {},
            variant = ButtonVariant.Secondary
        )
        CustomButton(
            text = "Outline",
            onClick = {},
            variant = ButtonVariant.Outline
        )
        CustomButton(
            text = "Ghost",
            onClick = {},
            variant = ButtonVariant.Ghost
        )
        CustomButton(
            text = "Danger",
            onClick = {},
            variant = ButtonVariant.Danger
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonsWithIconsPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomButton(
            text = "Change Info",
            onClick = {},
            variant = ButtonVariant.Secondary,
            icon = Icons.Default.Edit
        )
        CustomButton(
            text = "Add User",
            onClick = {},
            variant = ButtonVariant.Primary,
            icon = Icons.Default.Add
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PillButtonsPreview() {
    FlowRow(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PillButton(
            text = "Basic Info",
            onClick = {},
            selected = false
        )
        PillButton(
            text = "Employee Info",
            onClick = {},
            selected = true
        )
        PillButton(
            text = "Education",
            onClick = {},
            selected = true
        )
        PillButton(
            text = "Career History",
            onClick = {},
            selected = true
        )
    }
}

@Composable
fun FlatButton(
    text: String,
    textColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(backgroundColor)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FullWidthButtonsPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomButton(
            text = "Large",
            onClick = {},
            size = ButtonSize.Large,
            fullWidth = true,
            enabled = true
        )
        CustomButton(
            text = "Large",
            onClick = {},
            size = ButtonSize.Large,
            fullWidth = true,
            enabled = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonStatesPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomButton(
            text = "Normal",
            onClick = {},
            variant = ButtonVariant.Primary,
            enabled = true
        )
        CustomButton(
            text = "Disabled",
            onClick = {},
            variant = ButtonVariant.Primary,
            enabled = false
        )
    }
}
