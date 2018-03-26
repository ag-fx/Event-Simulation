package application.view

import application.controller.MyController
import application.model.AirCarRentalStateModel
import tornadofx.*


class ControlsView : View("Controls") {

    private val controller: MyController by inject()

    override val root = hbox {
        button("Speed up") { action { controller.speedUp() } }
        spacer()
        button("Slow down") { action { controller.slowDown() } }
        spacer()
        button("Pause") { action { controller.pause() } }
        spacer()
        button("Start") { action { controller.start() } }
        spacer()
        button("Resume") { action { controller.resume() } }
        spacer()
        button("Stop watching  ") { action { controller.stopWatching() } }
        spacer()
        button("Start watching ") { action { controller.startWatching() } }
    }
}

class ReplicationView : View("Replikácia") {

    private val controller: MyController by inject()

    override val root = vbox {
        spacer()
        this += ControlsView()
        spacer()
        tableview(controller.currentReplicationState) {
            columnResizePolicy = SmartResize.POLICY

            column("Simulacny cas", AirCarRentalStateModel::currentTime) {
                isSortable = false
            }
            column("Priemerny straveny cas", AirCarRentalStateModel::customersTimeInSystem) {
                isSortable = false
            }
        }
        spacer()

    }
}
