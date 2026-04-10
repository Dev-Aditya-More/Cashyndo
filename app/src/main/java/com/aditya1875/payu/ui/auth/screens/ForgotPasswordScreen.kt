package com.aditya1875.payu.ui.auth.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.aditya1875.payu.ui.auth.viewmodel.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: AuthViewModel = koinViewModel()
) {
    var email by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsStateWithLifecycle()

    val background = MaterialTheme.colorScheme.background
    val surface = MaterialTheme.colorScheme.surface
    val onBackground = MaterialTheme.colorScheme.onBackground
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val tertiary = MaterialTheme.colorScheme.tertiary

    LaunchedEffect(Unit) { viewModel.clearMessages() }

    val sent = state.successMessage != null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
        ) {

            Spacer(Modifier.height(24.dp))

            // Back button
            IconButton(
                onClick = {
                    viewModel.clearMessages()
                    navController.popBackStack()
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "Back", tint = onBackground)
            }

            Spacer(Modifier.height(32.dp))

            // ── Icon ─────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        if (sent) tertiary.copy(alpha = 0.12f)
                        else onBackground.copy(alpha = 0.08f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(targetState = sent, label = "iconAnim") { isSent ->
                    Icon(
                        imageVector = if (isSent) Icons.Outlined.CheckCircle else Icons.Outlined.Email,
                        contentDescription = null,
                        tint = if (isSent) tertiary else onBackground,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Title ─────────────────────────────────────────────────────
            AnimatedContent(targetState = sent, label = "titleAnim") { isSent ->
                Column {
                    Text(
                        if (isSent) "Check your inbox" else "Reset your password",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        if (isSent)
                            "We sent a reset link to $email. It may take a minute to arrive — check your spam folder too."
                        else
                            "Enter the email address linked to your Cashyndo account and we'll send you a reset link.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // ── Form or success card ──────────────────────────────────────
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = surface
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    AnimatedVisibility(
                        visible = !sent,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            Text(
                                "Email address",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it; viewModel.clearError() },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(
                                        "you@example.com",
                                        color = onSurfaceVariant.copy(0.5f)
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Email, contentDescription = null,
                                        tint = onSurfaceVariant, modifier = Modifier.size(20.dp)
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = onBackground.copy(alpha = 0.5f),
                                    unfocusedBorderColor = onSurfaceVariant.copy(alpha = 0.2f),
                                    focusedContainerColor = surface,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                                        0.5f
                                    )
                                )
                            )

                            Spacer(Modifier.height(8.dp))
                        }
                    }

                    // Error banner
                    AnimatedVisibility(
                        visible = state.errorMessage != null,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        state.errorMessage?.let { msg ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Warning, null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    msg, style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }

                    // Primary button — send link or try again
                    val btnScale by animateFloatAsState(
                        targetValue = if (state.isLoading) 0.97f else 1f,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium),
                        label = "fpBtnScale"
                    )
                    Button(
                        onClick = {
                            if (sent) {
                                // "Try a different email" — reset state
                                viewModel.clearMessages()
                                email = ""
                            } else {
                                viewModel.resetPassword(email)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .scale(btnScale),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (sent) tertiary else onBackground,
                            contentColor = if (sent) MaterialTheme.colorScheme.onPrimary else background
                        ),
                        enabled = !state.isLoading
                    ) {
                        AnimatedContent(
                            targetState = state.isLoading to sent,
                            label = "fpBtnContent"
                        ) { (loading, isSent) ->
                            if (loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = background,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    if (isSent) "Try a different email" else "Send Reset Link",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Back to login link
            TextButton(
                onClick = {
                    viewModel.clearMessages()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Back to Sign In",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = onBackground,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}