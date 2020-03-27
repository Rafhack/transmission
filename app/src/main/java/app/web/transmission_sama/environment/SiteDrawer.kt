package app.web.transmission_sama.environment

import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import app.web.transmission_sama.entities.Site
import app.web.transmission_sama.ui.SiteView

class SiteDrawer(private val container: FrameLayout) : Drawer<Site>() {

    override fun draw(subject: Site) {
        getSiteView().apply {
            container.addView(this)
            layoutParams = FrameLayout.LayoutParams(100, 200)
            x = subject.position.first
            y = subject.position.second
        }
    }

    private fun getSiteView(): View = SiteView(container.context).apply {
        siteColor = Color.RED
    }

}