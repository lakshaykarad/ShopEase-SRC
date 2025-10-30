package com.example.shopease.ui.theme.AuthViewModel


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopease.data.model.Routes
import com.example.shopease.ui.theme.Blue40

@Composable
fun RegisterScreen(viewModel: RegisterViewModel = viewModel(), navController: NavController) {

    val state = viewModel.state

    LaunchedEffect(state.isRegister) {
        if (state.isRegister) {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.REGISTER) { inclusive = true }
            }
        }
    }

    // Background Gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color.White)
                )
            )
            .padding(32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Welcome Text
            Text(
                text = "Welcome to ShopEase",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
                color = Blue40
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Create an account to get started",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Auth Fields
            AuthTextField(
                value = state.fullName,
                onValueChange = { viewModel.onFullNameChange(it) },
                label = "Full Name",
                placeholder = "John Doe",
                leadingIcon = Icons.Default.Person
            )

            AuthTextField(
                value = state.mobile,
                onValueChange = { viewModel.onMobileChange(it) },
                label = "Mobile Number",
                placeholder = "1234567890",
                leadingIcon = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone
            )

            AuthTextField(
                value = state.email,
                onValueChange = {
                    viewModel.onEmailChange(it)
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                        viewModel.checkFirstTimeUser()
                    }
                },
                label = "Email",
                placeholder = "example@mail.com",
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            AuthTextField(
                value = state.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = "Password",
                placeholder = "********",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Register Button
            Button(
                onClick = { viewModel.registerUser() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue40),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(text = "Register")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error Message
            state.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Terms & Conditions
            Text(
                text = buildAnnotatedString {
                    append("By registering, you agree to our ")
                    withStyle(style = SpanStyle(color = Blue40)) {
                        append("Terms & Conditions")
                    }
                },
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp),
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Already have an account
            Text(
                text = buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(style = SpanStyle(color = Blue40)) {
                        append("Login")
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    navController.navigate(Routes.LOGIN)
                }
            )
        }
    }
}


@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = "$label Icon") },
        trailingIcon = trailingIcon,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Blue40,
            unfocusedBorderColor = Blue40,
            focusedLabelColor = Blue40
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation
    )
}