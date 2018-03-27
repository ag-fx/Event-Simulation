package application.view

import application.controller.MyController
import application.model.AirCarRentalStateModel
import tornadofx.*

class SimulationView : View("Simulácia") {

    private val controller: MyController by inject()

    override val root = borderpane {
        top = vbox { this += ControlsView() }
        center = tableview(controller.replications) {
            columnResizePolicy = SmartResize.POLICY

            column("Priemerny straveny cas", AirCarRentalStateModel::customersTimeInSystem) {
                isSortable = false
                converter(SecondsToMinutesConverter())
            }

            column("daco", AirCarRentalStateModel::avgQueueSizeTerminalOne)
            column("daco", AirCarRentalStateModel::avgQueueWaitTimeTerminalOne)

        }
    }
}

