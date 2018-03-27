package application.controller

import aircarrental.AirCarConfig
import aircarrental.AirCarRentalSimulation
import application.model.*
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import kotlinx.coroutines.experimental.Job
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

class MyController : SimulationController() {

    private lateinit var testSim: AirCarRentalSimulation
    val threadJobs = mutableListOf<Job>()


    val speedProperty = SimpleDoubleProperty(500.0)
    var speed by speedProperty

    val tickProperty = SimpleDoubleProperty(1.0)
    var tick by tickProperty


    val currentRepProperty = SimpleObjectProperty<AirCarRentalStateModel>(initModel)
    private var currentRep by currentRepProperty


    val terminal1ppl = mutableListOf<CustomerModel>().observable()
    val terminal2ppl = mutableListOf<CustomerModel>().observable()
    val aircarppl = mutableListOf<CustomerModel>().observable()
    val minubuses = mutableListOf<MinibusModel>().observable()
    val employees = mutableListOf<EmployeeModel>().observable()

    private val thread = newSingleThreadContext("AirCarRentalSimulation")

    override fun start() {
        println("start")
        testSim = AirCarRentalSimulation(
            conf = AirCarConfig(numberOfEmployees = maxNumberOfEmployees, numberOfMinibuses = maxNumberOfMinibuses),
            maxSimTime = 60.0 * 60.0 * 24.0 * numberOfDays,
            numberOfReplication = numberOfReplication)

        println("deme")
        println("""
            maxNumberOfEmployees $maxNumberOfEmployees
            maxNumberOfMinibuses $maxNumberOfMinibuses
            numberOfDays $numberOfDays
            numberOfReplication $numberOfReplication
        """.trimIndent())
        testSim.log = false

        val j1 = launch(onUi) {
            testSim.currentReplicationChannel
                .consumeEach {
                    currentRep = AirCarRentalStateModel(it)
                    //  currentReplicationState.add(0, AirCarRentalStateModel(it))
                    terminal1ppl.setAll(it.terminal1Queue.map { CustomerModel(it) })
                    terminal2ppl.setAll(it.terminal2Queue.map { CustomerModel(it) })
                    aircarppl.setAll(it.carRentalQueue.map { CustomerModel(it) })
                    employees.setAll(it.employees.map { EmployeeModel(it) })
                    val sim = it
                    minubuses.setAll(it.minibuses.map { MinibusModel(sim.currentTime, it) })

                }

        }

        val j2 = launch(onUi) {
            testSim.afterReplicationChannel
                .consumeEach {
                    currentRep = AirCarRentalStateModel(it.last())
                }
        }

        val j3 = launch(thread) { testSim.start() }
        threadJobs.addAll(listOf(j1,j2,j3))

    }

    override fun pause() {
        testSim.pause()
    }

    override fun stop() {
        testSim.stop()
      //  threadJobs.forEach { it.cancel() }
      //  threadJobs.clear()
        currentRep = initModel
    }

    override fun resume() {
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


fun <T> Pair<T, T>.map(transform: (T) -> T): Pair<T, T> = transform(first) to transform(second)