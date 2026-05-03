// rust core facade

package dev.kith.mobile

import uniffi.kith.`init` as nativeInit
import uniffi.kith.ourId as nativeOurId
import uniffi.kith.popClipboard as nativePopClipboard
import uniffi.kith.pushClipboard as nativePushClipboard

object Core {
    fun start(peerIdHex: String) = nativeInit(peerIdHex)
    fun push(text: String) = nativePushClipboard(text)
    fun pop(): String? = nativePopClipboard()
    fun ourId(): String = nativeOurId()
}
