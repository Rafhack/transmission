package app.web.transmission_sama.environment

import android.view.View
import android.widget.FrameLayout
import app.web.transmission_sama.R
import app.web.transmission_sama.entities.Person
import app.web.transmission_sama.ui.PersonView

class PersonDrawer(private var environment: FrameLayout) : Drawer<Person>() {

    override fun draw() {
        getPersonView().apply {
            environment.addView(this)
            layoutParams = FrameLayout.LayoutParams(100, 100)
            x = innerSubject.position.first
            y = innerSubject.position.second
        }
    }

    override fun update() {
        environment.findViewWithTag<PersonView>(innerSubject.id).apply {
            setupView(this)
        }
    }

    private fun getPersonView(): View = PersonView(environment.context).apply {
        tag = innerSubject.id
        setupView(this)
    }

    private fun setupView(personView: PersonView) = personView.apply {
        personColor = if (innerSubject.isInfected) R.color.infected_person else R.color.not_infected
    }

}