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
        myController.run()
        textfield { text = "live ui :)" }
        button("Speed up") { action { myController.speedUp() } }
        button("Pause") { action { myController.pause() } }
        button("Resume") { action { myController.resume() } }
        button("Stop watching  ") { action { myController.stopWatching() } }
        button("Start watching ") { action { myController.startWatching() } }
    }
}
