package com.example.ihrm.ui.myinfo

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ihrm.R
import com.example.ihrm.data.remote.myinfo.UpdateProfileRequest
import com.example.ihrm.domain.model.MaritalStatus
import com.example.ihrm.domain.model.MyInfo
import com.example.ihrm.domain.model.MyProfile
import com.example.ihrm.ui.common.Avatar
import com.example.ihrm.ui.common.BaseHRMCompose
import com.example.ihrm.ui.common.CountryPickerBottomSheet
import com.example.ihrm.ui.common.MaritalStatusBottomSheet
import com.example.ihrm.ui.components.ButtonSize
import com.example.ihrm.ui.components.ButtonVariant
import com.example.ihrm.ui.components.CustomButton
import com.example.ihrm.ui.theme.DashboardFigmaInk
import com.example.ihrm.ui.theme.GenderUnselectedBg
import com.example.ihrm.ui.theme.GenderUnselectedText
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.util.DashboardBrush.MyInfoHeaderBrush
import com.example.ihrm.util.dropShadow
import com.example.ihrm.util.toDisplayDate
import com.example.ihrm.util.txtInterMedium14White90Percent

private val PageBg = Color(0xFFF9FAFB)
private val CardBorder = Color(0xFFF3F4F6)
private val FieldBorder = Color(0xFFE5E7EB)
private val LabelGray = Color(0xFF6A7282)
private val MutedValue = Color(0xFF99A1AF)
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

@Composable
private fun textOrPlaceholder(value: String?): String {
    if (value.isNullOrBlank()) return stringResource(R.string.my_info_not_available)
    return value
}

private fun rolesDisplay(info: MyInfo?): String? {
    if (info == null) return null
    val joined = info.roles.map { it.name }.filter { it.isNotBlank() }.joinToString(", ")
    return joined.ifBlank { null }
}

@Composable
private fun maritalStatusLabel(status: MaritalStatus?): String {
    if (status == null) return stringResource(R.string.my_info_not_available)
    return when (status) {
        MaritalStatus.SINGLE -> stringResource(R.string.marital_single)
        MaritalStatus.MARRIED -> stringResource(R.string.marital_married)
        MaritalStatus.DIVORCED -> stringResource(R.string.marital_divorced)
        MaritalStatus.WIDOWED -> stringResource(R.string.marital_widowed)
        MaritalStatus.UNKNOWN -> stringResource(R.string.my_info_not_available)
    }
}

private fun initialsFromName(name: String?): String {
    if (name.isNullOrBlank()) return "?"
    val parts = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
    if (parts.isEmpty()) return "?"
    return when (parts.size) {
        1 -> parts[0].take(2).uppercase()
        else -> "${parts[0].first()}${parts[1].first()}".uppercase()
    }
}

private fun changedOrNull(newValue: String?, oldValue: String?): String? {
    return if (newValue?.trim() == oldValue?.trim()) null else newValue
}

private data class MyInfoFormSaveDelta(
    val request: UpdateProfileRequest,
    val hasChangesProfile: Boolean,
    val hasChangesInfoMe: Boolean,
) {
    val hasAnyChanges: Boolean get() = hasChangesProfile || hasChangesInfoMe
}

/**
 * So sánh form với [myInfo]/[profile] đã load; null khi chưa đủ dữ liệu để PATCH (không có profile).
 */
