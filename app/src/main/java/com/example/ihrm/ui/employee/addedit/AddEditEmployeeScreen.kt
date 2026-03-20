package com.example.ihrm.ui.employee.addedit

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ihrm.R
import com.example.ihrm.ui.components.ButtonVariant
import com.example.ihrm.ui.components.CustomButton
import com.example.ihrm.ui.theme.FABGradientEnd
import com.example.ihrm.ui.theme.FABGradientStart
import com.example.ihrm.ui.theme.Neutral200
import com.example.ihrm.ui.theme.Neutral50
import com.example.ihrm.ui.theme.Neutral500
import com.example.ihrm.ui.theme.Neutral700
import com.example.ihrm.ui.theme.Primary200
import com.example.ihrm.ui.theme.Primary400
import com.example.ihrm.ui.theme.Primary50
import com.example.ihrm.ui.theme.Primary500
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val FormInputBg = Color(0xFFF9FAFB)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEmployeeScreen(
    employeeId: String?,
    onSaveSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: AddEditEmployeeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    LaunchedEffect(employeeId) {
        viewModel.loadEmployee(employeeId)
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSaveSuccess()
        }
    }

    var fullName by remember { mutableStateOf("") }
    var englishName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf<Date?>(null) }
    var gender by remember { mutableStateOf("") }
    var personalId by remember { mutableStateOf("") }
    var idIssueDate by remember { mutableStateOf<Date?>(null) }
    var idIssuePlace by remember { mutableStateOf("") }
    var nationality by remember { mutableStateOf("") }
    var maritalStatus by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var contractType by remember { mutableStateOf("") }
    var level by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }

    var showPhotoPicker by remember { mutableStateOf(false) }
    var showDobPicker by remember { mutableStateOf(false) }
    var showIdIssueDatePicker by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.employee) {
        uiState.employee?.let { emp ->
            fullName = emp.name
            englishName = emp.englishName ?: ""
            email = emp.email
            phone = emp.phone
            emp.hireDate?.let {
                dateOfBirth = try {
                    dateFormat.parse(it)
                } catch (_: Exception) {
                    null
                }
            }
            gender = emp.gender ?: ""
            personalId = emp.personalId ?: ""
            emp.idIssueDate?.let {
                idIssueDate = try {
                    dateFormat.parse(it)
                } catch (_: Exception) {
                    null
                }
            }
            address = emp.address ?: ""
            jobTitle = emp.position ?: ""
            department = emp.department ?: ""
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) avatarUri = uri
        showPhotoPicker = false
    }

    var avatarPhotoVersion by remember { mutableStateOf(0) }
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) avatarPhotoVersion++
        showPhotoPicker = false
    }

    fun launchTakePhoto() {
        val cameraFile = File(context.cacheDir, "employee_photo_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            cameraFile
        )
        avatarUri = uri
        takePictureLauncher.launch(uri)
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Primary500,
                        Primary400,
                        Primary200,
                        Primary50,
                        Primary50,
                        Primary50
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .statusBarsPadding()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(R.string.add_employee_title),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
                // Avatar + Upload Photo (inside gradient, white text)
                var avatarBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
                LaunchedEffect(avatarUri, avatarPhotoVersion) {
                    avatarUri?.let { uri ->
                        context.contentResolver.openInputStream(uri)?.use { stream ->
                            avatarBitmap = BitmapFactory.decodeStream(stream)
                        } ?: run { avatarBitmap = null }
                    } ?: run { avatarBitmap = null }
                }
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.3f))
                        .clickable { showPhotoPicker = true },
                    contentAlignment = Alignment.Center
                ) {
                    if (avatarBitmap != null) {
                        Image(
                            bitmap = avatarBitmap!!.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(40.dp)
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = Primary500,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.add_employee_upload_photo),
                    fontSize = 14.sp,
                    color = Color.White
                )
                Spacer(Modifier.height(20.dp))
            }
        }

        if (showPhotoPicker) {
            AlertDialog(
                onDismissRequest = { showPhotoPicker = false },
                title = { Text(stringResource(R.string.add_employee_upload_photo)) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(
                            onClick = {
                                launchTakePhoto()
                                showPhotoPicker = false
                            }
                        ) {
                            Text(stringResource(R.string.add_employee_take_photo))
                        }
                        TextButton(
                            onClick = {
                                pickImageLauncher.launch("image/*")
                                showPhotoPicker = false
                            }
                        ) {
                            Text(stringResource(R.string.add_employee_choose_from_library))
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showPhotoPicker = false }) {
                        Text(stringResource(R.string.add_employee_photo_cancel))
                    }
                }
            )
        }

        // White card with rounded top (like EmployeeDetailScreen)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            colors = CardDefaults.cardColors(containerColor = Neutral50)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                // Basic Information - section with vertical blue bar
                SectionTitleWithBar(stringResource(R.string.add_employee_basic_info))
                Text(
                    text = stringResource(R.string.add_employee_basic_info_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500,
                    modifier = Modifier.padding(top = 4.dp)
                )

                AddEditDetailEditField(
                    label = stringResource(R.string.add_employee_full_name),
                    value = fullName,
                    onValueChange = { fullName = it },
                    icon = Icons.Default.Person,
                    placeholder = stringResource(R.string.add_employee_full_name_hint)
                )
                AddEditDetailEditField(
                    label = stringResource(R.string.add_employee_english_name),
                    value = englishName,
                    onValueChange = { englishName = it },
                    icon = Icons.Default.Person,
                    placeholder = stringResource(R.string.add_employee_english_name_hint)
                )
                AddEditDetailEditField(
                    label = stringResource(R.string.add_employee_email),
                    value = email,
                    onValueChange = { email = it },
                    icon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email,
                    placeholder = stringResource(R.string.add_employee_email_hint)
                )
                AddEditDetailEditField(
                    label = stringResource(R.string.add_employee_phone),
                    value = phone,
                    onValueChange = { phone = it },
                    icon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone,
                    placeholder = stringResource(R.string.add_employee_phone_hint)
                )
                AddEditDateField(
                    label = stringResource(R.string.add_employee_date_of_birth),
                    date = dateOfBirth,
                    onPick = { showDobPicker = true },
                    dateFormat = dateFormat
                )
                if (showDobPicker) {
                    AddEmployeeDatePickerDialog(
                        initialMillis = dateOfBirth?.time,
                        onDateSelected = { dateOfBirth = it; showDobPicker = false },
                        onDismiss = { showDobPicker = false }
                    )
                }
                AddEditGenderDropdown(
                    label = stringResource(R.string.add_employee_gender),
                    value = gender,
                    onValueChange = { gender = it },
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = it },
                    options = listOf(
                        stringResource(R.string.add_employee_gender_male),
                        stringResource(R.string.add_employee_gender_female),
                        stringResource(R.string.add_employee_gender_other)
                    )
                )
                AddEditDetailEditField(
                    label = stringResource(R.string.add_employee_identity_id),
                    value = personalId,
                    onValueChange = { personalId = it },
                    icon = Icons.Default.Info,
                    placeholder = stringResource(R.string.add_employee_identity_id_hint)
                )
                AddEditDateField(
                    label = stringResource(R.string.add_employee_id_issue_day),
                    date = idIssueDate,
                    onPick = { showIdIssueDatePicker = true },
                    dateFormat = dateFormat
                )
                if (showIdIssueDatePicker) {
                    AddEmployeeDatePickerDialog(
                        initialMillis = idIssueDate?.time,
                        onDateSelected = { idIssueDate = it; showIdIssueDatePicker = false },
                        onDismiss = { showIdIssueDatePicker = false }
                    )
                }
                AddEditDetailEditField(
                    label = stringResource(R.string.add_employee_id_issue_place),
                    value = idIssuePlace,
                    onValueChange = { idIssuePlace = it },
                    icon = Icons.Default.LocationOn,
                    placeholder = stringResource(R.string.add_employee_id_issue_place_hint)
                )
                AddEditDropdownField(
                    label = stringResource(R.string.add_employee_nationality),
                    value = nationality,
                    onValueChange = { nationality = it },
                    options = listOf(
                        stringResource(R.string.nationality_vietnamese),
                        stringResource(R.string.nationality_american),
                        stringResource(R.string.nationality_british),
                        stringResource(R.string.nationality_chinese),
                        stringResource(R.string.nationality_japanese),
                        stringResource(R.string.nationality_korean)
                    ),
                    icon = Icons.Default.LocationOn
                )
                AddEditDropdownField(
                    label = stringResource(R.string.add_employee_marital_status),
                    value = maritalStatus,
                    onValueChange = { maritalStatus = it },
                    options = listOf(
                        stringResource(R.string.marital_single),
                        stringResource(R.string.marital_married),
                        stringResource(R.string.marital_divorced),
                        stringResource(R.string.marital_widowed)
                    ),
                    icon = Icons.Default.Person
                )
                AddEditDetailEditField(
                    label = stringResource(R.string.add_employee_address),
                    value = address,
                    onValueChange = { address = it },
                    icon = Icons.Default.LocationOn,
                    minLines = 3,
                    placeholder = stringResource(R.string.add_employee_address_hint)
                )

                Spacer(Modifier.height(24.dp))
                SectionTitleWithBar(stringResource(R.string.add_employee_employment_info))

                AddEditDropdownField(
                    label = stringResource(R.string.add_employee_job_title),
                    value = jobTitle,
                    onValueChange = { jobTitle = it },
                    options = listOf(
                        stringResource(R.string.job_title_account),
                        stringResource(R.string.job_title_ga),
                        stringResource(R.string.job_title_contract),
                        stringResource(R.string.job_title_developer)
                    ),
                    icon = Icons.Default.Person
                )
                AddEditDropdownField(
                    label = stringResource(R.string.add_employee_role),
                    value = role,
                    onValueChange = { role = it },
                    options = listOf(
                        stringResource(R.string.employee_info_role1),
                        stringResource(R.string.employee_info_role2)
                    ),
                    icon = Icons.Default.Person
                )
                AddEditDropdownField(
                    label = stringResource(R.string.add_employee_department),
                    value = department,
                    onValueChange = { department = it },
                    options = listOf(
                        stringResource(R.string.department_gdc),
                        stringResource(R.string.department_project_biz),
                        stringResource(R.string.department_banking),
                        stringResource(R.string.department_mobile),
                        stringResource(R.string.department_qa)
                    ),
                    icon = Icons.Default.LocationOn
                )
                AddEditDropdownField(
                    label = stringResource(R.string.add_employee_status),
                    value = status,
                    onValueChange = { status = it },
                    options = listOf(
                        stringResource(R.string.employee_info_status_working),
                        stringResource(R.string.status_on_leave),
                        stringResource(R.string.status_probation),
                        stringResource(R.string.status_notice_period)
                    ),
                    icon = Icons.Default.Person
                )
                AddEditDropdownField(
                    label = stringResource(R.string.add_employee_contract_type),
                    value = contractType,
                    onValueChange = { contractType = it },
                    options = listOf(
                        stringResource(R.string.employee_info_contract_full),
                        stringResource(R.string.employee_info_contract_part),
                        stringResource(R.string.employee_info_contract_contract),
                        stringResource(R.string.employee_info_contract_internship)
                    ),
                    icon = Icons.Default.Person
                )
                AddEditDropdownField(
                    label = stringResource(R.string.add_employee_level),
                    value = level,
                    onValueChange = { level = it },
                    options = listOf(
                        stringResource(R.string.employee_info_level_j1),
                        stringResource(R.string.employee_info_level_j2),
                        stringResource(R.string.employee_info_level_s1),
                        stringResource(R.string.employee_info_level_s2),
                        stringResource(R.string.employee_info_level_m1),
                        stringResource(R.string.employee_info_level_m2),
                        stringResource(R.string.employee_info_level_m3),
                        stringResource(R.string.employee_info_level_d1),
                        stringResource(R.string.employee_info_level_d2)
                    ),
                    icon = Icons.Default.Person
                )
            }
        }

        // Bottom buttons (outside card, like Figma)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .background(Neutral50)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomButton(
                text = stringResource(R.string.add_employee_cancel),
                onClick = onBack,
                variant = ButtonVariant.Outline,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            CustomButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.add_employee_create_member),
                onClick = {
                    viewModel.saveEmployee(
                        name = fullName,
                        email = email,
                        phone = phone,
                        department = department.ifBlank { null },
                        position = jobTitle.ifBlank { null },
                        hireDate = dateOfBirth?.let { dateFormat.format(it) },
                        salary = null,
                        address = address.ifBlank { null },
                        englishName = englishName.ifBlank { null },
                        gender = gender.ifBlank { null },
                        personalId = personalId.ifBlank { null },
                        idIssueDate = idIssueDate?.let { dateFormat.format(it) },
                        onSuccess = onSaveSuccess
                    )
                },
                variant = ButtonVariant.Primary,
                icon = Icons.Default.Add
            )
        }
    }
}

