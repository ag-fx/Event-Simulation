package application.controller

import TestSim.Newsstand.NewsstandSimulation
import application.model.MyModel
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.filter
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import tornadofx.*
import coroutines.JavaFx as onUi

class MyController : Controller() {

    private val myModel = MyModel()
    private val testSim = NewsstandSimulation()
    val textProperty = SimpleStringProperty("1")
    private var text by textProperty

    fun run() = launch(onUi) {
        testSim.start()
                .filter(CommonPool) { it.run % 50000 == 0 }
                .consumeEach {
                   // println(it)
                    text = "${it}"
                }
    }


    fun speedUp() {
        testSim.speed *= 2
    }

    fun pause() {
        testSim.pause()
    }

    fun resume() {
        testSim.resume()
    }

}