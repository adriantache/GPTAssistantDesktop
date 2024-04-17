package com.adriantache

import android.content.Context
import java.lang.ref.WeakReference

object ContextProvider {
    lateinit var context: WeakReference<Context>

    fun init(context: Context) {
        this.context = WeakReference(context)
    }
}
