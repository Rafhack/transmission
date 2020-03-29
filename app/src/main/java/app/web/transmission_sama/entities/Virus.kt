package app.web.transmission_sama.entities

import android.os.Handler
import app.web.transmission_sama.nextIntBetween
import app.web.transmission_sama.postDelayed
import java.util.*
import kotlin.random.Random

class Virus(onSpreadListener: (Infectable) -> Unit) : Infection(onSpreadListener) {

    private var random = Random(System.currentTimeMillis())

    override var name: String = "Virus"
    override var id: Long = UUID.randomUUID().mostSignificantBits
    override var averageLifetime: Float = 0f

    override fun <T : Infectable> infect(infectable: T) {
        if (!infectable.isInfected) {
            infectable.isInfected = true
            val infectionDelay = random.nextIntBetween(800, 500).toLong()
            Handler().postDelayed(5000) {
                onSpreadListener(infectable)
                postDelayed(it, infectionDelay)
            }
        }
    }

}