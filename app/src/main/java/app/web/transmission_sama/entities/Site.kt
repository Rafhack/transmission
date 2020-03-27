package app.web.transmission_sama.entities

class Site : Infectable, Positionable {
    override var isInfected: Boolean = false
    override var resistanceToInfection: Float = 0f
    override var immunities: List<Long> = arrayListOf()
    override var infectionRatio: Float = 0f
    override var position: Pair<Float, Float> = 0f to 0f
    var area: Int = 0
}