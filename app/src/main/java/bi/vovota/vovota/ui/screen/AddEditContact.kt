package bi.vovota.vovota.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import bi.vovota.vovota.Contact
import bi.vovota.vovota.viewmodel.ContactViewModel

@Composable
fun AddEditContactScreen(
    contactViewModel: ContactViewModel,
    contactToEdit: Contact?,
    onDone: () -> Unit
) {
    val name by contactViewModel.name.collectAsState()
    val number by contactViewModel.number.collectAsState()


    LaunchedEffect(contactToEdit) {
        if (contactToEdit != null) {
            contactViewModel.setForm(contactToEdit)
        } else {
            contactViewModel.clearForm()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = name,
            onValueChange = { contactViewModel.onNameChange(it) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )

        TextField(
            value = number,
            onValueChange = { contactViewModel.onNumberChange(it) },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = onDone) {
                Text("Cancel")
            }
            Button(onClick = {
                contactViewModel.saveContact()
                onDone()
            }) {
                Text(if (contactToEdit != null) "Save" else "Add")
            }
        }

        if (contactToEdit != null) {
            Button(
                onClick = {
                    contactViewModel.deleteContact(contactToEdit)
                    onDone()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFCDD2),
                    contentColor = Color(0xFFB71C1C)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete")
            }
        }
    }
}