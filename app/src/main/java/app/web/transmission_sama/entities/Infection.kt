package app.web.transmission_sama.entities

interface Infection {
    var name: String
    var id: Long
    var transmissionRatio: Float
    var averageLifetime: Float
}