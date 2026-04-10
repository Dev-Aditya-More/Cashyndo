package com.aditya1875.payu.ui.auth.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.aditya1875.payu.R
import com.aditya1875.payu.data.repository.auth.GoogleAuthUIClient
import com.aditya1875.payu.ui.auth.viewmodel.AuthViewModel
import com.aditya1875.payu.ui.navigation.Route
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun LoginAuthScreen(navController: NavController) {

    val viewModel: AuthViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    var isSignIn by remember { mutableStateOf(true) }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val background = MaterialTheme.colorScheme.background
    val surface = MaterialTheme.colorScheme.surface
    val onBackground = MaterialTheme.colorScheme.onBackground
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    val authUiClient: GoogleAuthUIClient = koinInject()
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        scope.launch {
            val account = authUiClient.getSignedInAccountFromIntent(result.data ?: return@launch)
            val idToken = account?.idToken
            if (idToken != null) {
                viewModel.signInWithGoogle(idToken)
            } else {
                // account or token null — surface a friendly error
            }
        }
    }

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            navController.navigate(Route.Main.route) {
                popUpTo(Route.Auth.route) { inclusive = true }
            }
        }
    }

    // Clear errors when switching tabs
    LaunchedEffect(isSignIn) {
        viewModel.clearMessages()
        fullName = ""
        email = ""
        password = ""
        confirmPassword = ""
        passwordVisible = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(72.dp))

            // ── Logo ──────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(onBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "C", style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold, color = background
                    )
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                "Welcome to Cashyndo",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "Smart money management, simplified",
                style = MaterialTheme.typography.bodyMedium, color = onSurfaceVariant
            )

            Spacer(Modifier.height(32.dp))

            // ── Card ──────────────────────────────────────────────────────
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = surface,
                tonalElevation = 0.dp
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)) {

                    Text(
                        "Get started",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        if (isSignIn) "Sign in to your account" else "Create your free account",
                        style = MaterialTheme.typography.bodySmall, color = onSurfaceVariant
                    )

                    Spacer(Modifier.height(16.dp))

                    AuthToggle(isSignIn = isSignIn, onToggle = { isSignIn = it })

                    Spacer(Modifier.height(20.dp))

                    // Full name (sign up only)
                    AnimatedVisibility(
                        visible = !isSignIn,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            AuthFieldLabel("Full Name")
                            AuthTextField(
                                value = fullName,
                                onValueChange = { fullName = it; viewModel.clearError() },
                                placeholder = "e.g. Aditya More"
                            )
                            Spacer(Modifier.height(16.dp))
                        }
                    }

                    // Email
                    AuthFieldLabel("Email")
                    AuthTextField(
                        value = email,
                        onValueChange = { email = it; viewModel.clearError() },
                        placeholder = "you@example.com",
                        keyboardType = KeyboardType.Email
                    )

                    Spacer(Modifier.height(16.dp))

                    // Password
                    AuthFieldLabel("Password")
                    AuthTextField(
                        value = password,
                        onValueChange = { password = it; viewModel.clearError() },
                        placeholder = if (isSignIn) "Enter your password" else "Min. 8 chars, include a number",
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onTogglePassword = { passwordVisible = !passwordVisible }
                    )

                    // Password strength hint (sign up only)
                    AnimatedVisibility(
                        visible = !isSignIn && password.isNotEmpty(),
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        PasswordStrengthRow(password = password)
                    }

                    // Confirm password (sign up only)
                    AnimatedVisibility(
                        visible = !isSignIn,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            Spacer(Modifier.height(16.dp))
                            AuthFieldLabel("Confirm Password")
                            AuthTextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it; viewModel.clearError() },
                                placeholder = "Repeat your password",
                                keyboardType = KeyboardType.Password,
                                isPassword = true,
                                passwordVisible = passwordVisible,
                                onTogglePassword = { passwordVisible = !passwordVisible }
                            )
                            // Match indicator
                            if (confirmPassword.isNotEmpty()) {
                                val matches = password == confirmPassword
                                Row(
                                    modifier = Modifier.padding(top = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        if (matches) Icons.Outlined.CheckCircle else Icons.Outlined.Warning,
                                        contentDescription = null,
                                        tint = if (matches) MaterialTheme.colorScheme.tertiary
                                        else MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        if (matches) "Passwords match" else "Passwords don't match",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (matches) MaterialTheme.colorScheme.tertiary
                                        else MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }

                    // Forgot password link (sign in only)
                    if (isSignIn) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { navController.navigate(Route.ForgotPassword.route) }) {
                                Text(
                                    "Forgot password?",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    } else {
                        Spacer(Modifier.height(16.dp))
                    }

                    // Error banner
                    AnimatedVisibility(
                        visible = state.errorMessage != null,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        state.errorMessage?.let { msg ->
                            MessageBanner(message = msg, isError = true)
                        }
                    }

                    // Success banner (email verification sent after signup)
                    AnimatedVisibility(
                        visible = state.successMessage != null && !isSignIn,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        state.successMessage?.let { msg ->
                            MessageBanner(message = msg, isError = false)
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // Primary CTA
                    val btnScale by animateFloatAsState(
                        targetValue = if (state.isLoading) 0.97f else 1f,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium),
                        label = "btnScale"
                    )
                    Button(
                        onClick = {
                            if (isSignIn) viewModel.signIn(email, password)
                            else viewModel.signUp(fullName, email, password, confirmPassword)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .scale(btnScale),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = onBackground, contentColor = background
                        ),
                        enabled = !state.isLoading
                    ) {
                        AnimatedContent(
                            targetState = state.isLoading,
                            label = "btnContent"
                        ) { loading ->
                            if (loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = background,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    if (isSignIn) "Sign In" else "Create Account",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Divider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = onSurfaceVariant.copy(0.25f)
                )
                Text("  OR  ", style = MaterialTheme.typography.bodySmall, color = onSurfaceVariant)
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = onSurfaceVariant.copy(0.25f)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Google sign-in
            OutlinedButton(
                onClick = { launcher.launch(authUiClient.getSignInIntent()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp, onSurfaceVariant.copy(alpha = 0.3f)
                ),
                enabled = !state.isLoading
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icons8_google),
                    contentDescription = "Google",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    "Continue with Google",
                    style = MaterialTheme.typography.titleSmall,
                    color = onBackground
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun PasswordStrengthRow(password: String) {
    val strength = when {
        password.length >= 8 && password.any { it.isDigit() } && password.any { it.isUpperCase() } -> 3
        password.length >= 8 && password.any { it.isDigit() } -> 2
        password.length >= 8 -> 1
        else -> 0
    }
    val label = listOf("Too short", "Weak", "Fair", "Strong")[strength]
    val color = listOf(
        MaterialTheme.colorScheme.error,
        MaterialTheme.colorScheme.error,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary
    )[strength]

    Column(modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            (0..2).forEach { i ->
                val filled = i < strength
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .clip(CircleShape)
                        .background(
                            if (filled) color else MaterialTheme.colorScheme.outlineVariant.copy(
                                alpha = 0.4f
                            )
                        )
                )
            }
        }
        Spacer(Modifier.height(2.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = color)
    }
}

@Composable
private fun MessageBanner(message: String, isError: Boolean) {
    val bgColor = if (isError) MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
    else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
    val textColor = if (isError) MaterialTheme.colorScheme.error
    else MaterialTheme.colorScheme.tertiary
    val icon = if (isError) Icons.Outlined.Warning else Icons.Outlined.CheckCircle

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier
                .size(16.dp)
                .padding(top = 1.dp)
        )
        Text(message, style = MaterialTheme.typography.bodySmall, color = textColor)
    }
}

@Composable
private fun AuthToggle(isSignIn: Boolean, onToggle: (Boolean) -> Unit) {
    val bg = MaterialTheme.colorScheme.background
    val onBg = MaterialTheme.colorScheme.onBackground
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    val surfVar = MaterialTheme.colorScheme.surfaceVariant

    Box(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(50))
        .background(surfVar)) {
        Row(Modifier.fillMaxWidth()) {
            listOf("Sign In" to true, "Sign Up" to false).forEach { (label, value) ->
                val isActive = isSignIn == value
                val scale by animateFloatAsState(
                    if (isActive) 1f else 0.95f,
                    spring(stiffness = Spring.StiffnessMediumLow),
                    label = "toggle"
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .scale(scale)
                        .clip(RoundedCornerShape(50))
                        .background(if (isActive) onBg else Color.Transparent)
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(onClick = { onToggle(value) }, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            label,
                            color = if (isActive) bg else muted,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthFieldLabel(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { onTogglePassword?.invoke() }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Outlined.VisibilityOff
                        else Icons.Outlined.Visibility,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else null,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    )
}