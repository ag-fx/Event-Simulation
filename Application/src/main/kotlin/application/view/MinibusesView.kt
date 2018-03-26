package application.view

import application.controller.MyController
import application.model.CustomerModel
import application.model.MinibusModel
import javafx.beans.property.SimpleListProperty
import tornadofx.*

class MinibusesView : View("Minibusy") {
    private val controller: MyController by inject()
    private val selected = SimpleListProperty<CustomerModel>()
    override val root = borderpane {
        top = label("minibus")
        center = tableview(controller.minubuses) {
            column("ID", MinibusModel::id)
            column("Odkial", MinibusModel::source)
            column("Kam", MinibusModel::destination)
            column("Odisiel", MinibusModel::leftAt)
            //column("vzdialenost do ciela", MinibusModel::distanceFromSource)
            column("Je v zastavke", MinibusModel::isInSource)
            onSelectionChange {
                selected.set(it?.seats?.toList()?.map { CustomerModel(it) }?.observable() ?: emptyList<CustomerModel>().observable())
            }
        }
        right = tableview(selected) {
            column("ID", CustomerModel::id)
            column("Terminal", CustomerModel::terminal)
            column("Cas vstupu", CustomerModel::arrivedToSystem)
        }
    }
}


