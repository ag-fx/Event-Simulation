package application.controller

import TestSim.Newsstand.NewsstandSimulation
import application.model.MyModel
import com.github.thomasnield.rxkotlinfx.observeOnFx
import com.github.thomasnield.rxkotlinfx.subscribeOnFx
import io.reactivex.schedulers.Schedulers
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import coroutines.JavaFx as onUi

class MyController : Controller() {

    private val myModel = MyModel()
    private val testSim = NewsstandSimulation()// NewsstandReplication()
    val textProperty = SimpleStringProperty("1")
    private var text by textProperty

    val simTextProperty = SimpleStringProperty("")
    var simText by simTextProperty


    fun run() = runAsync {
        testSim.simulation()
                .observeOn(Schedulers.io())
                .filter { it.run % 5_000 == 0 }
                .subscribeOnFx()
                .observeOnFx()
                .subscribe {
                    println(it)
                    text = "${it.avgWaitTime}"
                }

        testSim.start()
//        testSim.start()
//                .filterIndexed { i, s -> (s is Simulation.State.ReplicationState && i % 1_0000 == 0) || s is Simulation.State.SimulationState }
//                .consumeEach {
//                    when (it) {
//                        is Simulation.State.ReplicationState -> {
//                            text = "${it.state.avgWaitTime}"
//                        }
//                        is Simulation.State.SimulationState -> {
//                            simText = "${it.state.avgWaitTime}"
//                        }
//                    }
//                }
    }


    fun speedUp() {
        // testSim.speed *= 2
    }

    fun pause() {
//          testSim.pause()
    }

    fun resume() {
//         testSim.resume()
    }

}