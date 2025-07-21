package bi.vovota.vovota.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import bi.vovota.vovota.R


@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None
){
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = {Text(text=labelText)},
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(percent = 10),
        visualTransformation = visualTransformation,
        singleLine = true
    )
}

@Composable
fun PasswordField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    keyboardType: KeyboardType = KeyboardType.Password,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction,
){
    var isPasswordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = modifier,
        value= value,
        onValueChange= onValueChange,
        label = {Text(text = labelText)},
        trailingIcon = {
            val icon = if (isPasswordVisible){
                painterResource(id = R.drawable.visible)
            }else{
                painterResource(id = R.drawable.visibility_off)
            }
            IconButton(onClick = {isPasswordVisible = !isPasswordVisible}) {
                Icon(painter = icon, contentDescription = null)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPasswordVisible) visualTransformation else PasswordVisualTransformation() ,
        shape = RoundedCornerShape(percent = 10),
        singleLine = true
    )
}