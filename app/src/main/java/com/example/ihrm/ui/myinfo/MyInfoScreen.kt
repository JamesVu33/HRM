package com.example.ihrm.ui.myinfo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.R
import com.example.ihrm.ui.theme.DashboardFigmaInk
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.ui.theme.Neutral50
import com.example.ihrm.util.dropShadow
import com.example.ihrm.util.txtInterMedium14White90Percent

/** UI aligned with [Figma 871:37416](https://www.figma.com/design/Q9qmml4Qj4FD73VnYshBsO/HRM-Mobile-App-Ver?node-id=871-37416). */
private val MyInfoHeaderBrush = Brush.linearGradient(
    colorStops = arrayOf(
        0.03f to Color(0xFF0747A6),
        0.5f to Color(0xFF2684FF),
        0.97f to Color(0xFFB3D4FF),
    ),
    start = androidx.compose.ui.geometry.Offset(0f, Float.POSITIVE_INFINITY),
    end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, 0f),
)

private val PageBg = Color(0xFFF9FAFB)
private val CardBorder = Color(0xFFF3F4F6)
private val FieldBorder = Color(0xFFE5E7EB)
private val LabelGray = Color(0xFF6A7282)
private val MutedValue = Color(0xFF99A1AF)
private val GenderUnselectedBg = Color(0xFFF3F4F6)
private val GenderUnselectedText = Color(0xFF4A5565)
private val PrimaryBar = Color(0xFF155DFC)
private val ChangeInfoBlue = Color(0xFF0747A6)
private val CancelText = Color(0xFF364153)

private val LabelStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    color = LabelGray,
    letterSpacing = 0.6.sp,
)

private val FieldValueStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    color = DashboardFigmaInk,
    letterSpacing = (-0.31).sp,
)

private val SectionTitleStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 28.sp,
    color = DashboardFigmaInk,
    letterSpacing = (-0.88).sp,
)

private val SectionSubtitleStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    color = LabelGray,
    letterSpacing = (-0.15).sp,
)

private enum class GenderOption { Male, Female, Other }

