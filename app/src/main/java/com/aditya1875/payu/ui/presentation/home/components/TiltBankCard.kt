package com.aditya1875.payu.ui.presentation.home.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

@Composable
fun TiltBankCard(
    cardNumber: String, cardHolder: String, expiryDate: String, bankName: String,
    modifier: Modifier = Modifier
) {
    var dragX by remember { mutableFloatStateOf(0f) }
    var dragY by remember { mutableFloatStateOf(0f) }

    val rotX by animateFloatAsState(
        (-dragY / 14f).coerceIn(-10f, 10f),
        spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = "rotX"
    )
    val rotY by animateFloatAsState(
        (dragX / 14f).coerceIn(-10f, 10f),
        spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = "rotY"
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val cardScale by animateFloatAsState(
        if (isPressed) 0.97f else 1f,
        spring(stiffness = Spring.StiffnessMedium), label = "cardScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(cardScale)
            .graphicsLayer {
                rotationX = rotX
                rotationY = rotY
                cameraDistance = 12f * density
            }
            .shadow(
                elevation = (8 + abs(rotX) + abs(rotY)).dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color(0xFF4ABFAF).copy(alpha = 0.45f),
                ambientColor = Color(0xFF4ABFAF).copy(alpha = 0.15f)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFFE8D5B7),
                        Color(0xFF8EC5B2),
                        Color(0xFF4ABFAF)
                    )
                )
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = { dragX = 0f; dragY = 0f },
                    onDragCancel = { dragX = 0f; dragY = 0f },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragX += dragAmount.x
                        dragY += dragAmount.y
                    }
                )
            }
            .clickable(interactionSource = interactionSource, indication = null) {}
            .padding(22.dp)
            .height(165.dp)
    ) {
        Box(
            modifier = Modifier
                .size(160.dp).align(Alignment.TopEnd)
                .offset(x = 50.dp, y = (-50).dp).clip(CircleShape)
                .background(Color.White.copy(alpha = 0.06f))
        )

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    bankName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                )
                Row {
                    Box(
                        Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.5f))
                    )
                    Box(
                        Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.4f))
                            .offset(x = (-8).dp)
                    )
                }
            }
            Text(
                cardNumber, style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 2.sp
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Card Holder Name",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White
                        )
                    )
                    Text(
                        cardHolder,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Expired Date",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White
                        )
                    )
                    Text(
                        expiryDate,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}