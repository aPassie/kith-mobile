// pair screen

package dev.kith.mobile.ui

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.kith.mobile.services.SyncService

@Composable
fun PairScreen(onPaired: () -> Unit) {
    val ctx = LocalContext.current
    val prefs = ctx.getSharedPreferences("kith", android.content.Context.MODE_PRIVATE)
    var peer by remember { mutableStateOf(prefs.getString("peer_id", "") ?: "") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("paste laptop endpoint id")
        OutlinedTextField(
            value = peer,
            onValueChange = { peer = it.trim() },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Button(
            onClick = {
                prefs.edit().putString("peer_id", peer).apply()
                ctx.startForegroundService(Intent(ctx, SyncService::class.java))
                onPaired()
            }
        ) {
            Text("connect")
        }
    }
}
