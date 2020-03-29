package app.web.transmission_sama.entities

abstract class Infection(protected var onSpreadListener: (Infectable) -> Unit) {
    abstract var name: String
    abstract var id: Long
    abstract var averageLifetime: Float

    abstract fun <T : Infectable> infect(infectable: T)
}