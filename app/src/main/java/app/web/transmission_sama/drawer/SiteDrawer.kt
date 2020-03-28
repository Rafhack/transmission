package app.web.transmission_sama.drawer

import android.view.View
import android.widget.FrameLayout
import app.web.transmission_sama.R
import app.web.transmission_sama.entities.Site
import app.web.transmission_sama.ui.SiteView

class SiteDrawer(private val container: FrameLayout) : Drawer<Site>() {

    override fun draw() {
        getSiteView().apply {
            container.addView(this)
            layoutParams = FrameLayout.LayoutParams(200, 200)
            x = innerSubject.position.first
            y = innerSubject.position.second
        }
    }

    override fun update() {
        container.findViewWithTag<SiteView>(innerSubject.id).apply {
            setupView(this)
        }
    }

    private fun getSiteView(): View = SiteView(container.context).apply {
        tag = innerSubject.id
        setupView(this)
    }

    private fun setupView(siteView: SiteView) = siteView.apply {
        siteColor = if (innerSubject.isInfected) R.color.infected_person else R.color.not_infected
        size = innerSubject.area
    }

}