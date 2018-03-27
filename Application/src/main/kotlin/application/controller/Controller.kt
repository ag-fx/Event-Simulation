package application.controller

import aircarrental.AirCarConfig
import aircarrental.AirCarRentalSimulation
import application.model.*
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import tornadofx.*
import coroutines.JavaFx as onUi
import tornadofx.getValue
import tornadofx.setValue
import kotlin.math.pow
import kotlin.math.sqrt

class MyController : Controller() {

    private val testSim = AirCarRentalSimulation(
        conf = AirCarConfig(numberOfEmployees = 8, numberOfMinibuses = 5),
        maxSimTime = 60.0 * 60.0 * 24.0 * 30.0,
        numberOfReplication = 100)

    val speedProperty = SimpleDoubleProperty(500.0)
    var speed by speedProperty

    val tickProperty = SimpleDoubleProperty(1.0)
    var tick by tickProperty


    val currentRepProperty = SimpleObjectProperty<AirCarRentalStateModel>(initModel)
    var currentRep by currentRepProperty

    val currentReplicationState = mutableListOf<AirCarRentalStateModel>().observable()

    val terminal1ppl = mutableListOf<CustomerModel>().observable()
    val terminal2ppl = mutableListOf<CustomerModel>().observable()
    val aircarppl = mutableListOf<CustomerModel>().observable()
    val minubuses = mutableListOf<MinibusModel>().observable()
    val employees = mutableListOf<EmployeeModel>().observable()

    val replications = mutableListOf<AirCarRentalStateModel>().observable()
    private val thread = newSingleThreadContext("AirCarRentalSimulation")

    fun start() {
        testSim.log = false

        launch(onUi) {
            testSim.currentReplicationChannel
                .consumeEach {
                    currentRep = AirCarRentalStateModel(it)
                    currentReplicationState.add(0, AirCarRentalStateModel(it))
                    terminal1ppl.setAll(it.terminal1Queue.map { CustomerModel(it) })
                    terminal2ppl.setAll(it.terminal2Queue.map { CustomerModel(it) })
                    aircarppl.setAll(it.carRentalQueue.map { CustomerModel(it) })
                    employees.setAll(it.employees.map { EmployeeModel(it) })
                    val sim = it
                    minubuses.setAll(it.minibuses.map { MinibusModel(sim.currentTime, it) })

                }

        }

        launch(onUi) {
            testSim.afterReplicationChannel
                .consumeEach {
                    currentRep = AirCarRentalStateModel(it.last())
                    currentReplicationState.clear()
                    replications.add(0, AirCarRentalStateModel(it.last()))
                    val avg = it.map { it.averageTimeOfCustomerInSystem/60 }.average()
                    val nieco2 = it.map { (it.averageTimeOfCustomerInSystem - avg).pow(2) }.average()
                    val left = avg - (1.645* sqrt(nieco2))/sqrt(it.size-1.0)
                    val right = avg + (1.645* sqrt(nieco2))/sqrt(it.size-1.0)
                    println((left to right).map { it/60 })
                }
        }

        async(thread) { testSim.start() }

    }

    fun pause() {
        testSim.pause()
    }

    fun resume() {
        testSim.resume()
    }

    fun startWatching() {
        testSim.startWatching()
    }

    fun stopWatching() {
        testSim.stopWatching()
    }

    fun updateSpeed() {
        testSim.sleepTime = speed.toLong()
        tick = 5.0
        updateTick()
    }

    fun updateTick() {
        testSim.oneTick = tick
    }


}



fun <T> Pair<T, T>.map(transform: (T) -> T): Pair<T,T> = transform(first) to transform(second)