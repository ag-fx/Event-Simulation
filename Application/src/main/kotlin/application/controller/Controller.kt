package application.controller

import Core.NewsSim
import Core.Simulation
import TestSim.Newsstand.NewsstandReplication
import application.model.MyModel
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.filter
import kotlinx.coroutines.experimental.channels.filterIndexed
import kotlinx.coroutines.experimental.launch
import tornadofx.*
import coroutines.JavaFx as onUi
import tornadofx.getValue
import tornadofx.setValue

class MyController : Controller() {

    private val myModel = MyModel()
    private val testSim = NewsSim()// NewsstandReplication()
    val textProperty = SimpleStringProperty("1")
    private var text by textProperty

    val simTextProperty = SimpleStringProperty("")
    var simText by simTextProperty


    fun run() = launch(onUi) {
        testSim.start()
                .filterIndexed { i, s -> (s is Simulation.State.ReplicationState && i % 1_0000 == 0) || s is Simulation.State.SimulationState }
                .consumeEach {
                    when (it) {
                        is Simulation.State.ReplicationState -> {
                            text = "${it.state.avgWaitTime}"
                        }
                        is Simulation.State.SimulationState -> {
                            simText = "${it.state.avgWaitTime}"
                        }
                    }
                }
    }


    fun speedUp() {
        // testSim.speed *= 2
    }

    fun pause() {
          testSim.pause()
    }

    fun resume() {
         testSim.resume()
    }

}