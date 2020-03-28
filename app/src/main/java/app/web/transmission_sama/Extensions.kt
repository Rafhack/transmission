package app.web.transmission_sama

import android.os.Handler

inline fun Handler.postDelayed(delayMillis: Long, crossinline runnable: Handler.(Runnable) -> Unit) =
    postDelayed(object : Runnable {
        override fun run() {
            runnable(this)
        }
    }, delayMillis)