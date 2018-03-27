package application.model

import aircarrental.entities.Employee
import javafx.beans.property.SimpleStringProperty

class EmployeeModel(employee: Employee) : tornadofx.ItemViewModel<Employee>(employee) {
    val id = bind(Employee::id)
    val isBusy = bind(Employee::isBusy)
    val serving = SimpleStringProperty(employee.serving?.let { "${it.id} z ${it.terminal}"}  ?: "")
}