@Composable
private fun SectionTitleWithBar(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(20.dp)
                .background(Primary400, RoundedCornerShape(2.dp))
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Neutral700,
            fontSize = 20.sp
        )
    }
}

/** Edit field style matching EmployeeDetailScreen BasicInfoEditFields / DetailEditField */
@Composable
private fun AddEditDetailEditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1,
    placeholder: String = ""
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Neutral500
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label.uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Neutral500
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = if (placeholder.isNotEmpty()) { { Text(placeholder) } } else null,
            modifier = Modifier.fillMaxWidth(),
            minLines = minLines,
            singleLine = minLines <= 1,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = FormInputBg,
                unfocusedContainerColor = FormInputBg,
                focusedBorderColor = Neutral200,
                unfocusedBorderColor = Neutral200,
                focusedTextColor = Neutral700,
                unfocusedTextColor = Neutral700
            ),
            shape = RoundedCornerShape(14.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun AddEditDateField(
    label: String,
    date: Date?,
    onPick: () -> Unit,
    dateFormat: SimpleDateFormat
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Neutral500
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label.uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Neutral500
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = date?.let { dateFormat.format(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            placeholder = { Text(stringResource(R.string.add_employee_date_format)) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onPick() },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = FormInputBg,
                unfocusedContainerColor = FormInputBg,
                focusedBorderColor = Neutral200,
                unfocusedBorderColor = Neutral200
            ),
            shape = RoundedCornerShape(14.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun AddEditGenderDropdown(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    options: List<String>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Neutral500
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Neutral500
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandedChange(true) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = FormInputBg,
                unfocusedContainerColor = FormInputBg,
                focusedBorderColor = Neutral200,
                unfocusedBorderColor = Neutral200
            ),
            shape = RoundedCornerShape(14.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            options.forEach { g ->
                DropdownMenuItem(
                    text = { Text(g) },
                    onClick = {
                        onValueChange(g)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditDropdownField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    options: List<String>,
    icon: ImageVector,
    hint: String? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val displayValue = value.ifBlank { hint ?: "" }
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Neutral500
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label.uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Neutral500
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = displayValue,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = FormInputBg,
                    unfocusedContainerColor = FormInputBg,
                    focusedBorderColor = Neutral200,
                    unfocusedBorderColor = Neutral200,
                    disabledTextColor = Neutral700,
                    disabledBorderColor = Neutral200
                ),
                shape = RoundedCornerShape(14.dp),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White, RoundedCornerShape(14.dp))
            ) {
                options.forEach { option ->
                    val isSelected = value == option
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = option,
                                    color = if (isSelected) Primary400 else Neutral700,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Primary400,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEmployeeDatePickerDialog(
    initialMillis: Long?,
    onDateSelected: (Date) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis ?: System.currentTimeMillis(),
        yearRange = (Calendar.getInstance().get(Calendar.YEAR) - 100)..Calendar.getInstance()
            .get(Calendar.YEAR)
    )
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        selectedDayContainerColor = Primary400,
                        selectedDayContentColor = Color.White,
                        todayContentColor = Primary400
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(android.R.string.cancel), color = Neutral500)
                    }
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { ms ->
                            onDateSelected(Date(ms))
                        }
                    }) {
                        Text(stringResource(android.R.string.ok), color = Primary400)
                    }
                }
            }
        }
    }
}