private fun computeMyInfoFormSaveDelta(
    myInfo: MyInfo?,
    profile: MyProfile?,
    genderSelected: GenderOption,
    selectedMaritalStatus: MaritalStatus?,
    fullNameText: String,
    englishNameText: String,
    emailText: String,
    phoneText: String,
    dobText: String,
    identityIdText: String,
    identityIssueDayText: String,
    identityIssuePlaceText: String,
    addressText: String,
    countryCodeText: String,
): MyInfoFormSaveDelta? {
    val info = myInfo ?: return null
    val p = profile ?: return null

    val oldDob = p.dateOfBirth.toDisplayDate()
    val oldIdentityIssueDate = p.identityIdIssueDate.toDisplayDate()
    val oldGender = p.gender.name
    val oldMaritalStatus = p.maritalStatus.takeIf { it != MaritalStatus.UNKNOWN }?.name

    val request = UpdateProfileRequest(
        gender = changedOrNull(uiGenderToRequestValue(genderSelected), oldGender),
        avatarUrl = null,
        dateOfBirth = changedOrNull(dobText, oldDob),
        nationality = changedOrNull(countryCodeText, p.nationality),
        identityId = changedOrNull(identityIdText, p.identityId),
        identityIdIssueDate = changedOrNull(identityIssueDayText, oldIdentityIssueDate),
        identityIdIssuePlace = changedOrNull(identityIssuePlaceText, p.identityIdIssuePlace),
        englishName = changedOrNull(englishNameText, p.englishName),
        address = changedOrNull(addressText, p.address),
        maritalStatus = changedOrNull(selectedMaritalStatus?.name, oldMaritalStatus),
        fullName = changedOrNull(fullNameText, info.fullName),
        email = changedOrNull(emailText, info.email),
        phoneNumber = changedOrNull(phoneText, info.phoneNumber),
    )

    val hasChangesProfile = listOf(
        request.gender,
        request.avatarUrl,
        request.dateOfBirth,
        request.nationality,
        request.identityId,
        request.identityIdIssueDate,
        request.identityIdIssuePlace,
        request.englishName,
        request.address,
        request.maritalStatus,
    ).any { it != null }

    val hasChangesInfoMe = listOf(
        request.fullName,
        request.email,
        request.phoneNumber,
    ).any { it != null }

    return MyInfoFormSaveDelta(request, hasChangesProfile, hasChangesInfoMe)
}

@Composable
fun MyInfoScreen(
    onMenuClick: () -> Unit,
    onCancelClick: () -> Unit,
    onChangeInfoClick: () -> Unit = {},
    onKeyClick: () -> Unit = {},
    onChangePhotoClick: () -> Unit = {},
    viewModel: MyInfoViewModel = hiltViewModel()
) {
    BaseHRMCompose(
        content = {
            MyInfoScreenContent(
                viewModel = viewModel,
                onMenuClick = onMenuClick,
                onCancelClick = onCancelClick,
                onChangeInfoClick = onChangeInfoClick,
                onKeyClick = onKeyClick,
                onChangePhotoClick = onChangePhotoClick,
            )
        },
        viewmodel = viewModel,
        onErrorAlertClose = onCancelClick
    )
}

