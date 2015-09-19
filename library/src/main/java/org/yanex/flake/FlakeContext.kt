package org.yanex.flake

import android.content.res.Configuration
import android.os.Bundle
import org.yanex.flake.internal.FlakeContextImpl

abstract class FlakeContext internal constructor() {
    abstract var messageListener: ((Any) -> Unit)?

    abstract fun saveInstanceState(outState: Bundle?)
    abstract fun onConfigurationChanged(newConfig: Configuration?)

    abstract fun <T: Flake<*>> sendMessage(flakeClass: Class<T>, message: Any): Boolean
    abstract fun sendMessageToContext(message: Any): Boolean
    abstract fun sendBroadcastMessage(message: Any)

    abstract fun <T: Any> useComponent(type: Class<T>, instance: T)
    inline fun <reified T : Any> useComponent(instance: T) {
        useComponent(T::class.java, instance)
    }

    abstract fun <T: Any> getComponent(type: Class<T>): T
    inline fun <reified T: Any> getComponent(): T {
        return getComponent(T::class.java)
    }

    inline fun <reified T : Flake<*>> sendMessage(message: Any): Boolean {
        return sendMessage(T::class.java, message)
    }

    companion object {
        @JvmStatic
        fun create(savedInstanceState: Bundle? = null): FlakeContext {
            return FlakeContextImpl(savedInstanceState)
        }
    }
}