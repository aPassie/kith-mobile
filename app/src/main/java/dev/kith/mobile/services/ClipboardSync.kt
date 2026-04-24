// clipboard sync service

package dev.kith.mobile.services

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class ClipboardSync(context: Context) {
    private val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    fun read(): String? {
        val clip = cm.primaryClip ?: return null
        if (clip.itemCount == 0) return null
        return clip.getItemAt(0).text?.toString()
    }

    fun write(text: String) {
        cm.setPrimaryClip(ClipData.newPlainText("kith", text))
    }

    fun observe(onChange: (String?) -> Unit) {
        cm.addPrimaryClipChangedListener {
            onChange(read())
        }
    }
}
