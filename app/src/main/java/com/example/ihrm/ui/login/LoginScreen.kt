package com.example.ihrm.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ihrm.R
import com.example.ihrm.ui.components.ButtonSize
import com.example.ihrm.ui.components.ButtonVariant
import com.example.ihrm.ui.components.CustomButton
import com.example.ihrm.ui.theme.Error
import com.example.ihrm.ui.theme.Neutral200
import com.example.ihrm.ui.theme.Neutral400
import com.example.ihrm.ui.theme.Neutral500
import com.example.ihrm.ui.theme.Neutral600
import com.example.ihrm.ui.theme.Neutral700
import com.example.ihrm.ui.theme.Primary300
import com.example.ihrm.ui.theme.Primary400
import com.example.ihrm.ui.theme.Primary50
import com.example.ihrm.ui.theme.Primary500

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Primary50, Primary300, Primary50)
                    )
                )
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(72.dp))

                    LogoHeader()

                    Spacer(modifier = Modifier.height(24.dp))

                    LoginContent(uiState, viewModel, onLoginSuccess)

                    Spacer(modifier = Modifier.height(20.dp))

                    // bottom brand label
                    Text(
                        text = stringResource(R.string.login_test_footer),
                        color = Primary400,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(horizontal = 32.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.login_test_title),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Neutral700,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.login_test_subtitle),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Neutral500,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = buildAnnotatedString {
                append(stringResource(R.string.login_test_employee_id_label))

                withStyle(
                    style = SpanStyle(
                        color = Color.Red
                    )
                ) {
                    append(" *")
                }
            },
            color = Neutral600,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        IdInput(uiState, viewModel)

        val employeeIdError = uiState.employeeIdError
        if (employeeIdError != null) {
            Text(
                text = stringResource(employeeIdError.toEmployeeIdMessageResId()),
                color = Error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            )
        }

        if (uiState.loginError != null) {
            Text(
                text = uiState.loginError,
                color = Error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = buildAnnotatedString {
                append(stringResource(R.string.login_test_password_label))

                withStyle(
                    style = SpanStyle(
                        color = Color.Red
                    )
                ) {
                    append(" *")
                }
            },
            color = Neutral600,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        PasswordInput(uiState, viewModel)

        val passwordError = uiState.passwordError
        if (passwordError != null) {
            Text(
                text = stringResource(passwordError.toPasswordMessageResId()),
                color = Error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Primary400
                )
            }
        } else {
            CustomButton(
                text = stringResource(R.string.login_test_login_button),
                onClick = { viewModel.login(onLoginSuccess) },
                modifier = Modifier.fillMaxWidth(),
                size = ButtonSize.Large,
                variant = ButtonVariant.Primary,
                enabled = uiState.employeeId.isNotEmpty() && uiState.password.isNotEmpty()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.login_test_forgot_password),
                color = Primary400,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun IdInput(
    uiState: LoginUiState,
    viewModel: LoginViewModel
) {
    OutlinedTextField(
        value = uiState.employeeId,
        onValueChange = viewModel::updateEmployeeId,
        placeholder = {
            Text(
                stringResource(R.string.login_test_employee_id_placeholder),
                color = Neutral400
            )
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = !uiState.isLoading,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.icon_email),
                contentDescription = null,
                tint = if (uiState.employeeIdError != null) Error else Neutral400
            )
        },
        isError = uiState.employeeIdError != null,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (uiState.employeeIdError != null) Error else Neutral200,
            unfocusedBorderColor = if (uiState.employeeIdError != null) Error else Neutral200,
            errorBorderColor = Error
        ),
        shape = RoundedCornerShape(14.dp)
    )
}

@Composable
private fun PasswordInput(
    uiState: LoginUiState,
    viewModel: LoginViewModel
) {
    OutlinedTextField(
        value = uiState.password,
        onValueChange = viewModel::updatePassword,
        placeholder = {
            Text(
                stringResource(R.string.login_test_password_placeholder),
                color = Neutral400
            )
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = !uiState.isLoading,
        singleLine = true,
        visualTransformation = if (uiState.isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.icon_password),
                contentDescription = null,
                tint = if (uiState.passwordError != null) Error else Neutral400
            )
        },
        trailingIcon = {
            IconButton(onClick = viewModel::togglePasswordVisibility) {
                Icon(
                    imageVector = if (uiState.isPasswordVisible) ImageVector.vectorResource(
                        id = R.drawable.icon_eye_off
                    ) else ImageVector.vectorResource(id = R.drawable.icon_remember),
                    contentDescription = null,
                    tint = Neutral400
                )
            }
        },
        isError = uiState.passwordError != null,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (uiState.passwordError != null) Error else Neutral200,
            unfocusedBorderColor = if (uiState.passwordError != null) Error else Neutral200,
            errorBorderColor = Error
        ),
        shape = RoundedCornerShape(14.dp)
    )
}

@Composable
private fun LogoHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.2f),
                    RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.shinhan_logo),
                modifier = Modifier.size(32.dp),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = stringResource(R.string.login_test_brand),
            color = Primary500,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
    }
}

private fun LoginFieldError.toEmployeeIdMessageResId(): Int {
    return when (this) {
        LoginFieldError.Required -> R.string.login_test_error_employee_id_required
        LoginFieldError.TooShort -> R.string.login_test_error_employee_id_too_short
        LoginFieldError.InvalidLength -> R.string.login_test_error_employee_id_invalid_length
        LoginFieldError.InvalidRules -> R.string.login_test_error_password_rules
    }
}

private fun LoginFieldError.toPasswordMessageResId(): Int {
    return when (this) {
        LoginFieldError.Required -> R.string.login_test_error_password_required
        LoginFieldError.TooShort -> R.string.login_test_error_employee_id_too_short
        LoginFieldError.InvalidLength -> R.string.login_test_error_employee_id_invalid_length
        LoginFieldError.InvalidRules -> R.string.login_test_error_password_rules
    }
}


