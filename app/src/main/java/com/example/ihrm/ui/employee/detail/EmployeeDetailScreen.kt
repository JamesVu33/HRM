package com.example.ihrm.ui.employee.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ihrm.R
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.ui.common.header.BaseHeader
import com.example.ihrm.ui.theme.IHRMTheme
import com.example.ihrm.ui.theme.Neutral500
import com.example.ihrm.ui.theme.Neutral700
import com.example.ihrm.ui.theme.Primary200
import com.example.ihrm.ui.theme.Primary400
import com.example.ihrm.ui.theme.Primary50
import com.example.ihrm.ui.theme.Primary500
import com.example.ihrm.ui.theme.SurfaceBorder
import com.example.ihrm.util.DashboardBrush
import com.example.ihrm.ui.localization.tr

private val FormInputBg = Color(0xFFF9FAFB)
private val Neutral200 = Color(0xFFe5e7eb)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDetailScreen(
    employeeId: String,
    onEditClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: EmployeeDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(employeeId) {
        viewModel.loadEmployee(employeeId)
    }

    val successMessage = tr(R.string.employee_info_update_success)
    LaunchedEffect(uiState.updateSuccess) {
        if (uiState.updateSuccess) {
            snackbarHostState.showSnackbar(
                message = successMessage,
                withDismissAction = true
            )
            viewModel.clearUpdateSuccess()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            BaseHeader(
                modifier = Modifier.statusBarsPadding(),
                title = tr(R.string.employee_detail_title),
                showNavigationIcon = true,
                onNavigationClick = onBackClick,
                containerColor = Color.Transparent
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = DashboardBrush.BaseBackground)
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Primary400)
                    }
                }

                uiState.error != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tr(
                                R.string.employee_detail_error_format,
                                uiState.error!!
                            )
                        )
                    }
                }

                uiState.employee == null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = tr(R.string.employee_detail_not_found))
                    }
                }

                else -> {
                    EmployeeDetailContent(
                        employee = uiState.employee!!,
                        onBackClick = onBackClick,
                        onSave = { updated ->
                            viewModel.updateEmployee(updated) {
                                viewModel.loadEmployee(employeeId)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmployeeDetailContent(
    employee: Employee,
    onBackClick: () -> Unit,
    onSave: (Employee) -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf(
        tr(R.string.employee_detail_tab_basic_info),
        tr(R.string.employee_detail_tab_employee_info),
        tr(R.string.employee_detail_tab_emergency_contact)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = employee.name.take(2).uppercase().filter { it.isLetter() }
                        .ifEmpty { "?" },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = employee.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = tr(
                        R.string.employee_detail_subtitle_format,
                        employee.position ?: "",
                        employee.id
                    ),
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .padding(start = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(tabs) { index, label ->
                val isSelected = selectedTabIndex == index
                TextButton(
                    onClick = { selectedTabIndex = index },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = if (isSelected) Color.White else Primary500,
                        contentColor = if (isSelected) Primary400 else Color.White
                    )
                ) {
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        // Content card (phần còn lại màn hình)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp, top = 24.dp, end = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                when (selectedTabIndex) {
                    0 -> BasicInfoSection(employee = employee, onSave = onSave)
                    1 -> EmployeeInfoSection(employee = employee, onSave = onSave)
                    else -> PlaceholderSection(tabName = tabs[selectedTabIndex])
                }
            }
        }
    }
}


@Composable
private fun BasicInfoSection(
    employee: Employee,
    onSave: (Employee) -> Unit
) {
    var isEditing by remember(employee.id) { mutableStateOf(false) }
    var fullName by remember(employee) { mutableStateOf(employee.name) }
    var englishName by remember(employee) { mutableStateOf(employee.englishName ?: "") }
    var gender by remember(employee) { mutableStateOf(employee.gender ?: "Male") }
    var email by remember(employee) { mutableStateOf(employee.email) }
    var phone by remember(employee) { mutableStateOf(employee.phone) }
    var personalId by remember(employee) { mutableStateOf(employee.personalId ?: "") }
    var idIssueDate by remember(employee) { mutableStateOf(employee.idIssueDate ?: "") }
    var address by remember(employee) { mutableStateOf(employee.address ?: "") }
    var genderExpanded by remember { mutableStateOf(false) }
    val genders = listOf("Male", "Female", "Other")

    if (!isEditing) {
        fullName = employee.name
        englishName = employee.englishName ?: ""
        gender = employee.gender ?: "Male"
        email = employee.email
        phone = employee.phone
        personalId = employee.personalId ?: ""
        idIssueDate = employee.idIssueDate ?: ""
        address = employee.address ?: ""
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = tr(R.string.employee_detail_personal_info),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Neutral700
        )
        TextButton(
            onClick = {
                if (isEditing) {
                    onSave(
                        employee.copy(
                            name = fullName,
                            englishName = englishName.ifBlank { null },
                            gender = gender,
                            email = email,
                            phone = phone,
                            personalId = personalId.ifBlank { null },
                            idIssueDate = idIssueDate.ifBlank { null },
                            address = address.ifBlank { null },
                            updatedAt = System.currentTimeMillis()
                        )
                    )
                    isEditing = false
                } else {
                    isEditing = true
                }
            },
            colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                containerColor = Primary400,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                imageVector = if (isEditing) Icons.Default.Person else Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isEditing) tr(R.string.employee_detail_save) else tr(
                    R.string.employee_detail_change_info
                ),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (isEditing) {
                BasicInfoEditFields(
                    fullName = fullName,
                    onFullNameChange = { fullName = it },
                    englishName = englishName,
                    onEnglishNameChange = { englishName = it },
                    gender = gender,
                    onGenderChange = { gender = it },
                    genderExpanded = genderExpanded,
                    onGenderExpandedChange = { genderExpanded = it },
                    genders = genders,
                    email = email,
                    onEmailChange = { email = it },
                    phone = phone,
                    onPhoneChange = { phone = it },
                    personalId = personalId,
                    onPersonalIdChange = { personalId = it },
                    idIssueDate = idIssueDate,
                    onIdIssueDateChange = { idIssueDate = it },
                    address = address,
                    onAddressChange = { address = it }
                )
            } else {
                DetailField(
                    label = tr(R.string.employee_detail_full_name),
                    value = employee.name,
                    icon = Icons.Default.Person
                )
                DetailField(
                    label = tr(R.string.employee_detail_english_name),
                    value = employee.englishName ?: "—",
                    icon = Icons.Default.Person
                )
                DetailField(
                    label = tr(R.string.employee_detail_gender),
                    value = employee.gender ?: "—",
                    icon = Icons.Default.Person
                )
                DetailField(
                    label = tr(R.string.employee_detail_email_address),
                    value = employee.email,
                    icon = Icons.Default.Email
                )
                DetailField(
                    label = tr(R.string.employee_detail_contact_number),
                    value = employee.phone,
                    icon = Icons.Default.Phone
                )
                DetailField(
                    label = tr(R.string.employee_detail_personal_id),
                    value = employee.personalId ?: "—",
                    icon = Icons.Default.Person
                )
                DetailField(
                    label = tr(R.string.employee_detail_id_issue_date),
                    value = employee.idIssueDate ?: "—",
                    icon = Icons.Default.Person
                )
                DetailField(
                    label = tr(R.string.employee_detail_permanent_address),
                    value = employee.address ?: "—",
                    icon = Icons.Default.LocationOn,
                    showDivider = false
                )
            }
        }
    }
}

@Composable
private fun BasicInfoEditFields(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    englishName: String,
    onEnglishNameChange: (String) -> Unit,
    gender: String,
    onGenderChange: (String) -> Unit,
    genderExpanded: Boolean,
    onGenderExpandedChange: (Boolean) -> Unit,
    genders: List<String>,
    email: String,
    onEmailChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    personalId: String,
    onPersonalIdChange: (String) -> Unit,
    idIssueDate: String,
    onIdIssueDateChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit
) {
    DetailEditField(
        label = tr(R.string.employee_detail_full_name),
        value = fullName,
        onValueChange = onFullNameChange,
        icon = Icons.Default.Person
    )
    DetailEditField(
        label = tr(R.string.employee_detail_english_name),
        value = englishName,
        onValueChange = onEnglishNameChange,
        icon = Icons.Default.Person
    )
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
            text = tr(R.string.employee_detail_gender).uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Neutral500
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = gender,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onGenderExpandedChange(true) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = FormInputBg,
                unfocusedContainerColor = FormInputBg,
                focusedBorderColor = Neutral200,
                unfocusedBorderColor = Neutral200
            ),
            shape = RoundedCornerShape(14.dp)
        )
        DropdownMenu(
            expanded = genderExpanded,
            onDismissRequest = { onGenderExpandedChange(false) }
        ) {
            genders.forEach { g ->
                DropdownMenuItem(
                    text = { Text(g) },
                    onClick = {
                        onGenderChange(g)
                        onGenderExpandedChange(false)
                    }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    DetailEditField(
        label = tr(R.string.employee_detail_email_address),
        value = email,
        onValueChange = onEmailChange,
        icon = Icons.Default.Email,
        keyboardType = KeyboardType.Email
    )
    DetailEditField(
        label = tr(R.string.employee_detail_contact_number),
        value = phone,
        onValueChange = onPhoneChange,
        icon = Icons.Default.Phone,
        keyboardType = KeyboardType.Phone
    )
    DetailEditField(
        label = tr(R.string.employee_detail_personal_id),
        value = personalId,
        onValueChange = onPersonalIdChange,
        icon = Icons.Default.Person
    )
    DetailEditField(
        label = tr(R.string.employee_detail_id_issue_date),
        value = idIssueDate,
        onValueChange = onIdIssueDateChange,
        icon = Icons.Default.Person
    )
    DetailEditField(
        label = tr(R.string.employee_detail_permanent_address),
        value = address,
        onValueChange = onAddressChange,
        icon = Icons.Default.LocationOn,
        minLines = 3
    )
}

@Composable
private fun DetailEditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1
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
private fun DetailField(
    label: String,
    value: String,
    icon: ImageVector,
    showDivider: Boolean = true
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
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Neutral700,
            modifier = Modifier.fillMaxWidth()
        )
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = SurfaceBorder,
                thickness = 1.dp
            )
        }
    }
}

