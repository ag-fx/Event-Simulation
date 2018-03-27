package application.controller

import aircarrental.AirCarConfig
import aircarrental.AirCarRentalSimulation
import aircarrental.entities.Minibus
import application.model.*
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import tornadofx.*
import coroutines.JavaFx as onUi
import tornadofx.getValue
import tornadofx.setValue

class MyController : Controller() {

    private val testSim = AirCarRentalSimulation(
        conf = AirCarConfig(numberOfEmployees = 20, numberOfMinibuses = 5),
        maxSimTime = 60.0 * 60.0 * 24.0 * 30.0,
        numberOfReplication = 100)

    val textProperty = SimpleStringProperty("1")
    private var text by textProperty

    val simTextProperty = SimpleStringProperty("")
    private var simText by simTextProperty

    val simTimeProperty = SimpleStringProperty("")
    private var simTime by simTimeProperty

    val currentReplicationProperty = SimpleIntegerProperty(0)
    var currentReplication by currentReplicationProperty

    val replicationPercentageProperty = SimpleDoubleProperty(0.0)
    var replicationPercentage by replicationPercentageProperty

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
                    currentReplication = it.replicationNumber
                    replicationPercentage = it.replicationNumber / (testSim.numberOfReplication * 1.0)
                    currentReplicationState.add(0, AirCarRentalStateModel(it))
                    text = "${it.customersTimeInSystem.div(60)}"
                    simTime = "${it.currentTime}"
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
                    currentReplication = it.last().replicationNumber
                    replicationPercentage = it.last().replicationNumber / (testSim.numberOfReplication * 1.0)

                    currentReplicationState.clear()
                    replications.add(0, AirCarRentalStateModel(it.last()))
                    simText = "${it.map { it.customersTimeInSystem / 60 }.average()}"
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