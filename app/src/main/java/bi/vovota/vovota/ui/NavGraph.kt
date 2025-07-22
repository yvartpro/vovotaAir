package bi.vovota.vovota.ui

import ContactList
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bi.vovota.vovota.R
import bi.vovota.vovota.ui.screen.AddEditContactScreen
import bi.vovota.vovota.ui.screen.AuthScreen
import bi.vovota.vovota.ui.screen.CallScreen
import bi.vovota.vovota.viewmodel.AuthViewModel
import bi.vovota.vovota.viewmodel.CallViewModel
import bi.vovota.vovota.viewmodel.ContactViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VovotaNavGraph(
    authViewModel: AuthViewModel = hiltViewModel(),
    callViewModel: CallViewModel = hiltViewModel(),
    contactViewModel: ContactViewModel = hiltViewModel()

) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        topBar = {
            if (currentRoute != Screen.AuthScreen.route) {
                var title by remember { mutableStateOf("") }
                when (currentRoute) {
                    Screen.CallScreen.route -> title = stringResource(R.string.call_title)
                    Screen.AddEditContactScreen.route -> title = "Contact"
                    Screen.ContactList.route -> title = "Contact list"
                }
                TopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        if (currentRoute != Screen.CallScreen.route) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    },
                    actions =  {
                        if (currentRoute == Screen.CallScreen.route) {
                            IconButton(onClick = { navController.navigate(Screen.ContactList.route)}) {
                                Icon(painter = painterResource(R.drawable.ic_contact), tint = MaterialTheme.colorScheme.primary, contentDescription = "Contacts")
                            }
                        }
                    }
                )
            }
        }
    ) {  innerP->
        NavHost(
            navController = navController,
            startDestination = Screen.CallScreen.route,
            modifier = Modifier.padding(innerP)
        ) {
            composable(route = Screen.CallScreen.route) {
                CallScreen(
                    authViewModel = authViewModel,
                    navController = navController,
                    callViewModel = callViewModel,
                    contactViewModel = contactViewModel
                )
            }
            composable(route = Screen.AuthScreen.route) {
                AuthScreen(
                    modifier = Modifier,
                    navController = navController,
                    authViewModel = authViewModel
                )
            }

            composable(route = Screen.ContactList.route) {
                ContactList(
                    contactViewModel = contactViewModel,
                    onNavigateToAddEdit = { navController.navigate("add_edit") },
                    callViewModel = callViewModel,
                    navController = navController
                )
            }

            composable(route = Screen.AddEditContactScreen.route) {
                AddEditContactScreen(
                    contactViewModel = contactViewModel,
                    onDone = { navController.popBackStack() },
                    contactToEdit = contactViewModel.contactToEdit.value,
                )
            }
        }
    }
}

sealed class Screen(val route: String) {
    object CallScreen : Screen("call")
    object AuthScreen : Screen("auth")
    object AddEditContactScreen: Screen("add_edit")
    object ContactList: Screen("contact_list")
}