@Composable
fun MyInfoScreenContent(
    onMenuClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onChangeInfoClick: () -> Unit = {},
    onKeyClick: () -> Unit = {},
    onChangePhotoClick: () -> Unit = {},
    viewModel: MyInfoViewModel
) {
    val myInfo by viewModel.myInfo.collectAsState()
    val countries by viewModel.countries.collectAsState()
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            viewModel.changeAvatar(uri)
        }
    }
    val profile = myInfo?.profile
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showCountryPicker by remember { mutableStateOf(false) }
    var showMaritalStatusPicker by remember { mutableStateOf(false) }

    var fullNameText by remember { mutableStateOf("") }
    var englishNameText by remember { mutableStateOf("") }
    var emailText by remember { mutableStateOf("") }
    var phoneText by remember { mutableStateOf("") }
    var dobText by remember { mutableStateOf("") }
    var identityIdText by remember { mutableStateOf("") }
    var identityIssueDayText by remember { mutableStateOf("") }
    var identityIssuePlaceText by remember { mutableStateOf("") }
    var addressText by remember { mutableStateOf("") }
    var countryCodeText by remember { mutableStateOf("") }
    var genderSelected by remember { mutableStateOf(GenderOption.Male) }
    var selectedMaritalStatus by remember { mutableStateOf<MaritalStatus?>(null) }
    var formBaseline by remember { mutableStateOf<MyInfoFormBaseline?>(null) }

    LaunchedEffect(myInfo) {
        val info = myInfo ?: return@LaunchedEffect
        val p = info.profile
        fullNameText = info.fullName
        englishNameText = p?.englishName.orEmpty()
        emailText = info.email
        phoneText = info.phoneNumber
        dobText = p?.dateOfBirth.toDisplayDate()
        identityIdText = p?.identityId.orEmpty()
        identityIssueDayText = p?.identityIdIssueDate.toDisplayDate()
        identityIssuePlaceText = p?.identityIdIssuePlace.orEmpty()
        addressText = p?.address.orEmpty()
        countryCodeText = p?.nationality.orEmpty()
        selectedMaritalStatus = p?.maritalStatus?.takeIf { it != MaritalStatus.UNKNOWN }
        genderSelected = domainGenderToUiOption(p?.gender)
        formBaseline = MyInfoFormBaseline(
            fullName = fullNameText,
            englishName = englishNameText,
            email = emailText,
            phone = phoneText,
            dob = dobText,
            identityId = identityIdText,
            identityIssueDay = identityIssueDayText,
            identityIssuePlace = identityIssuePlaceText,
            address = addressText,
            countryCode = countryCodeText,
            gender = genderSelected,
            marital = selectedMaritalStatus,
        )
    }

    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            onSave = { currentPassword, newPassword, confirmPassword ->
                viewModel.changePassword(currentPassword, newPassword, confirmPassword)
                showChangePasswordDialog = false
            },
            onDismiss = { showChangePasswordDialog = false }
        )
    }

    CountryPickerBottomSheet(
        isVisible = showCountryPicker,
        countries = countries.orEmpty(),
        onDismiss = { showCountryPicker = false },
        onCountrySelected = { selectedCountry ->
            countryCodeText = selectedCountry.code
        },
    )

    MaritalStatusBottomSheet(
        isVisible = showMaritalStatusPicker,
        statuses = listOf(
            MaritalStatus.SINGLE to stringResource(R.string.marital_single),
            MaritalStatus.MARRIED to stringResource(R.string.marital_married),
            MaritalStatus.DIVORCED to stringResource(R.string.marital_divorced),
            MaritalStatus.WIDOWED to stringResource(R.string.marital_widowed),
        ),
        onDismiss = { showMaritalStatusPicker = false },
        onStatusSelected = { selectedStatus ->
            selectedMaritalStatus = selectedStatus
        },
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MyInfoHeaderBrush)
            .navigationBarsPadding()
    ) {
        MyInfoHeader(
            onMenuClick = onMenuClick,
            onKeyClick = {
                showChangePasswordDialog = true
                onKeyClick()
            },
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            val fullName = fullNameText.takeIf { it.isNotBlank() } ?: myInfo?.fullName
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.size(96.dp)) {
                    Avatar(
                        imageUrl = profile?.avatarUrl,
                        initials = initialsFromName(fullName),
                        size = 96.dp,
                        showBorder = true,
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable(onClick = {
                                imagePickerLauncher.launch("image/*")
                                onChangePhotoClick()
                            }),
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
            ) {
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
                                MyInfoValueBox(
                                    value = fullNameText,
                                    onValueChange = { fullNameText = it },
                                )
                            },
                        )
                        MyInfoDivider()
                        MyInfoLabeledField(
                            label = stringResource(R.string.my_info_label_english_name),
                            icon = ImageVector.vectorResource(R.drawable.ic_person),
                            content = {
                                MyInfoValueBox(
                                    value = englishNameText,
                                    onValueChange = { englishNameText = it },
                                )
                            },
                        )
                        MyInfoDivider()
                        MyInfoLabeledField(
                            label = stringResource(R.string.my_info_label_email),
                            icon = ImageVector.vectorResource(R.drawable.ic_email),
                            content = {
                                MyInfoValueBox(
                                    value = emailText,
                                    onValueChange = { emailText = it },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                )
                            },
                        )
                        MyInfoDivider()
                        MyInfoLabeledField(
                            label = stringResource(R.string.my_info_label_phone),
                            icon = ImageVector.vectorResource(R.drawable.ic_cellphone),
                            content = {
                                MyInfoValueBox(
                                    value = phoneText,
                                    onValueChange = { phoneText = it },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                )
                            },
                        )
                        MyInfoDivider()
                        MyInfoLabeledField(
                            label = stringResource(R.string.my_info_label_dob),
                            icon = ImageVector.vectorResource(R.drawable.ic_calendar),
                            content = {
                                MyInfoValueBox(
                                    value = dobText,
                                    onValueChange = { dobText = it },
                                )
                            },
                        )
                        MyInfoDivider()
                        MyInfoLabeledField(
                            label = stringResource(R.string.my_info_label_gender),
                            icon = ImageVector.vectorResource(R.drawable.ic_person),
                            content = {
                                MyInfoGenderRow(
                                    selected = genderSelected,
                                    onSelect = { genderSelected = it },
                                )
                            },
                        )
                        MyInfoDivider()
                        MyInfoLabeledField(
                            label = stringResource(R.string.my_info_label_identity_id),
                            icon = ImageVector.vectorResource(R.drawable.ic_calendar),
                            content = {
                                MyInfoValueBox(
                                    value = identityIdText,
                                    onValueChange = { identityIdText = it },
                                )
                            },
                        )
                        MyInfoDivider()
                        MyInfoLabeledField(
                            label = stringResource(R.string.my_info_label_identity_issue_day),
                            icon = ImageVector.vectorResource(R.drawable.ic_calendar),
                            content = {
                                MyInfoValueBox(
                                    value = identityIssueDayText,
                                    onValueChange = { identityIssueDayText = it },
                                )
                            },
                        )
                        MyInfoDivider()
                        MyInfoLabeledField(
                            label = stringResource(R.string.my_info_label_identity_issue_place),
                            icon = ImageVector.vectorResource(R.drawable.ic_location),
                            content = {
                                MyInfoValueBox(
                                    value = identityIssuePlaceText,
                                    onValueChange = { identityIssuePlaceText = it },
                                )
                            },
                        )
                        MyInfoDivider()
                        MyInfoLabeledField(
                            label = stringResource(R.string.my_info_label_nationality),
                            icon = ImageVector.vectorResource(R.drawable.ic_location),
                            content = {
                                MyInfoDropdownRow(
                                    value = countryCodeText.ifBlank {
                                        textOrPlaceholder(profile?.nationality)
                                    },
                                    onClick = {
                                        if (!countries.isNullOrEmpty()) {
                                            showCountryPicker = true
                                        }
                                    }
                                )
                            },
                        )
                        MyInfoDivider()
                        MyInfoLabeledField(
                            label = stringResource(R.string.my_info_label_marital_status),
                            icon = ImageVector.vectorResource(R.drawable.ic_person),
                            content = {
                                MyInfoDropdownRow(
                                    value = if (selectedMaritalStatus != null) {
                                        maritalStatusLabel(selectedMaritalStatus)
                                    } else {
                                        maritalStatusLabel(profile?.maritalStatus)
                                    },
                                    onClick = {
                                        showMaritalStatusPicker = true
                                    }
                                )
                            },
                        )
                        MyInfoDivider()
                        MyInfoLabeledField(
                            label = stringResource(R.string.my_info_label_address),
                            icon = ImageVector.vectorResource(R.drawable.ic_location),
                            content = {
                                MyInfoAddressBox(
                                    value = addressText,
                                    onValueChange = { addressText = it },
                                )
                            },
                        )
                    }

                    MyInfoFormCard(
                        title = stringResource(R.string.my_info_section_employee_title),
                        subtitle = null,
                    ) {
                        MyInfoReadOnlyEmployeeField(
                            label = stringResource(R.string.my_info_label_role),
                            value = textOrPlaceholder(rolesDisplay(myInfo)),
                        )
                        MyInfoDivider()
                        MyInfoReadOnlyEmployeeField(
                            label = stringResource(R.string.my_info_label_job_title),
                            value = textOrPlaceholder(profile?.title?.name),
                        )
                        MyInfoDivider()
                        MyInfoReadOnlyEmployeeField(
                            label = stringResource(R.string.my_info_label_level),
                            value = textOrPlaceholder(profile?.level?.name),
                        )
                        MyInfoDivider()
                        MyInfoReadOnlyEmployeeField(
                            label = stringResource(R.string.my_info_label_department),
                            value = stringResource(R.string.my_info_not_available),
                        )
                        MyInfoDivider()
                        MyInfoReadOnlyEmployeeField(
                            label = stringResource(R.string.my_info_label_status),
                            value = textOrPlaceholder(myInfo?.status),
                        )
                    }

                    val changeInfoEnabled = remember(
                        formBaseline,
                        fullNameText,
                        englishNameText,
                        emailText,
                        phoneText,
                        dobText,
                        identityIdText,
                        identityIssueDayText,
                        identityIssuePlaceText,
                        addressText,
                        countryCodeText,
                        genderSelected,
                        selectedMaritalStatus,
                    ) {
                        formBaseline?.let { base ->
                            base.differsFrom(
                                fullNameText = fullNameText,
                                englishNameText = englishNameText,
                                emailText = emailText,
                                phoneText = phoneText,
                                dobText = dobText,
                                identityIdText = identityIdText,
                                identityIssueDayText = identityIssueDayText,
                                identityIssuePlaceText = identityIssuePlaceText,
                                addressText = addressText,
                                countryCodeText = countryCodeText,
                                genderSelected = genderSelected,
                                selectedMaritalStatus = selectedMaritalStatus,
                            )
                        } == true
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
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
                                .alpha(if (changeInfoEnabled) 1f else 0.45f)
                                .background(ChangeInfoBlue)
                                .clickable(enabled = changeInfoEnabled) {
                                    val delta = computeMyInfoFormSaveDelta(
                                        myInfo = myInfo,
                                        profile = profile,
                                        genderSelected = genderSelected,
                                        selectedMaritalStatus = selectedMaritalStatus,
                                        fullNameText = fullNameText,
                                        englishNameText = englishNameText,
                                        emailText = emailText,
                                        phoneText = phoneText,
                                        dobText = dobText,
                                        identityIdText = identityIdText,
                                        identityIssueDayText = identityIssueDayText,
                                        identityIssuePlaceText = identityIssuePlaceText,
                                        addressText = addressText,
                                        countryCodeText = countryCodeText,
                                    ) ?: run {
                                        onChangeInfoClick()
                                        return@clickable
                                    }
                                    if (delta.hasChangesProfile) {
                                        viewModel.updateProfile(delta.request)
                                    }
                                    if (delta.hasChangesInfoMe) {
                                        viewModel.updateInfoMe(delta.request)
                                    }
                                    onChangeInfoClick()
                                },
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
}

