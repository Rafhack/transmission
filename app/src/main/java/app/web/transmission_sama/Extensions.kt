package app.web.transmission_sama

import android.os.Handler
import kotlin.random.Random

inline fun Handler.postDelayed(delayMillis: Long, crossinline runnable: Handler.(Runnable) -> Unit) =
    postDelayed(object : Runnable {
        override fun run() {
            runnable(this)
        }
    }, delayMillis)

fun Random.nextIntBetween(max: Int, min: Int): Int = nextInt(max - min) + min