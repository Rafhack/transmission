package app.web.transmission_sama.environment

import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.web.transmission_sama.entities.Person
import app.web.transmission_sama.entities.Site
import app.web.transmission_sama.nextIntBetween
import app.web.transmission_sama.postDelayed
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import kotlin.math.abs
import kotlin.random.Random

class EnvironmentViewModel : ViewModel() {

    companion object {
        const val DEFAULT_POPULATION = 100
        const val DEFAULT_BUILDING_COUNT = 10
        const val MIN_INFECTION_RATIO = 50
        const val MAX_INFECTION_RATIO = 100
        const val MOVEMENT_INTERVAL = 50L
        const val MIN_REPEAT_STEPS = 20
        const val MAX_REPEAT_STEPS = 120
        const val DEFAULT_STEP_SIZE = 3f
    }

    private val random by lazy { Random(System.currentTimeMillis()) }

    private val stepMap = mutableMapOf<Long, MutableList<Pair<Boolean, Boolean>>>()

    var peopleLiveData = MutableLiveData<Person>()
    var movementLiveData = MutableLiveData<Person>()
    var nearbyLiveData = MutableLiveData<List<Person>>()
    var siteLiveData = MutableLiveData<Site>()

    var peopleList = arrayListOf<Person>()
    var siteList = arrayListOf<Site>()

    fun generatePeople(boundary: Rect, sizeOffset: Int, maxPopulation: Int = DEFAULT_POPULATION) {
        Observable.create<Person> {
            for (i in 0 until maxPopulation) {
                Person().apply {
                    resistanceToInfection = random.nextFloat()
                    infectionRatio = (random.nextIntBetween(
                        MAX_INFECTION_RATIO, MIN_INFECTION_RATIO
                    )).toFloat()
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

    fun getNearbyPeople(person: Person) {
        Observable.create<List<Person>> { emitter ->
            emitter.onNext(peopleList.filter { near ->
                near != person && abs(near.position.first - person.position.first) <= person.infectionRatio &&
                        abs(near.position.second - person.position.second) <= person.infectionRatio
            })
        }.subscribeOn(Schedulers.io()).observeOn(mainThread()).subscribe { nearbyLiveData.value = it }
    }

    fun movePeople(sizeOffset: Int, boundary: Rect, stepSize: Float = DEFAULT_STEP_SIZE) {
        Observable.create<Person> { emitter ->
            peopleList.forEach { person ->
                Handler(Looper.getMainLooper()).postDelayed(MOVEMENT_INTERVAL) {
                    val personSteps = stepMap[person.id] ?: mutableListOf()

                    if (personSteps.isEmpty()) {
                        val direction = random.nextBoolean() to random.nextBoolean()
                        val stepsCount = random.nextIntBetween(MAX_REPEAT_STEPS, MIN_REPEAT_STEPS)
                        for (i in 0 until stepsCount) personSteps.add(direction)
                        stepMap[person.id] = personSteps
                        person.position = movePerson(person, personSteps, sizeOffset, boundary, stepSize)
                    } else {
                        person.position = movePerson(person, personSteps, sizeOffset, boundary, stepSize)
                        personSteps.removeAt(0)
                        stepMap[person.id] = personSteps
                    }

                    emitter.onNext(person)
                    postDelayed(it, MOVEMENT_INTERVAL)
                }
            }
        }.subscribeOn(Schedulers.io()).observeOn(mainThread()).subscribe {
            movementLiveData.value = it
        }
    }

    private fun movePerson(
        person: Person, personSteps: MutableList<Pair<Boolean, Boolean>>, sizeOffset: Int, boundary: Rect, stepSize: Float
    ): Pair<Float, Float> {
        val newPosition = person.position.first.plus(if (personSteps[0].first) stepSize else -stepSize) to
                person.position.second.plus(if (personSteps[0].second) stepSize else -stepSize)
        return if (newPosition.first + sizeOffset >= 0 && newPosition.first + sizeOffset <= boundary.right &&
            newPosition.second + sizeOffset >= 0 && newPosition.second + sizeOffset <= boundary.bottom
        ) {
            newPosition
        } else {
            for (i in personSteps.indices) personSteps[i] = !personSteps[i].first to !personSteps[i].second
            stepMap[person.id] = personSteps
            person.position
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