@Composable
private fun MyInfoHeader(
    onMenuClick: () -> Unit,
    onKeyClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
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
private fun MyInfoValueBox(
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        textStyle = FieldValueStyle,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedBorderColor = FieldBorder,
            unfocusedBorderColor = FieldBorder,
            disabledBorderColor = FieldBorder,
            focusedTextColor = DashboardFigmaInk,
            unfocusedTextColor = DashboardFigmaInk,
            cursorColor = PrimaryBar,
        ),
    )
}

@Composable
private fun MyInfoAddressBox(
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = false,
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp),
        textStyle = FieldValueStyle.copy(
            color = MutedValue,
            lineHeight = 24.sp,
        ),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedBorderColor = FieldBorder,
            unfocusedBorderColor = FieldBorder,
            disabledBorderColor = FieldBorder,
            focusedTextColor = DashboardFigmaInk,
            unfocusedTextColor = DashboardFigmaInk,
            cursorColor = PrimaryBar,
        ),
    )
}

@Composable
private fun MyInfoDropdownRow(
    value: String,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, FieldBorder, RoundedCornerShape(14.dp))
            .background(Color.White)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
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
    readOnly: Boolean = false,
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
                    .clickable(enabled = !readOnly) { onSelect(option) },
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

private val ChangePasswordPlaceholderColor = Color(0xFF101828).copy(alpha = 0.5f)

