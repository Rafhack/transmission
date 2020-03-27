package app.web.transmission_sama.environment

import app.web.transmission_sama.entities.Positionable

abstract class Drawer<in T : Positionable> {

    abstract fun draw(subject: T)

}