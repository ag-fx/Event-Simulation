package application.view

import application.controller.MyController
import application.model.AirCarRentalStateModel
import tornadofx.*

class ReplicationView : View("Replikácia") {

    private val controller: MyController by inject()

    override val root = vbox {

        hbox {
            button("Start") { action { TODO() } }
            button("Pause") { action { TODO() } }
            button("Resume") { action { TODO() } }
            button("Speed up") { action { TODO() } }
            button("Slow  down") { action { TODO() } }
        }
        tableview(controller.currentReplicationState) {
            columnResizePolicy = SmartResize.POLICY

            column("Simulacny cas", AirCarRentalStateModel::currentTime){
                isSortable = false
            }
            column("Priemerny straveny cas", AirCarRentalStateModel::customersTimeInSystem){
                isSortable = false
            }
        }

    }
}