@Composable
private fun ChangePasswordDialog(
    onSave: (currentPassword: String, newPassword: String, confirmPassword: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var currentVisible by remember { mutableStateOf(false) }
    var newVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(125.dp)
                        .background(MyInfoHeaderBrush),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp, top = 32.dp, end = 16.dp, bottom = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top,
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.my_info_change_password_title),
                                style = TextStyle(
                                    fontFamily = InterFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    lineHeight = 36.sp,
                                    color = Color.White,
                                    letterSpacing = (-0.53).sp,
                                ),
                            )
                            Text(
                                text = stringResource(R.string.my_info_change_password_subtitle),
                                style = TextStyle(
                                    fontFamily = InterFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    lineHeight = 21.sp,
                                    color = Color.White.copy(alpha = 0.8f),
                                    letterSpacing = (-0.5).sp,
                                ),
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                                .clickable(onClick = onDismiss),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.my_info_change_password_close_cd),
                                tint = Color.White,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .padding(top = 32.dp, bottom = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    ChangePasswordField(
                        label = stringResource(R.string.my_info_change_password_current_label),
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        placeholder = stringResource(R.string.my_info_change_password_current_placeholder),
                        visible = currentVisible,
                        onToggleVisible = { currentVisible = !currentVisible },
                    )
                    ChangePasswordField(
                        label = stringResource(R.string.my_info_change_password_new_label),
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        placeholder = stringResource(R.string.my_info_change_password_new_placeholder),
                        visible = newVisible,
                        onToggleVisible = { newVisible = !newVisible },
                    )
                    ChangePasswordField(
                        label = stringResource(R.string.my_info_change_password_confirm_label),
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = stringResource(R.string.my_info_change_password_confirm_placeholder),
                        visible = confirmVisible,
                        onToggleVisible = { confirmVisible = !confirmVisible },
                    )
                    CustomButton(
                        modifier = Modifier
                            .dropShadow(
                                offsetX = 0.dp,
                                offsetY = 4.dp,
                                blur = 6.dp,
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0x00000000).copy(0.1f),
                                spread = (-1).dp
                            ),
                        size = ButtonSize.Large,
                        variant = ButtonVariant.Primary,
                        text = stringResource(R.string.employee_detail_save),
                        onClick = { onSave(currentPassword, newPassword, confirmPassword) },
                        enabled = currentPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty(),
                        fullWidth = true,
                    )
                }
            }
        }
    }
}

@Composable
private fun ChangePasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    visible: Boolean,
    onToggleVisible: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_permission),
                contentDescription = null,
                tint = PrimaryBar,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = label,
                style = LabelStyle,
            )
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    style = FieldValueStyle.copy(color = ChangePasswordPlaceholderColor),
                )
            },
            singleLine = true,
            visualTransformation = if (visible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = onToggleVisible) {
                    Icon(
                        imageVector = if (visible) {
                            ImageVector.vectorResource(R.drawable.icon_eye_off)
                        } else {
                            ImageVector.vectorResource(R.drawable.icon_remember)
                        },
                        contentDescription = stringResource(R.string.my_info_change_password_toggle_visibility_cd),
                        tint = DashboardFigmaInk,
                        modifier = Modifier.size(20.dp),
                    )
                }
            },
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = PageBg,
                unfocusedContainerColor = PageBg,
                disabledContainerColor = PageBg,
                focusedBorderColor = FieldBorder,
                unfocusedBorderColor = FieldBorder,
                cursorColor = PrimaryBar,
                focusedTextColor = DashboardFigmaInk,
                unfocusedTextColor = DashboardFigmaInk,
            ),
        )
    }
}
