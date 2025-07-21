package bi.vovota.vovota

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import bi.vovota.vovota.ui.VovotaNavGraph
import bi.vovota.vovota.ui.theme.VovotaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VovotaTheme {
                VovotaNavGraph()
            }
        }
    }
}


