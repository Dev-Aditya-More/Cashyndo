package com.aditya1875.payu.ui.presentation.profile.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aditya1875.payu.ui.presentation.profile.viewmodel.ProfileViewModel
import com.aditya1875.payu.ui.presentation.balances.viewmodel.TransactionViewModel
import com.aditya1875.payu.ui.presentation.home.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    homeViewModel: HomeViewModel = koinViewModel(),
    transactionViewModel: TransactionViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    val totalExpense by transactionViewModel.totalExpense.collectAsStateWithLifecycle()
    val balance by transactionViewModel.balance.collectAsStateWithLifecycle()

    var isPreview by remember { mutableStateOf(true) }

    var showSheet by remember { mutableStateOf(false) }

    // Edit fields
    var editName by remember { mutableStateOf("") }
    var editEmail by remember { mutableStateOf("") }
    var editPassword by remember { mutableStateOf("") }
    var editConfirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val background = MaterialTheme.colorScheme.background
    val onBackground = MaterialTheme.colorScheme.onBackground
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val surface = MaterialTheme.colorScheme.surface

    val firstName = state.name.split(" ").firstOrNull() ?: state.name
    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

    Scaffold(
        containerColor = background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(onBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "P",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = background
                        )
                    )
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    "PayU",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(Modifier.height(25.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    state.name.ifBlank { firstName },
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    listOf("Preview", "Edit").forEach { tab ->
                        val isSelected = (tab == "Preview") == isPreview
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(50))
                                .background(if (isSelected) onBackground else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            TextButton(
                                onClick = { isPreview = tab == "Preview" },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    tab,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (isSelected) background else onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            AnimatedVisibility(
                visible = isPreview,
                enter = fadeIn() + slideInHorizontally(),
                exit = fadeOut() + slideOutHorizontally()
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    ProfileInfoRow(
                        label = "Total spendings:",
                        value = "₹${totalExpense}"
                    )
                    Spacer(Modifier.height(16.dp))
                    ProfileInfoRow(
                        label = "Email:",
                        value = userEmail
                    )
                    Spacer(Modifier.height(16.dp))
                    ProfileInfoRow(
                        label = "Balance:",
                        value = "₹${balance}"
                    )
                }
            }

            AnimatedVisibility(
                visible = !isPreview,
                enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
                exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it })
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                    EditFieldLabel("Full Name")
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter your full name", color = onSurfaceVariant) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = onSurfaceVariant.copy(alpha = 0.2f),
                            focusedBorderColor = onBackground.copy(alpha = 0.4f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            focusedContainerColor = surface
                        )
                    )

                    Spacer(Modifier.height(16.dp))

                    EditFieldLabel("Email")
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter your email", color = onSurfaceVariant) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = onSurfaceVariant.copy(alpha = 0.2f),
                            focusedBorderColor = onBackground.copy(alpha = 0.4f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            focusedContainerColor = surface
                        )
                    )

                    Spacer(Modifier.height(16.dp))

                    EditFieldLabel("Password")
                    OutlinedTextField(
                        value = editPassword,
                        onValueChange = { editPassword = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Create a password", color = onSurfaceVariant) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                    contentDescription = null,
                                    tint = onSurfaceVariant
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = onSurfaceVariant.copy(alpha = 0.2f),
                            focusedBorderColor = onBackground.copy(alpha = 0.4f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            focusedContainerColor = surface
                        )
                    )

                    Spacer(Modifier.height(16.dp))

                    EditFieldLabel("Confirm Password")
                    OutlinedTextField(
                        value = editConfirmPassword,
                        onValueChange = { editConfirmPassword = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Confirm your password", color = onSurfaceVariant) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = onSurfaceVariant.copy(alpha = 0.2f),
                            focusedBorderColor = onBackground.copy(alpha = 0.4f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            focusedContainerColor = surface
                        )
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (editName.isNotBlank()) {
                                viewModel.updateName(editName)
                            }
                            isPreview = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = onBackground,
                            contentColor = background
                        )
                    ) {
                        Text(
                            "Update Details",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }

            Spacer(Modifier.height(100.dp))
        }
    }
}

@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Row {
        Text(
            "$label ",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
private fun EditFieldLabel(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}