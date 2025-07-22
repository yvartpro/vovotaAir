package bi.vovota.vovota.ui.screen

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import bi.vovota.vovota.R
import bi.vovota.vovota.ui.Screen
import bi.vovota.vovota.ui.theme.blueVt
import bi.vovota.vovota.viewmodel.AuthViewModel
import bi.vovota.vovota.viewmodel.CallViewModel
import bi.vovota.vovota.viewmodel.ContactViewModel
import kotlinx.coroutines.launch

val backgroundColor = Color(0xFFF0F2F5)
val surfaceColor = Color(0xFFFFFFFF)
val onSurfaceColor = Color(0xFF303238)
val subtleAccentColor = Color(0xFFD0D3D9)
val callButtonColor = blueVt//Color(0xFF4CAF50)
val onCallButtonColor = Color.White

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CallScreen(
    authViewModel: AuthViewModel,
    navController: NavHostController,
    callViewModel: CallViewModel,
    contactViewModel: ContactViewModel
) {
    val token by authViewModel.tokenStateFlow.collectAsState()
    var enteredNumber = callViewModel.enteredNumber.collectAsState().value
    var callOngoing = callViewModel.callOngoing.collectAsState().value
    val scope = rememberCoroutineScope()

    //check if user ia authenticated
//    LaunchedEffect(Unit) {
//        if (token.isNullOrBlank()) {
//            navController.navigate(Screen.AuthScreen.route)
//        }
//    }
    // Fake auto-end call after 3 seconds
    LaunchedEffect(callOngoing) {
        if (callOngoing) {
            kotlinx.coroutines.delay(60000)
            callViewModel.endCall()
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
                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = callViewModel.name.value,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 28.sp,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text =  enteredNumber,
                        fontSize = 28.sp,
                        color = if (enteredNumber.isEmpty()) subtleAccentColor else onSurfaceColor,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Calling...",
                        color = Color.DarkGray,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
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
                        text = if (enteredNumber.isEmpty()) "" else enteredNumber,
                        fontSize = 38.sp,
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
                        icon = R.drawable.ic_add_contact,
                        contentDescription = "Save Contact",
                        onClick = {
                            if (enteredNumber.isNotEmpty()) {
                                callViewModel.setContactNumber(enteredNumber)
                                navController.navigate(Screen.AddEditContactScreen.route)
                            }
                        },
                        enabled = enteredNumber.isNotEmpty(),
                    )

                    SmallActionButton(
                        icon = R.drawable.ic_clear,
                        contentDescription = "Backspace",
                        onClick = {
                            if (enteredNumber.isNotEmpty()) {
                                callViewModel.backspace()
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
                                    onClick = { callViewModel.appendDigit(key) },
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
                        scope.launch {
                            val user = contactViewModel.getContactByNumber(enteredNumber)
                            callViewModel.startCall(user?.phoneNumber ?: enteredNumber, user?.name ?: "Unknown")
                        }
                    } else if (callOngoing) {
                        callViewModel.endCall()
                        callViewModel.clearNumber()
                    }
                },
                enabled = enteredNumber.length in 1..5,
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
    @DrawableRes icon: Int,
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
            painter = painterResource(icon),
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}
