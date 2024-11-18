package com.adriantache

import android.content.Context
import java.lang.ref.WeakReference

// TODO: remove this helper and just use composables to get context
object ContextProvider {
    lateinit var context: WeakReference<Context>

    fun init(context: Context) {
        this.context = WeakReference(context)
    }
}
