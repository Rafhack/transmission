package app.web.transmission_sama.environment

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.web.transmission_sama.R
import app.web.transmission_sama.entities.Person
import app.web.transmission_sama.postDelayed
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.math.abs

class EnvironmentActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(EnvironmentViewModel::class.java) }

    private val random by lazy { Random() }

    private val boundary by lazy {
        with(frmBoundary) {
            Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
        }
    }

    private val stepMap = mutableMapOf<Long, MutableList<Pair<Boolean, Boolean>>>()

    private val sizeOffset by lazy { resources.getDimensionPixelSize(R.dimen.person_view) / 2 }

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupObservables()
        setupTreeObserver()

        frmBoundary.setOnClickListener {
            viewModel.peopleList.forEach { person ->
                Handler().postDelayed(50) {

                    val personSteps = stepMap[person.id] ?: mutableListOf()

                    if (personSteps.isEmpty()) {
                        val direction = random.nextBoolean() to random.nextBoolean()
                        val stepsCount = random.nextInt(120 - 50) + 50
                        for (i in 0 until stepsCount) personSteps.add(direction)
                        stepMap[person.id] = personSteps
                        movePerson(person, personSteps)
                    } else {
                        movePerson(person, personSteps)
                        personSteps.removeAt(0)
                        stepMap[person.id] = personSteps
                    }

                    person.drawer.update()
                    postDelayed(it, 50)
                }
            }

            val person = viewModel.peopleList[0]
            startInfection(person)
        }
    }

    private fun movePerson(person: Person, personSteps: MutableList<Pair<Boolean, Boolean>>) {
        val newPosition = person.position.first.plus(if (personSteps[0].first) 3f else -3f) to
                person.position.second.plus(if (personSteps[0].second) 3f else -3f)
        if (newPosition.first + sizeOffset >= 0 && newPosition.first + sizeOffset <= frmBoundary.width &&
            newPosition.second + sizeOffset >= 0 && newPosition.second + sizeOffset <= frmBoundary.height
        ) {
            person.position = newPosition
        } else {
            for (i in personSteps.indices) personSteps[i] = !personSteps[i].first to !personSteps[i].second
            stepMap[person.id] = personSteps
        }
    }

    private fun startInfection(person: Person) {
        person.isInfected = true

        Handler().postDelayed(5000) {
            val nearby = viewModel.peopleList.filter { near ->
                near != person && abs(near.position.first - person.position.first) <= person.infectionRatio &&
                        abs(near.position.second - person.position.second) <= person.infectionRatio
            }
            nearby.forEach { near ->
                if (!near.isInfected) Handler().postDelayed(
                    { startInfection(near) },
                    (random.nextInt(1000 - 500) + 500L)
                )
            }
            postDelayed(it, 5000)
        }
    }

    private fun setupTreeObserver() {
        frmBoundary.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                frmBoundary.viewTreeObserver.removeOnGlobalLayoutListener(this)
                viewModel.generatePeople(boundary, sizeOffset, 100)
                //         viewModel.generateSites(boundary, 3)
            }
        })
    }

    private fun setupObservables() {
        setupPeopleObservable()
        setupSiteObservable()
    }

    private fun setupSiteObservable() {
        viewModel.siteLiveData.observe(this, Observer {
            it.attachDrawer(SiteDrawer(frmBoundary))
            it.drawer.draw()
        })
    }

    private fun setupPeopleObservable() {
        viewModel.peopleLiveData.observe(this, Observer {
            it.attachDrawer(PersonDrawer(frmBoundary))
            it.drawer.draw()
        })
    }

}
