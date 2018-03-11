package application.view

import application.controller.MyController
import tornadofx.*

class MainView : View("Hello TornadoFX") {

    private val myController: MyController by inject()

    override val root = vbox {
        label(myController.textProperty)
        myController.run()
        textfield { text = "live ui :)" }
        button("Speed up") { action { myController.speedUp() } }
        button("Pause") { action { myController.pause() } }
        button("Resume") { action { myController.resume() } }
    }
}