package application.view

import application.controller.MyController
import application.model.CustomerModel
import application.model.EmployeeModel
import tornadofx.*

class Terminal1View : View("Terminal 1") {
    private val controller: MyController by inject()

    override val root = borderpane {

        left = tableview(controller.terminal1ppl) {
            smartResize()
            column("Id", CustomerModel::id).apply{ isSortable = false}
            column("Terminal", CustomerModel::terminal).apply{ isSortable = false}
            column("Prichod", CustomerModel::arrivedToSystem).apply{ isSortable = false}
        }
    }
}

class Terminal2View : View("Terminal 2") {
    private val controller: MyController by inject()

    override val root = borderpane {

        left = tableview(controller.terminal2ppl) {
            smartResize()
            column("Id", CustomerModel::id).apply{ isSortable = false}
            column("Terminal", CustomerModel::terminal).apply{ isSortable = false}
            column("Prichod", CustomerModel::arrivedToSystem).apply{ isSortable = false}
        }
    }
}

class AirCarRentalView : View("AirCar Rental") {
    private val controller: MyController by inject()

    override val root = borderpane {
        center = tableview(controller.aircarppl) {
            smartResize()

            column("Id", CustomerModel::id).apply{ isSortable = false}
            column("Terminal", CustomerModel::terminal).apply{ isSortable = false}
            column("Prichod", CustomerModel::arrivedToSystem).apply{ isSortable = false}
        }
        right = tableview(controller.employees) {
            smartResize()

            column("Id", EmployeeModel::id).apply{ isSortable = false}
            column("Obsluhuje", EmployeeModel::isBusy).apply{ isSortable = false}
            column("Zakaznik", EmployeeModel::serving).apply{ isSortable = false}
        }
    }
}
