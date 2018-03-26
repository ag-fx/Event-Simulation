package application.view

import application.controller.MyController
import application.model.AirCarRentalStateModel
import tornadofx.*

class SimulationView : View("Simulácia") {

    private val controller: MyController by inject()

    override val root = vbox {
        spacer()
        this += ControlsView()
        spacer()
        tableview(controller.replications) {
            columnResizePolicy = SmartResize.POLICY

            // column("Simulacny cas", AirCarRentalStateModel::currentTime){
            //     isSortable = false
            //     converter(SecondsToMinutesConverter())
            // }
            column("Priemerny straveny cas", AirCarRentalStateModel::customersTimeInSystem) {
                isSortable = false
                converter(SecondsToMinutesConverter())
            }
            spacer()

        }

    }
}

