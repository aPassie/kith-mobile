// rust core facade

package dev.kith.mobile

import uniffi.kith.greet as nativeGreet

object Core {
    fun greet(name: String): String = nativeGreet(name)
}
