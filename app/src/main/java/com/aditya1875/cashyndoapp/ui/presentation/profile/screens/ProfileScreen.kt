package com.aditya1875.cashyndoapp.ui.presentation.profile.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aditya1875.cashyndoapp.ui.presentation.profile.viewmodel.ProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(navController: NavController) {

    val viewModel: ProfileViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    val primary = MaterialTheme.colorScheme.primary
    val surface = MaterialTheme.colorScheme.surface
    val surfaceVar = MaterialTheme.colorScheme.surfaceVariant
    val onSurfVar = MaterialTheme.colorScheme.onSurfaceVariant
    val error = MaterialTheme.colorScheme.error
    val background = MaterialTheme.colorScheme.background

    val firstName = state.name.split(" ").firstOrNull() ?: state.name

    Scaffold(
        containerColor = background,
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(24.dp))

            // Avatar
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(primary.copy(alpha = 0.15f))
                        .border(2.dp, primary, RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = primary,
                        modifier = Modifier.size(52.dp)
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // Name
            Text(
                firstName,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(32.dp))

            // ── Preferences ─────────────────────
            SectionHeader("PREFERENCES")

            // Currency
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.medium,
                color = surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(Modifier.weight(1f)) {
                        Text("Currency")
                        Text(
                            "Default transaction display",
                            style = MaterialTheme.typography.bodySmall,
                            color = onSurfVar
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("USD", "EUR", "INR").forEach { cur ->

                            val isSelected = cur == state.currency

                            Surface(
                                onClick = { viewModel.onCurrencyChange(cur) },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) primary else surfaceVar
                            ) {
                                Text(
                                    cur,
                                    modifier = Modifier.padding(8.dp),
                                    color = if (isSelected)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        onSurfVar
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // Dark Mode
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.medium,
                color = surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(Modifier.weight(1f)) {
                        Text("Dark Mode")
                        Text(
                            "App theme",
                            style = MaterialTheme.typography.bodySmall,
                            color = onSurfVar
                        )
                    }

                    Switch(
                        checked = state.isDarkMode,
                        onCheckedChange = { viewModel.onThemeChange(it) }
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── Actions ─────────────────────

            SectionHeader("DATA")

            ActionRow(
                icon = Icons.Outlined.Upload,
                iconBg = primary.copy(alpha = 0.1f),
                iconTint = primary,
                label = "Export Data",
                onClick = { viewModel.exportData() }
            )

            Spacer(Modifier.height(10.dp))

            ActionRow(
                icon = Icons.Outlined.RestartAlt,
                iconBg = error.copy(alpha = 0.1f),
                iconTint = error,
                label = "Reset Data",
                labelColor = error,
                trailingIcon = Icons.Default.Warning,
                trailingTint = error,
                onClick = { viewModel.resetData() }
            )

            Spacer(Modifier.height(10.dp))

            ActionRow(
                icon = Icons.Outlined.Logout,
                iconBg = surfaceVar,
                iconTint = onSurfVar,
                label = "Logout",
                onClick = {
                    viewModel.logout()
                    navController.navigate("auth") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    )
}

@Composable
private fun ActionRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    label: String,
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    trailingIcon: ImageVector = Icons.Outlined.ChevronRight,
    trailingTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                label,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold, color = labelColor
                ),
                modifier = Modifier.weight(1f)
            )
            Icon(
                trailingIcon,
                contentDescription = null,
                tint = trailingTint,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}