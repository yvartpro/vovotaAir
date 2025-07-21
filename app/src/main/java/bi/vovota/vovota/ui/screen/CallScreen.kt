package bi.vovota.vovota.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bi.vovota.vovota.ui.theme.blueVt

val backgroundColor = Color(0xFFF0F2F5)
val surfaceColor = Color(0xFFFFFFFF)
val onSurfaceColor = Color(0xFF303238)
val subtleAccentColor = Color(0xFFD0D3D9)
val callButtonColor = blueVt//Color(0xFF4CAF50)
val onCallButtonColor = Color.White

@Composable
fun CallScreen() {
    var enteredNumber by remember { mutableStateOf("") }
    var callOngoing by remember { mutableStateOf(false) }

    // Fake auto-end call after 3 seconds
    LaunchedEffect(callOngoing) {
        if (callOngoing) {
            kotlinx.coroutines.delay(3000)
            callOngoing = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Call Status Text (Dynamic) ---
            AnimatedVisibility(visible = callOngoing) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        text = "Calling...",
                        color = Color.Red,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f)
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        text =  enteredNumber,
                        fontSize = if (enteredNumber.length > 8) 38.sp else 48.sp,
                        color = if (enteredNumber.isEmpty()) subtleAccentColor else onSurfaceColor,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (!callOngoing){
                // --- Number Display ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f)
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        text = if (enteredNumber.isEmpty()) "Enter number" else enteredNumber,
                        fontSize = if (enteredNumber.length > 8) 38.sp else 48.sp,
                        color = if (enteredNumber.isEmpty()) subtleAccentColor else onSurfaceColor,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                // --- Action Row ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SmallActionButton(
                        icon = Icons.TwoTone.Add,
                        contentDescription = "Save Contact",
                        onClick = { /* Save logic */ },
                        enabled = enteredNumber.isNotEmpty()
                    )

                    SmallActionButton(
                        icon = Icons.Filled.ArrowBack,
                        contentDescription = "Backspace",
                        onClick = {
                            if (enteredNumber.isNotEmpty()) {
                                enteredNumber = enteredNumber.dropLast(1)
                            }
                        },
                        enabled = enteredNumber.isNotEmpty()
                    )
                }

                // --- Dial Pad ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.6f),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val dialPadRows = listOf(
                        listOf("1", "2", "3"),
                        listOf("4", "5", "6"),
                        listOf("7", "8", "9"),
                        listOf("*", "0", "#")
                    )

                    dialPadRows.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            row.forEach { key ->
                                DialPadButton(
                                    text = key,
                                    onClick = { enteredNumber += key },
                                    modifier = Modifier.size(64.dp) // Slightly smaller for better fit
                                )
                            }
                        }
                    }
                }
            }

            // --- Call Button ---
            Spacer(modifier = Modifier.weight(0.1f))

            Button(
                onClick = {
                    if (!callOngoing && enteredNumber.isNotEmpty()) {
                        callOngoing = true
                    }
                },
                enabled = enteredNumber.length in 1..15,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(64.dp)
                    .padding(bottom = 24.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (callOngoing) Color.Red else callButtonColor,
                    contentColor = onCallButtonColor,
                    disabledContainerColor = subtleAccentColor.copy(alpha = 0.5f)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = "Call",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
fun DialPadButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(surfaceColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            color = onSurfaceColor,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun SmallActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = when {
        !enabled -> subtleAccentColor.copy(alpha = 0.2f)
        isPressed -> subtleAccentColor.copy(alpha = 0.3f)
        else -> surfaceColor.copy(alpha = 0.1f)
    }
    val iconTintColor = if (enabled) onSurfaceColor else onSurfaceColor.copy(alpha = 0.4f)

    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = backgroundColor),
        interactionSource = interactionSource,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = iconTintColor,
            disabledContentColor = onSurfaceColor.copy(alpha = 0.4f)
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}
