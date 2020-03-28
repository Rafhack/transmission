package app.web.transmission_sama.entities

class Person : Infectable, Positionable() {
    override var isInfected: Boolean = false
    override var resistanceToInfection: Float = 0f
    override var immunities: List<Long> = arrayListOf()
    override var infectionRatio: Float = 0f
    override var position: Pair<Float, Float> = 0f to 0f
    override var id: Long = 0L
}