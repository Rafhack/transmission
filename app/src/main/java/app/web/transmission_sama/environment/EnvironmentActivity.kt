package app.web.transmission_sama.environment

import android.graphics.Rect
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.web.transmission_sama.R
import kotlinx.android.synthetic.main.activity_main.*

class EnvironmentActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(EnvironmentViewModel::class.java) }

    private val personDrawer by lazy { PersonDrawer(frmBoundary) }
    private val siteDrawer by lazy { SiteDrawer(frmBoundary) }

    private val boundary by lazy {
        with(frmBoundary) {
            Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupObservables()
        setupTreeObserver()
    }

    private fun setupTreeObserver() {
        frmBoundary.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                frmBoundary.viewTreeObserver.removeOnGlobalLayoutListener(this)
                viewModel.generatePeople(boundary)
                viewModel.generateSites(boundary)
            }
        })
    }

    private fun setupObservables() {
        setupPeopleObservable()
        setupSiteObservable()
    }

    private fun setupSiteObservable() {
        viewModel.siteLiveData.observe(this, Observer { siteDrawer.draw(it) })
    }

    private fun setupPeopleObservable() {
        viewModel.peopleLiveData.observe(this, Observer { personDrawer.draw(it) })
    }

}