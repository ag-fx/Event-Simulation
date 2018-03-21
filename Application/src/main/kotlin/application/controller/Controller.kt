package application.controller

import TestSim.Newsstand.NewsstandSimulation
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import tornadofx.*
import coroutines.JavaFx as onUi

class MyController : Controller() {

    private val testSim = NewsstandSimulation()
    val textProperty = SimpleStringProperty("1")
    private var text by textProperty

    val simTextProperty = SimpleStringProperty("")
    private var simText by simTextProperty

    val simTimeProperty = SimpleStringProperty("")
    private var simTime by simTimeProperty


    fun run() {

        launch(onUi) {
            testSim.currentReplicationChannel
                    .consumeEach {
                        text = "${it.avgWaitTime}"
                        simTime = "${it.currentTime}"
                    }

        }

        launch(onUi) {
            testSim.afterReplicationChannel
                    .consumeEach {
                        simText = "${it.map { it.avgWaitTime }.average()}"
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