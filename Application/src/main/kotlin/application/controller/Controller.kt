package application.controller

import aircarrental.AirCarConfig
import aircarrental.AirCarRentalSimulation
import application.model.AirCarRentalStateModel
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import tornadofx.*
import coroutines.JavaFx as onUi

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

    val currentReplicationState = mutableListOf<AirCarRentalStateModel>().observable()
    val replications = mutableListOf<AirCarRentalStateModel>().observable()
    val thread = newSingleThreadContext("AirCarRentalSimulation")

    fun start() {
        testSim.log = false

        launch(onUi) {
            testSim.currentReplicationChannel
                .consumeEach {
                    currentReplicationState.add(0, AirCarRentalStateModel(it))
                    text = "${it.customersTimeInSystem.div(60)}"
                    simTime = "${it.currentTime}"
                }

        }

        launch(onUi) {
            testSim.afterReplicationChannel
                .consumeEach {
                    currentReplicationState.clear()
                    replications.add(0, AirCarRentalStateModel(it.last()))
                    simText = "${it.map { it.customersTimeInSystem / 60 }.average()}"
                }
        }

        async(thread) { testSim.start() }

    }


    fun speedUp() {
        testSim.speed *= 2
    }

    fun slowDown() {
        testSim.speed /= 2
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


}