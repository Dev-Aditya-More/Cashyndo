package com.aditya1875.payu.ui.auth.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    // Fields
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
            val account = authUiClient.getSignedInAccountFromIntent(result.data!!)
            account?.idToken?.let {
                viewModel.signInWithGoogle(it)
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

            Spacer(Modifier.height(80.dp))

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(onBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "P",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = background
                    )
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                "Welcome to PayU",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = onBackground
                )
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Send money globally with the real exchange rate",
                style = MaterialTheme.typography.bodyMedium,
                color = onSurfaceVariant
            )

            Spacer(Modifier.height(32.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = surface,
                tonalElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        "Get started",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        "Sign in to your account or create a new one",
                        style = MaterialTheme.typography.bodySmall,
                        color = onSurfaceVariant
                    )

                    Spacer(Modifier.height(16.dp))

                    // Toggle
                    AuthToggle(
                        isSignIn = isSignIn,
                        onToggle = { isSignIn = it }
                    )

                    Spacer(Modifier.height(20.dp))

                    // Fields
                    AnimatedVisibility(
                        visible = !isSignIn,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            AuthFieldLabel("Full Name")
                            AuthTextField(
                                value = fullName,
                                onValueChange = { fullName = it },
                                placeholder = "Enter your full name"
                            )
                            Spacer(Modifier.height(16.dp))
                        }
                    }

                    AuthFieldLabel("Email")
                    AuthTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Enter your email",
                        keyboardType = KeyboardType.Email
                    )

                    Spacer(Modifier.height(16.dp))

                    AuthFieldLabel("Password")
                    AuthTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = if (isSignIn) "Enter your password" else "Create a password",
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onTogglePassword = { passwordVisible = !passwordVisible }
                    )

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
                                onValueChange = { confirmPassword = it },
                                placeholder = "Confirm your password",
                                keyboardType = KeyboardType.Password,
                                isPassword = true,
                                passwordVisible = passwordVisible,
                                onTogglePassword = { passwordVisible = !passwordVisible }
                            )
                        }
                    }

                    if (isSignIn) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = {
                                navController.navigate(Route.ForgotPassword.route)
                            } ) {
                                Text(
                                    "Forgot password?",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    } else {
                        Spacer(Modifier.height(16.dp))
                    }

                    if (state.errorMessage != null) {
                        Text(
                            state.errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Button(
                        onClick = {
                            if (isSignIn) {
                                viewModel.signIn(email, password)
                            } else {
                                viewModel.signUp(fullName, email, password, confirmPassword)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = onBackground,
                            contentColor = background
                        ),
                        enabled = !state.isLoading
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = background,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                if (isSignIn) "Sign In" else "Create Account",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.weight(1f), color = onSurfaceVariant.copy(0.3f))
                Text(
                    " OR ",
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurfaceVariant
                )
                Divider(modifier = Modifier.weight(1f), color = onSurfaceVariant.copy(0.3f))
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    launcher.launch(authUiClient.getSignInIntent())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icons8_google),
                    contentDescription = "Google",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Continue with Google")
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun AuthToggle(
    isSignIn: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val surface = MaterialTheme.colorScheme.surface
    val onBackground = MaterialTheme.colorScheme.onBackground
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            // Sign In tab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(if (isSignIn) onBackground else Color.Transparent)
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = { onToggle(true) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Sign In",
                        color = if (isSignIn) surface else onSurfaceVariant,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = if (isSignIn) FontWeight.SemiBold else FontWeight.Normal
                        )
                    )
                }
            }

            // Sign Up tab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(if (!isSignIn) onBackground else Color.Transparent)
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = { onToggle(false) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Sign Up",
                        color = if (!isSignIn) surface else onSurfaceVariant,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = if (!isSignIn) FontWeight.SemiBold else FontWeight.Normal
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun AuthFieldLabel(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.SemiBold
        ),
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
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { onTogglePassword?.invoke() }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Outlined.VisibilityOff
                        else
                            Icons.Outlined.Visibility,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else null,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    )
}