private val SectionPaddingHorizontal = 20.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmployeeInfoDatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                utcTimeMillis <= System.currentTimeMillis()

            override fun isSelectableYear(year: Int): Boolean =
                year <= java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        }
    )
    Dialog(onDismissRequest = onDismiss) {
        EmployeeInfoDatePickerContent(
            datePickerState = datePickerState,
            onConfirm = {
                datePickerState.selectedDateMillis?.let { ms ->
                    val sdf =
                        java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                    onDateSelected(sdf.format(java.util.Date(ms)))
                }
            },
            onDismiss = onDismiss
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmployeeInfoDatePickerContent(
    datePickerState: androidx.compose.material3.DatePickerState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
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
                    Text(tr(android.R.string.cancel), color = Neutral500)
                }
                TextButton(onClick = onConfirm) {
                    Text(tr(android.R.string.ok), color = Primary400)
                }
            }
        }
    }
}

@Composable
private fun EmployeeInfoSection(
    employee: Employee,
    onSave: (Employee) -> Unit
) {
    val role1Str = tr(R.string.employee_info_role1)
    val role2Str = tr(R.string.employee_info_role2)
    val jobDeveloperStr = tr(R.string.employee_info_job_developer)
    val levelJ1Str = tr(R.string.employee_info_level_j1)
    val statusWorkingStr = tr(R.string.employee_info_status_working)
    val contractFullStr = tr(R.string.employee_info_contract_full)
    val jobTitleOptionsStr = listOf(
        tr(R.string.employee_info_job_accounting),
        tr(R.string.employee_info_job_ga),
        tr(R.string.employee_info_job_hr),
        tr(R.string.employee_info_job_contract),
        tr(R.string.employee_info_job_developer)
    )
    val levelOptionsStr = listOf(
        tr(R.string.employee_info_level_j1),
        tr(R.string.employee_info_level_j2),
        tr(R.string.employee_info_level_s1),
        tr(R.string.employee_info_level_s2),
        tr(R.string.employee_info_level_m1),
        tr(R.string.employee_info_level_m2),
        tr(R.string.employee_info_level_m3),
        tr(R.string.employee_info_level_d1),
        tr(R.string.employee_info_level_d2)
    )
    val contractOptionsStr = listOf(
        tr(R.string.employee_info_contract_full),
        tr(R.string.employee_info_contract_part),
        tr(R.string.employee_info_contract_contract),
        tr(R.string.employee_info_contract_internship)
    )
    val statusOtherStr = tr(R.string.employee_info_status_other)

    var role by remember(role1Str) { mutableStateOf(role1Str) }
    var jobTitle by remember(jobDeveloperStr) { mutableStateOf(jobDeveloperStr) }
    var level by remember(levelJ1Str) { mutableStateOf(levelJ1Str) }
    var department by remember { mutableStateOf("GDC") }
    var status by remember(statusWorkingStr) { mutableStateOf(statusWorkingStr) }
    var contractType by remember(contractFullStr) { mutableStateOf(contractFullStr) }
    var departmentAddress by remember(employee.address) { mutableStateOf(employee.address ?: "") }
    var dateOfBirth by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    val roleOptions = listOf(role1Str, role2Str)
    val jobTitleOptions = jobTitleOptionsStr
    val levelOptions = levelOptionsStr
    val departmentOptions = listOf("GDC", "HR", "IT", "Finance")
    val statusOptions = listOf(statusWorkingStr, statusOtherStr)
    val contractOptions = contractOptionsStr

    if (showDatePicker) {
        EmployeeInfoDatePickerDialog(
            onDateSelected = { dateStr -> dateOfBirth = dateStr; showDatePicker = false },
            onDismiss = { showDatePicker = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SectionPaddingHorizontal)
    ) {
        Text(
            text = tr(R.string.employee_info_employment_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Neutral700
        )
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                EmployeeInfoDropdown(
                    label = tr(R.string.employee_info_role),
                    value = role,
                    options = roleOptions,
                    onValueChange = { role = it },
                    icon = Icons.Default.Person
                )
                EmployeeInfoDropdown(
                    label = tr(R.string.employee_info_job_title),
                    value = jobTitle,
                    options = jobTitleOptions,
                    onValueChange = { jobTitle = it },
                    icon = Icons.Default.Person,
                    hint = tr(R.string.employee_info_job_title_hint)
                )
                EmployeeInfoDropdown(
                    label = tr(R.string.employee_info_level),
                    value = level,
                    options = levelOptions,
                    onValueChange = { level = it },
                    icon = Icons.Default.Person
                )
                EmployeeInfoDropdown(
                    label = tr(R.string.employee_info_department),
                    value = department,
                    options = departmentOptions,
                    onValueChange = { department = it },
                    icon = Icons.Default.LocationOn
                )
                EmployeeInfoDropdown(
                    label = tr(R.string.employee_info_status),
                    value = status,
                    options = statusOptions,
                    onValueChange = { status = it },
                    icon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Neutral500
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tr(R.string.employee_info_contract_type).uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Neutral500
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                // Contract type pills: row1 = Full-time, Part-time, Contract; row2 = Internship (per Figma)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            contractOptionsStr[0],
                            contractOptionsStr[1],
                            contractOptionsStr[2]
                        ).forEach { option ->
                            val selected = contractType == option
                            Button(
                                onClick = { contractType = option },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selected) Primary400 else SurfaceBorder,
                                    contentColor = if (selected) Color.White else Neutral500
                                ),
                                elevation = ButtonDefaults.buttonElevation(0.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = option,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        val internshipOption = contractOptionsStr[3]
                        val selected = contractType == internshipOption
                        Button(
                            onClick = { contractType = internshipOption },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selected) Primary400 else SurfaceBorder,
                                contentColor = if (selected) Color.White else Neutral500
                            ),
                            elevation = ButtonDefaults.buttonElevation(0.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = internshipOption,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Neutral500
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tr(R.string.employee_info_department_address).uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Neutral500
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = departmentAddress,
                    onValueChange = { departmentAddress = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            tr(R.string.employee_info_department_address_placeholder),
                            color = Neutral500
                        )
                    },
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
//
//                Spacer(modifier = Modifier.height(16.dp))
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Edit,
//                        contentDescription = null,
//                        modifier = Modifier.size(16.dp),
//                        tint = Neutral500
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        text = tr(R.string.employee_info_date_of_birth).uppercase(),
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.SemiBold,
//                        color = Neutral500
//                    )
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//                Box(modifier = Modifier.fillMaxWidth()) {
//                    OutlinedTextField(
//                        value = dateOfBirth,
//                        onValueChange = {},
//                        readOnly = true,
//                        modifier = Modifier.fillMaxWidth(),
//                        placeholder = { Text("DD/MM/YYYY", color = Neutral500) },
//                        colors = OutlinedTextFieldDefaults.colors(
//                            focusedContainerColor = FormInputBg,
//                            unfocusedContainerColor = FormInputBg,
//                            focusedBorderColor = Neutral200,
//                            unfocusedBorderColor = Neutral200
//                        ),
//                        shape = RoundedCornerShape(14.dp),
//                        trailingIcon = {
//                            Icon(
//                                imageVector = Icons.Default.KeyboardArrowDown,
//                                contentDescription = null,
//                                tint = Neutral500
//                            )
//                        }
//                    )
//                    // Overlay để bắt click mở lịch (TextField readOnly vẫn ăn click trước)
//                    Box(
//                        modifier = Modifier
//                            .matchParentSize()
//                            .clickable(
//                                indication = null,
//                                interactionSource = remember { MutableInteractionSource() }
//                            ) { showDatePicker = true }
//                    )
//                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        // Two buttons per Figma: Cancel + Update
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
//            Button(
//                onClick = { /* Cancel: dismiss or reset */ },
//                modifier = Modifier.weight(1f),
//                shape = RoundedCornerShape(14.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = SurfaceBorder, contentColor = Neutral700),
//                elevation = ButtonDefaults.buttonElevation(0.dp)
//            ) {
//                Text(
//                    text = tr(R.string.employee_info_cancel),
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.SemiBold
//                )
//            }
            Button(
                onClick = {
                    onSave(
                        employee.copy(
                            department = department,
                            position = jobTitle,
                            address = departmentAddress.ifBlank { employee.address },
                            updatedAt = System.currentTimeMillis()
                        )
                    )
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary500,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(
                    text = tr(R.string.employee_info_update),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmployeeInfoDropdown(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
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
                        },
                        modifier = Modifier.background(
                            if (isSelected) Primary50 else Color.Transparent
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = SurfaceBorder, thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun PlaceholderSection(tabName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = tr(R.string.employee_detail_personal_info),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Neutral700
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.icon_comming_soon),
                contentDescription = "",
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Emergency Contact",
                fontSize = 20.sp,
                color = Neutral500,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Coming soon",
                fontSize = 16.sp,
                color = Neutral500,
                fontWeight = FontWeight.Thin
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun Preview() {
    IHRMTheme {
        EmployeeDetailContent(
            employee = Employee(
                id = "123",
                name = "Alexander Wright",
                email = "alexander.wright@company.com",
                phone = "+1 (555) 123-4567",
                department = "S1",
                position = "Designer",
                hireDate = null,
                salary = null,
                address = "1234 Enterprise Way, San Francisco, CA 94102",
                englishName = "Alex",
                gender = "Male",
                personalId = "ID-2024-7891",
                idIssueDate = "15/01/2024"
            ),
            onBackClick = {},
            onSave = {}
        )
    }
}

