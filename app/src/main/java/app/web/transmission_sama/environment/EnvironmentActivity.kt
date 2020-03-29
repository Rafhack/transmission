package app.web.transmission_sama.environment

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.web.transmission_sama.R
import app.web.transmission_sama.drawer.PersonDrawer
import app.web.transmission_sama.drawer.SiteDrawer
import app.web.transmission_sama.entities.Person
import app.web.transmission_sama.entities.Virus
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

    private val infection by lazy {
        Virus { infectable ->
            viewModel.getNearbyPeople(infectable as Person)
        }
    }

    private val sizeOffset by lazy { resources.getDimensionPixelSize(R.dimen.person_view) / 2 }

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupObservers()
        setupTreeObserver()

        frmBoundary.setOnClickListener {

            val patientZero = viewModel.peopleList[0]
            infection.infect(patientZero)
            viewModel.movePeople(sizeOffset, boundary)

            //    startInfection(patientZero)
        }
    }

    private fun setupObservers() {
        setupPeopleObserver()
        setupSiteObserver()
        setupMovementObserver()
        setupNearbyObserver()
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

    private fun setupMovementObserver() {
        viewModel.movementLiveData.observe(this, Observer { it.drawer.update() })
    }

    private fun setupSiteObserver() {
        viewModel.siteLiveData.observe(this, Observer {
            it.attachDrawer(SiteDrawer(frmBoundary))
            it.drawer.draw()
        })
    }

    private fun setupPeopleObserver() {
        viewModel.peopleLiveData.observe(this, Observer {
            it.attachDrawer(PersonDrawer(frmBoundary))
            it.drawer.draw()
        })
    }

    private fun setupNearbyObserver() {
        viewModel.nearbyLiveData.observe(this, Observer { nearby ->
            nearby.forEach { infection.infect(it) }
        })
    }

}
