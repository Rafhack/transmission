package app.web.transmission_sama.environment

import app.web.transmission_sama.entities.Positionable

abstract class Drawer<T : Positionable> {

    protected lateinit var innerSubject: T

    abstract fun draw()
    abstract fun update()

    fun setSubject(subject: T) {
        innerSubject = subject
    }

}