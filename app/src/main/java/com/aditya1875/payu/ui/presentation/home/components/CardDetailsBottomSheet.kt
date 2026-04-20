package com.aditya1875.payu.ui.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.aditya1875.payu.ui.presentation.home.screens.CardDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailsBottomSheet(
    current: CardDetails,
    onSave: (CardDetails) -> Unit,
    onDismiss: () -> Unit
) {
    var cardNumber by remember { mutableStateOf(current.cardNumber) }
    var cardHolder by remember { mutableStateOf(current.cardHolder) }
    var expiry by remember { mutableStateOf(current.expiryDate) }
    var bankName by remember { mutableStateOf(current.bankName) }
    var cvv by remember { mutableStateOf("") }          // never saved
    var cvvVisible by remember { mutableStateOf(false) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                "Card details",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Card number with auto-spacing
            OutlinedTextField(
                value = cardNumber,
                onValueChange = { raw ->
                    val digits = raw.filter { it.isDigit() }.take(16)
                    cardNumber = digits.chunked(4).joinToString(" ")
                },
                label = { Text("Card number") },
                placeholder = { Text("•••• •••• •••• ••••") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cardHolder,
                onValueChange = { cardHolder = it.uppercase() },
                label = { Text("Cardholder name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = expiry,
                    onValueChange = { raw ->
                        val digits = raw.filter { it.isDigit() }.take(4)
                        expiry = if (digits.length >= 3)
                            "${digits.take(2)}/${digits.drop(2)}"
                        else digits
                    },
                    label = { Text("Expiry (MM/YY)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                // CVV — input only, NEVER saved
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { cvv = it.filter { c -> c.isDigit() }.take(4) },
                    label = { Text("CVV") },
                    visualTransformation = if (cvvVisible)
                        VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { cvvVisible = !cvvVisible }) {
                            Icon(
                                if (cvvVisible) Icons.Default.VisibilityOff
                                else Icons.Default.Visibility,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = bankName,
                onValueChange = { bankName = it },
                label = { Text("Bank / card name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                "CVV is used for display only and is never stored.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = {
                    onSave(
                        CardDetails(
                            cardNumber = cardNumber,
                            cardHolder = cardHolder,
                            expiryDate = expiry,
                            bankName = bankName
                            // cvv intentionally dropped
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = cardNumber.replace(" ", "").length == 16
            ) {
                Text("Save card")
            }
        }
    }
}