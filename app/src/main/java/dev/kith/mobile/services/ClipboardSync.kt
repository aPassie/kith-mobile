// clipboard <-> core glue

package dev.kith.mobile.services

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import dev.kith.mobile.Core

class ClipboardSync(context: Context) {
    private val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    private var lastApplied: String? = null

    private val listener = ClipboardManager.OnPrimaryClipChangedListener {
        val text = read() ?: return@OnPrimaryClipChangedListener
        if (text == lastApplied) return@OnPrimaryClipChangedListener
        try {
            Core.push(text)
        } catch (e: Throwable) {
            android.util.Log.w("kith", "push failed: ${e.message}")
        }
    }

    fun start() {
        cm.addPrimaryClipChangedListener(listener)
    }

    fun stop() {
        cm.removePrimaryClipChangedListener(listener)
    }

    fun applyIncoming(text: String) {
        lastApplied = text
        cm.setPrimaryClip(ClipData.newPlainText("kith", text))
    }

    private fun read(): String? {
        val clip = cm.primaryClip ?: return null
        if (clip.itemCount == 0) return null
        return clip.getItemAt(0).text?.toString()
    }
}
