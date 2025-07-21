package bi.vovota.vovota.ui.screen

import bi.vovota.vovota.viewmodel.AuthViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import bi.vovota.vovota.R
import bi.vovota.vovota.viewmodel.AuthState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import bi.vovota.vovota.ui.component.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import bi.vovota.vovota.ui.Screen


@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    modifier: Modifier,
    navController: NavHostController
) {
    val state by viewModel.authState.collectAsState()
    val token by viewModel.tokenStateFlow.collectAsState()
    var isLogin by remember { mutableStateOf(true) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVerify by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .imePadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Image(
            painter = painterResource(id = R.drawable.vovota),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier.size(80.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = if (isLogin) stringResource(R.string.auth_login) else stringResource(R.string.auth_register),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        when (state) {
            is AuthState.Loading -> Text(stringResource(R.string.loading))
            is AuthState.Success -> Text("Welcome! Token: ${(state as AuthState.Success).token}")
            is AuthState.Error -> Text("Error: ${(state as AuthState.Error).message}")
            else -> {}
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            InputField(
                value = username,
                onValueChange = { username = it },
                labelText = stringResource(R.string.auth_name),
                keyboardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )
            PasswordField(
                value = password,
                onValueChange = { password = it },
                labelText = stringResource(R.string.auth_pwd),
                keyboardType = KeyboardType.Password,
                modifier = Modifier.fillMaxWidth(),
                imeAction = ImeAction.Next
            )
            if (!isLogin) {
                PasswordField(
                    value = passwordVerify,
                    onValueChange = { passwordVerify = it },
                    labelText = stringResource(R.string.auth_pwd_v),
                    keyboardType = KeyboardType.Password,
                    modifier = Modifier.fillMaxWidth(),
                    imeAction = ImeAction.Done
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = msg, color = MaterialTheme.colorScheme.error)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                focusManager.clearFocus()
                if(isLogin) {
                    viewModel.login(username, password)
                } else {
                    if (password == passwordVerify) {
                        viewModel.register(username, password)
                    } else {
                        msg = "Password not matching"
                    }
                } },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(2.dp)
        ){
            Text(text = if(isLogin) stringResource(R.string.auth_login) else stringResource(R.string.auth_register))
        }
        when (state) {
            is AuthState.Success -> {
                val loginToken = (state as AuthState.Success).token
                LaunchedEffect(loginToken) {
                    viewModel.saveToken(loginToken)
                }
                //Text("Login Success! Token: $token")
                navController.navigate(Screen.CallScreen.route)
            }

            is AuthState.Error -> {
                Text("Login Failed: ${(state as AuthState.Error).message}")
            }

            is AuthState.Loading -> {
                CircularProgressIndicator()
            }

            else -> {
                // Do nothing
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(text = if (isLogin) stringResource(R.string.auth_new) else stringResource(R.string.auth_already))
            TextButton(onClick = { isLogin = !isLogin}){
                Text(text = if(isLogin) stringResource(R.string.auth_r_now) else stringResource(R.string.auth_l_here))
            }
        }
    }
}
