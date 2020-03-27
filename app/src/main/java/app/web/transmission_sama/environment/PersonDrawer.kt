package app.web.transmission_sama.environment

import android.view.View
import android.widget.FrameLayout
import app.web.transmission_sama.R
import app.web.transmission_sama.entities.Person
import app.web.transmission_sama.ui.PersonView

class PersonDrawer(private var environment: FrameLayout) : Drawer<Person>() {

    private var count = 0

    override fun draw(subject: Person) {
        getPersonView(subject).apply {
            environment.addView(this)
            layoutParams = FrameLayout.LayoutParams(100, 100)
            x = subject.position.first
            y = subject.position.second
        }
    }

    private fun getPersonView(person: Person): View = PersonView(environment.context).apply {
        personColor = if (person.isInfected) R.color.infected else R.color.not_infected
    }

}