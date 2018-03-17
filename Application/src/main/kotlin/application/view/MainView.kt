package application.view

import application.controller.MyController
import tornadofx.*

class MainView : View("Hello TornadoFX") {

    private val myController: MyController by inject()

    override val root = vbox {
        prefWidth = 600.0
        hbox {
            label("Replication")
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
    }
}