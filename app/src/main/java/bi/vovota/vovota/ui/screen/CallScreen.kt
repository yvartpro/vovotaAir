package bi.vovota.vovota.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import bi.vovota.vovota.R
import bi.vovota.vovota.ui.Screen

@Composable
fun CallScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.call_title), modifier = Modifier.padding(bottom = 16.dp))
        Button(onClick = { navController.navigate(Screen.AuthScreen.route) }, modifier = Modifier.fillMaxWidth()) {
            Text("Go to Auth")
        }
    }
}