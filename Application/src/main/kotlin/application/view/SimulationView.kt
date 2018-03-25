package application.view

import application.controller.MyController
import application.model.AirCarRentalStateModel
import tornadofx.*

class SimulationView : View("Simulácia") {

    private val controller: MyController by inject()

    override val root = vbox {

        hbox {
            button("Start") { action { TODO() } }
            button("Pause") { action { TODO() } }
            button("Resume") { action { TODO() } }
            button("Speed up") { action { TODO() } }
            button("Slow  down") { action { TODO() } }
        }
        tableview(controller.replications) {
            columnResizePolicy = SmartResize.POLICY

           // column("Simulacny cas", AirCarRentalStateModel::currentTime){
           //     isSortable = false
           //     converter(SecondsToMinutesConverter())
           // }
            column("Priemerny straveny cas", AirCarRentalStateModel::customersTimeInSystem){
                isSortable = false
                converter(SecondsToMinutesConverter())
            }
        }

    }
}

