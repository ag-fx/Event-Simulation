package application.controller

import aircarrental.AirCarConfig
import aircarrental.AirCarRentalSimulation
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections.observableArrayList
import javafx.scene.chart.XYChart
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.filter
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import coroutines.JavaFx as onUi
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

abstract class SimulationController : Controller() {

    val minNumberOfMinibusesProperty = SimpleObjectProperty(3)
    var minNumberOfMinibuses by minNumberOfMinibusesProperty

    val maxNumberOfMinibusesProperty = SimpleObjectProperty(6)
    var maxNumberOfMinibuses by maxNumberOfMinibusesProperty

    val minNumberOfEmployeesProperty = SimpleObjectProperty(10)
    var minNumberOfEmployees by minNumberOfEmployeesProperty

    val maxNumberOfEmployeesProperty = SimpleObjectProperty(19)
    var maxNumberOfEmployees by maxNumberOfEmployeesProperty

    val numberOfDaysProperty = SimpleObjectProperty(7)
    var numberOfDays by numberOfDaysProperty

    val numberOfReplicationProperty = SimpleObjectProperty(10)
    var numberOfReplication by numberOfReplicationProperty

    abstract fun start()
    abstract fun stop()
    abstract fun pause()
    abstract fun resume()
}

class VariableEmployeeFixedMinibusController : SimulationController() {


    val data = observableArrayList<XYChart.Data<Number, Number>>()!!
    val line = (0..30)
        .map { it to 20 }
        .map { XYChart.Data(it.first as Number, it.second as Number) }
        .observable()

    private val thread = newSingleThreadContext("VariableEmployeeFixedMinibusController")

    private fun simulations() = (minNumberOfEmployees..maxNumberOfEmployees)
        .map { AirCarConfig(numberOfMinibuses = minNumberOfMinibuses, numberOfEmployees = it) }
        .map { AirCarRentalSimulation(conf = it, maxSimTime = 60.0 * 60 * 24 * numberOfDays, numberOfReplication = numberOfReplication) }
        .onEach {
            it.stopWatching()
            it.log = false
        }

    private lateinit var simulations: List<AirCarRentalSimulation>

    override fun start() {
        simulations = simulations()
        simulations.asSequence().forEach { sim ->
            launch(thread) { sim.start() }
            val rep = sim.afterReplicationChannel
            launch(onUi) {
                rep
                    .filter { it.size == sim.numberOfReplication - 1 }
                    .consumeEach {
                        data.add(sim.conf.numberOfEmployees to it.map { it.averageTimeOfCustomerInSystem / 60 }.average())
                    }
            }

        }
    }

    override fun stop() = simulations.forEach { it.stop() }

    override fun pause() = simulations.forEach { it.pause() }

    override fun resume() = simulations.forEach { it.resume() }

}