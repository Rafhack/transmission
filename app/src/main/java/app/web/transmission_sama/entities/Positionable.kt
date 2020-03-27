package app.web.transmission_sama.entities

import app.web.transmission_sama.environment.Drawer

abstract class Positionable {
    abstract var position: Pair<Float, Float>
    abstract var id: Long

    lateinit var drawer: Drawer<out Positionable>

    inline fun <reified T : Positionable> attachDrawer(drawer: Drawer<in T>) {
        this.drawer = drawer
        drawer.setSubject(this as T)
    }
}