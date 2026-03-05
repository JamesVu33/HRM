package com.example.ihrm.ui.auth

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ihrm.ui.components.CustomButton
import com.example.ihrm.ui.components.ButtonSize
import com.example.ihrm.ui.components.ButtonVariant
import com.example.ihrm.ui.theme.Error
import com.example.ihrm.ui.theme.Neutral200
import com.example.ihrm.ui.theme.Neutral400
import com.example.ihrm.ui.theme.Neutral600
import com.example.ihrm.ui.theme.Primary300
import com.example.ihrm.ui.theme.Primary400
import com.example.ihrm.ui.theme.Primary500

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Primary300.copy(alpha = 0.2f),
                            Color.White
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with Logo
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo Circle
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Primary500),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "S",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Shinhan DS",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Neutral600
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Login Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Title
                    Text(
                        text = "Welcome",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Neutral600,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Subtitle
                    Text(
                        text = "Enter your credentials to access your account",
                        fontSize = 14.sp,
                        color = Neutral400,
                        modifier = Modifier.fillMaxWidth(),
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Email Field
                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = viewModel::updateEmail,
                        label = { Text("Email *") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email",
                                tint = if (uiState.emailError != null) Error else Neutral400
                            )
                        },
                        isError = uiState.emailError != null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (uiState.emailError != null) Error else Primary400,
                            unfocusedBorderColor = if (uiState.emailError != null) Error else Neutral200,
                            errorBorderColor = Error
                        ),
                        shape = RoundedCornerShape(8.dp),
                        placeholder = { Text("Enter your email", color = Neutral400) }
                    )

                    // Email Error Message
                    if (uiState.emailError != null) {
                        Text(
                            text = uiState.emailError ?: "",
                            color = Error,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Field
                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = viewModel::updatePassword,
                        label = { Text("Password *") },
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
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password",
                                tint = if (uiState.passwordError != null) Error else Neutral400
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                                Icon(
                                    imageVector = if (uiState.isPasswordVisible) {
                                        Icons.Default.Favorite
                                    } else {
                                        Icons.Default.FavoriteBorder
                                    },
                                    contentDescription = if (uiState.isPasswordVisible) {
                                        "Hide password"
                                    } else {
                                        "Show password"
                                    },
                                    tint = Neutral400
                                )
                            }
                        },
                        isError = uiState.passwordError != null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (uiState.passwordError != null) Error else Primary400,
                            unfocusedBorderColor = if (uiState.passwordError != null) Error else Neutral200,
                            errorBorderColor = Error
                        ),
                        shape = RoundedCornerShape(8.dp),
                        placeholder = { Text("Enter your password", color = Neutral400) }
                    )

                    // Password Error Message
                    if (uiState.passwordError != null) {
                        Text(
                            text = uiState.passwordError ?: "",
                            color = Error,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Login Button
                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        }
                    } else {
                        CustomButton(
                            text = "Login",
                            onClick = { viewModel.login(onLoginSuccess) },
                            modifier = Modifier.fillMaxWidth(),
                            size = ButtonSize.Large,
                            variant = ButtonVariant.Primary,
                            enabled = !uiState.isLoading
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Forgot Password Link
                    TextButton(
                        onClick = { /* TODO: Implement forgot password */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Forgot your password?",
                            color = Primary400,
                            fontSize = 14.sp
                        )
                    }

                    // Sign Up Link
                    TextButton(
                        onClick = onNavigateToSignUp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Don't have an account? Sign Up",
                            color = Primary400,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Footer
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "More friendly, secure, creative",
                        fontSize = 12.sp,
                        color = Primary400,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Connect more, Create the most",
                        fontSize = 12.sp,
                        color = Primary400,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}