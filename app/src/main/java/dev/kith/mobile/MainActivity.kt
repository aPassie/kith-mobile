// app entrypoint

package dev.kith.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.kith.mobile.ui.App
import dev.kith.mobile.ui.KithTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KithTheme {
                App()
            }
        }
    }
}
