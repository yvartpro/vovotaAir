import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import bi.vovota.vovota.ui.Screen
import bi.vovota.vovota.ui.screen.backgroundColor
import bi.vovota.vovota.viewmodel.CallViewModel
import bi.vovota.vovota.viewmodel.ContactViewModel

@Composable
fun ContactList(
    contactViewModel: ContactViewModel,
    onNavigateToAddEdit: () -> Unit,
    callViewModel: CallViewModel,
    navController: NavHostController
) {
    val contacts = contactViewModel.contacts.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(contacts) { contact ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable(onClick = {
                                navController.navigate(Screen.CallScreen.route)
                                callViewModel.startCall(contact.phoneNumber, contact.name)
                            }),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar / Icon
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 12.dp)
                        )

                        // Name and phone
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = contact.name,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Text(
                                text = contact.phoneNumber,
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                        }

                        // Edit morev button
                        IconButton(onClick = {
                            contactViewModel.setContactToEdit(contact)
                            onNavigateToAddEdit() // Edit mode
                        }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Options")
                        }
                    }
                    Divider()
                }
            }

            // Floating Add Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                FloatingActionButton(onClick = {
                    contactViewModel.clearContactToEdit()
                    onNavigateToAddEdit()
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Contact")
                }
            }
        }
    }
}

