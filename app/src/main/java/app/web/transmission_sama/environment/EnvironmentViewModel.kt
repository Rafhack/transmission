package app.web.transmission_sama.environment

import android.graphics.Rect
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.web.transmission_sama.entities.Person
import app.web.transmission_sama.entities.Site
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import kotlin.random.Random

class EnvironmentViewModel : ViewModel() {

    companion object {
        const val DEFAULT_POPULATION = 100
        const val DEFAULT_BUILDING_COUNT = 10
        const val MINIMUM_INFECTION_RATIO = 50
        const val MAXIMUM_INFECTION_RATIO = 100
    }

    private val random by lazy { Random(System.currentTimeMillis()) }

    var peopleLiveData = MutableLiveData<Person>()
    var siteLiveData = MutableLiveData<Site>()

    var peopleList = arrayListOf<Person>()
    var siteList = arrayListOf<Site>()

    fun generatePeople(boundary: Rect, sizeOffset: Int, maxPopulation: Int = DEFAULT_POPULATION) {
        Observable.create<Person> {
            for (i in 0 until maxPopulation) {
                Person().apply {
                    resistanceToInfection = random.nextFloat()
                    infectionRatio = (random.nextInt(
                        MAXIMUM_INFECTION_RATIO - MINIMUM_INFECTION_RATIO
                    ) + MINIMUM_INFECTION_RATIO).toFloat()
                    position = getRandomPositionInBoundary(boundary, sizeOffset)
                    id = UUID.randomUUID().mostSignificantBits
                    peopleList.add(this)
                    it.onNext(this)
                }
            }
            it.onComplete()
        }.subscribeOn(Schedulers.io()).observeOn(mainThread()).subscribe {
            peopleLiveData.value = it
        }
    }

    fun generateSites(boundary: Rect, maxSites: Int = DEFAULT_BUILDING_COUNT) {
        Observable.create<Site> {
            for (i in 0 until maxSites) {
                Site().apply {
                    position = getRandomPositionInBoundary(boundary, 0)
                    area = 10 + random.nextInt() * (10 - 30)
                    id = UUID.randomUUID().mostSignificantBits
                    siteList.add(this)
                    it.onNext(this)
                }
            }
            it.onComplete()
        }.subscribeOn(Schedulers.io()).observeOn(mainThread()).subscribe {
            siteLiveData.value = it
        }
    }

    private fun getRandomPositionInBoundary(boundary: Rect, sizeOffset: Int): Pair<Float, Float> {
        return (-sizeOffset + random.nextInt(
            boundary.right - boundary.left
        )).toFloat() to (-sizeOffset + random.nextInt(
            boundary.bottom - boundary.top
        )).toFloat()
    }

}