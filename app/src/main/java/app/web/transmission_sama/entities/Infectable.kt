package app.web.transmission_sama.entities

interface Infectable {
    var isInfected: Boolean
    var resistanceToInfection: Float
    var immunities: List<Long>
    var infectionRatio: Float
}