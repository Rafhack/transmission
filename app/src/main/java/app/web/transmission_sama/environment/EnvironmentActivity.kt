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

    private val sizeOffset by lazy { resources.getDimensionPixelSize(R.dimen.person_view) / 2 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupObservables()
        setupTreeObserver()

        frmBoundary.setOnClickListener {
            viewModel.peopleList.forEach { person ->
                Handler().postDelayed(50) {
                    val newPosition = person.position.first.plus(if (random.nextBoolean()) 7f else -7f) to
                            person.position.second.plus(if (random.nextBoolean()) 7f else -7f)

                    if (newPosition.first + sizeOffset >= 0 && newPosition.first + sizeOffset <= frmBoundary.width &&
                        newPosition.second + sizeOffset >= 0 && newPosition.second + sizeOffset <= frmBoundary.height
                    ) person.position = newPosition
                    person.drawer.update()
                    postDelayed(it, 50)
                }
            }

            val person = viewModel.peopleList[0]
            startInfection(person)
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
                if (!near.isInfected) startInfection(near)
            }
            postDelayed(it, 5000)
        }
    }

    private fun setupTreeObserver() {
        frmBoundary.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                frmBoundary.viewTreeObserver.removeOnGlobalLayoutListener(this)
                viewModel.generatePeople(boundary, sizeOffset, 80)
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
