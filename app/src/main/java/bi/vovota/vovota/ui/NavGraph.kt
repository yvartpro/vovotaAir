package bi.vovota.vovota.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import bi.vovota.vovota.R
import bi.vovota.vovota.ui.screen.AuthScreen
import bi.vovota.vovota.ui.screen.CallScreen
import bi.vovota.vovota.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VovotaNavGraph(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.call_title)) })
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
                    navController = navController
                )
            }
            composable(route = Screen.AuthScreen.route) {
                AuthScreen(
                    modifier = Modifier,
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }
    }
}

sealed class Screen(val route: String) {
    object CallScreen : Screen("call")
    object AuthScreen : Screen("auth")
}