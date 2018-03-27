package application.view

import aircarrental.AirCarRentalState
import application.controller.MyController
import application.model.AirCarRentalStateModel
import application.model.CustomerModel
import application.model.EmployeeModel
import tornadofx.*

class Terminal1View : View("Terminal 1") {
    private val controller: MyController by inject()

    override val root = borderpane {
        top = vbox {

        }
        center = tableview(controller.terminal1ppl) {
            column("Id", CustomerModel::id)
            column("Terminal", CustomerModel::terminal)
            column("Prichod", CustomerModel::arrivedToSystem)
        }
    }
}

class Terminal2View : View("Terminal 2") {
    private val controller: MyController by inject()

    override val root = borderpane {

        center = tableview(controller.terminal2ppl) {
            column("Id", CustomerModel::id)
            column("Terminal", CustomerModel::terminal)
            column("Prichod", CustomerModel::arrivedToSystem)
        }
    }
}

class AirCarRentalView : View("AirCar Rental") {
    private val controller: MyController by inject()

    override val root =  borderpane {
          center =   tableview(controller.aircarppl) {
                column("Id", CustomerModel::id)
                column("Terminal", CustomerModel::terminal)
                column("Prichod", CustomerModel::arrivedToSystem)
            }
           right =  tableview(controller.employees) {
                column("Id", EmployeeModel::id)
                column("Obsluhuje", EmployeeModel::isBusy)
                column("Zakaznik", EmployeeModel::serving)
            }
    }
}
