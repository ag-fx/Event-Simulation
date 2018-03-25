package application.controller

import aircarrental.AirCarConfig
import aircarrental.AirCarRentalSimulation
import application.model.AirCarRentalStateModel
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import tornadofx.*
import coroutines.JavaFx as onUi

class MyController : Controller() {

    private val testSim = AirCarRentalSimulation(
        conf = AirCarConfig(numberOfEmployees = 8, numberOfMinibuses = 5),
        maxSimTime = 60.0 * 60.0 * 24.0 * 30,
        numberOfReplication = 100)

    val textProperty = SimpleStringProperty("1")
    private var text by textProperty

    val simTextProperty = SimpleStringProperty("")
    private var simText by simTextProperty

    val simTimeProperty = SimpleStringProperty("")
    private var simTime by simTimeProperty

    val currentReplicationState = mutableListOf<AirCarRentalStateModel>().observable()
    val replications = mutableListOf<AirCarRentalStateModel>().observable()

    fun run() {
        testSim.log = false
       // testSim.stopWatching()
//        AirCarConfig(numberOfEmployees = 1, numberOfMinibuses = 1) to 16595.1310
//        val sim = AirCarRentalSimulation(configuration.first, 60 * 60.0 * 24 * 30 , repcount)
        launch(onUi) {
            testSim.currentReplicationChannel
                .consumeEach {
                    currentReplicationState.add(0,AirCarRentalStateModel(it))
                    text = "${it.customersTimeInSystem.div(60)}"
                    simTime = "${it.currentTime}"
                }

        }

        launch(onUi) {
            testSim.afterReplicationChannel
                .consumeEach {
                    currentReplicationState.clear()
                    replications.add(0,AirCarRentalStateModel(it.last()))
                    simText = "${it.map { it.customersTimeInSystem / 60 }.average()}"
                }
        }

        testSim.start()


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