@Composable
fun MyInfoScreen(
    onMenuClick: () -> Unit,
    onCancelClick: () -> Unit,
    onChangeInfoClick: () -> Unit = {},
    onKeyClick: () -> Unit = {},
    onChangePhotoClick: () -> Unit = {},
) {
    var gender by remember { mutableStateOf(GenderOption.Male) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MyInfoHeaderBrush)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        MyInfoHeader(
            onMenuClick = onMenuClick,
            onKeyClick = onKeyClick,
            onChangePhotoClick = onChangePhotoClick,
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MyInfoFormCard(
                    title = stringResource(R.string.my_info_section_basic_title),
                    subtitle = stringResource(R.string.my_info_section_basic_subtitle),
                ) {
                    MyInfoLabeledField(
                        label = stringResource(R.string.my_info_label_full_name),
                        icon = ImageVector.vectorResource(R.drawable.ic_person),
                        content = {
                            MyInfoValueBox(stringResource(R.string.my_info_demo_full_name))
                        },
                    )
                    MyInfoDivider()
                    MyInfoLabeledField(
                        label = stringResource(R.string.my_info_label_english_name),
                        icon = ImageVector.vectorResource(R.drawable.ic_person),
                        content = { MyInfoValueBox(stringResource(R.string.my_info_demo_english_name)) },
                    )
                    MyInfoDivider()
                    MyInfoLabeledField(
                        label = stringResource(R.string.my_info_label_email),
                        icon = ImageVector.vectorResource(R.drawable.ic_email),
                        content = { MyInfoValueBox(stringResource(R.string.my_info_demo_email)) },
                    )
                    MyInfoDivider()
                    MyInfoLabeledField(
                        label = stringResource(R.string.my_info_label_phone),
                        icon = ImageVector.vectorResource(R.drawable.ic_cellphone),
                        content = { MyInfoValueBox(stringResource(R.string.my_info_demo_phone)) },
                    )
                    MyInfoDivider()
                    MyInfoLabeledField(
                        label = stringResource(R.string.my_info_label_dob),
                        icon = ImageVector.vectorResource(R.drawable.ic_calendar),
                        content = { MyInfoValueBox(stringResource(R.string.my_info_demo_dob)) },
                    )
                    MyInfoDivider()
                    MyInfoLabeledField(
                        label = stringResource(R.string.my_info_label_gender),
                        icon = ImageVector.vectorResource(R.drawable.ic_person),
                        content = {
                            MyInfoGenderRow(
                                selected = gender,
                                onSelect = { gender = it },
                            )
                        },
                    )
                    MyInfoDivider()
                    MyInfoLabeledField(
                        label = stringResource(R.string.my_info_label_identity_id),
                        icon = ImageVector.vectorResource(R.drawable.ic_calendar),
                        content = { MyInfoValueBox(stringResource(R.string.my_info_demo_identity_id)) },
                    )
                    MyInfoDivider()
                    MyInfoLabeledField(
                        label = stringResource(R.string.my_info_label_identity_issue_day),
                        icon = ImageVector.vectorResource(R.drawable.ic_calendar),
                        content = { MyInfoValueBox(stringResource(R.string.my_info_demo_identity_issue)) },
                    )
                    MyInfoDivider()
                    MyInfoLabeledField(
                        label = stringResource(R.string.my_info_label_identity_issue_place),
                        icon = ImageVector.vectorResource(R.drawable.ic_location),
                        content = { MyInfoValueBox(stringResource(R.string.my_info_demo_identity_place)) },
                    )
                    MyInfoDivider()
                    MyInfoLabeledField(
                        label = stringResource(R.string.my_info_label_nationality),
                        icon = ImageVector.vectorResource(R.drawable.ic_location),
                        content = {
                            MyInfoDropdownRow(stringResource(R.string.my_info_demo_nationality))
                        },
                    )
                    MyInfoDivider()
                    MyInfoLabeledField(
                        label = stringResource(R.string.my_info_label_marital_status),
                        icon = ImageVector.vectorResource(R.drawable.ic_person),
                        content = {
                            MyInfoDropdownRow(stringResource(R.string.marital_single))
                        },
                    )
                    MyInfoDivider()
                    MyInfoLabeledField(
                        label = stringResource(R.string.my_info_label_address),
                        icon = ImageVector.vectorResource(R.drawable.ic_location),
                        content = {
                            MyInfoAddressBox(stringResource(R.string.my_info_demo_address))
                        },
                    )
                }

                MyInfoFormCard(
                    title = stringResource(R.string.my_info_section_employee_title),
                    subtitle = null,
                ) {
                    MyInfoReadOnlyEmployeeField(
                        label = stringResource(R.string.my_info_label_role),
                        value = stringResource(R.string.my_info_demo_role),
                    )
                    MyInfoDivider()
                    MyInfoReadOnlyEmployeeField(
                        label = stringResource(R.string.my_info_label_job_title),
                        value = stringResource(R.string.my_info_demo_job_title),
                    )
                    MyInfoDivider()
                    MyInfoReadOnlyEmployeeField(
                        label = stringResource(R.string.my_info_label_level),
                        value = stringResource(R.string.my_info_demo_level),
                    )
                    MyInfoDivider()
                    MyInfoReadOnlyEmployeeField(
                        label = stringResource(R.string.my_info_label_department),
                        value = stringResource(R.string.my_info_demo_department),
                    )
                    MyInfoDivider()
                    MyInfoReadOnlyEmployeeField(
                        label = stringResource(R.string.my_info_label_status),
                        value = stringResource(R.string.my_info_demo_status),
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .dropShadow(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.Black.copy(alpha = 0.1f),
                                blur = 6.dp,
                                offsetY = 4.dp,
                                spread = (-1).dp
                            )
                            .height(60.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White)
                            .border(1.dp, FieldBorder, RoundedCornerShape(20.dp))
                            .clickable(onClick = onCancelClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.my_info_cancel),
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = CancelText,
                            ),
                        )
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .dropShadow(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.Black.copy(alpha = 0.1f),
                                blur = 6.dp,
                                offsetY = 4.dp,
                                spread = (-1).dp
                            )
                            .height(60.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(ChangeInfoBlue)
                            .clickable(onClick = onChangeInfoClick),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_edit),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = stringResource(R.string.my_info_change_info),
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = Color.White,
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MyInfoHeader(
    onMenuClick: () -> Unit,
    onKeyClick: () -> Unit,
    onChangePhotoClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.my_info_menu_cd),
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = stringResource(R.string.my_info_screen_title),
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                    color = Color.White,
                    letterSpacing = (-0.89).sp,
                ),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.4f))
                    .clickable(onClick = onKeyClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_key),
                    contentDescription = stringResource(R.string.my_info_key_cd),
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(96.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f))
                        .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable(onClick = onChangePhotoClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_upload),
                        contentDescription = stringResource(R.string.my_info_change_photo),
                        tint = Color(0xFF155DFC),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.my_info_change_photo),
                style = txtInterMedium14White90Percent,
            )
        }
    }
}

