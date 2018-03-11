package application.controller

import TestSim.MyTestEventOne
import TestSim.MyTestSimulation
import application.model.MyModel
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import tornadofx.*
import coroutines.JavaFx as onUi

class MyController : Controller() {

    private val myModel = MyModel()
    private  val testSim = MyTestSimulation()
    val textProperty = SimpleStringProperty("1")
    private var text by textProperty

    fun run() {
        launch(onUi) {
          //  testSim.plan(MyTestEventOne())
            testSim.start()
                    .consumeEach {
                        text = "${it.time}"
                    }

        }

    }

    fun speedUp() {
        testSim.speed *=2
    }

    fun pause(){
        testSim.pause()
    }

    fun resume(){
        testSim.resume()
    }
}