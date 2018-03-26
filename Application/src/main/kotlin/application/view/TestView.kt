package application.view

import application.controller.MyController
import javafx.scene.Parent
import javafx.scene.chart.NumberAxis
import tornadofx.*

class TestView : View("TestView") {
    private val myController: MyController by inject()

    override val root = vbox {
        hbox {
            label("Replication")
            spacer()
            label(myController.simTimeProperty)
            spacer()
            textfield(myController.textProperty)
        }
        hbox {
            label("Simulation")
            spacer()
            textfield(myController.simTextProperty)
        }
        textfield { text = "live ui :)" }
        hbox {
            button("Speed up") { action { myController.speedUp() } }
            spacer()
            button("Pause") { action { myController.pause() } }
            spacer()
            button("Resume") { action { myController.resume() } }
            spacer()
            button("Stop watching  ") { action { myController.stopWatching() } }
            spacer()
            button("Start watching ") { action { myController.startWatching() } }
        }
    }
}