@Composable
private fun MyInfoFormCard(
    title: String,
    subtitle: String?,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, CardBorder),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(PrimaryBar)
                )
                Text(text = title, style = SectionTitleStyle)
            }
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = SectionSubtitleStyle,
                    modifier = Modifier.padding(start = 24.dp, top = 4.dp, end = 24.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }
            Column(modifier = Modifier.padding(top = 8.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun MyInfoDivider() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(CardBorder)
    )
}

@Composable
private fun MyInfoLabeledField(
    label: String,
    icon: ImageVector,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryBar,
                modifier = Modifier.size(16.dp)
            )
            Text(text = label, style = LabelStyle)
        }
        content()
    }
}

@Composable
private fun MyInfoValueBox(value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, FieldBorder, RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = value, style = FieldValueStyle)
    }
}

@Composable
private fun MyInfoAddressBox(value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, FieldBorder, RoundedCornerShape(14.dp))
            .background(PageBg)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = value,
            style = FieldValueStyle.copy(
                color = MutedValue,
                lineHeight = 24.sp,
            ),
        )
    }
}

@Composable
private fun MyInfoDropdownRow(value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, FieldBorder, RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = value, style = FieldValueStyle)
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = DashboardFigmaInk,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(20.dp)
        )
    }
}

@Composable
private fun MyInfoGenderRow(
    selected: GenderOption,
    onSelect: (GenderOption) -> Unit,
) {
    val selectedBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF2B7FFF), Color(0xFF155DFC))
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        GenderOption.entries.forEach { option ->
            val isSelected = option == selected
            val label = when (option) {
                GenderOption.Male -> stringResource(R.string.my_info_gender_male)
                GenderOption.Female -> stringResource(R.string.my_info_gender_female)
                GenderOption.Other -> stringResource(R.string.my_info_gender_other)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .then(
                        if (isSelected) {
                            Modifier.background(selectedBrush, RoundedCornerShape(14.dp))
                        } else {
                            Modifier.background(GenderUnselectedBg, RoundedCornerShape(14.dp))
                        }
                    )
                    .clickable { onSelect(option) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    style = TextStyle(
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = if (isSelected) Color.White else GenderUnselectedText,
                        letterSpacing = (-0.15).sp,
                    ),
                )
            }
        }
    }
}

@Composable
private fun MyInfoReadOnlyEmployeeField(
    label: String,
    value: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_brief_case),
                contentDescription = null,
                tint = PrimaryBar,
                modifier = Modifier.size(16.dp)
            )
            Text(text = label, style = LabelStyle)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, FieldBorder, RoundedCornerShape(14.dp))
                .background(PageBg)
                .alpha(0.5f)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = value,
                style = FieldValueStyle.copy(color = MutedValue),
            )
        }
    }
}
