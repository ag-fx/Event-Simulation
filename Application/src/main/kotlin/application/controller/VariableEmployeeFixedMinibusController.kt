package application.controller

import aircarrental.AirCarConfig
import aircarrental.AirCarRentalSimulation
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections.observableArrayList
import javafx.collections.ObservableList
import javafx.scene.chart.XYChart
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.filter
import kotlinx.coroutines.experimental.channels.filterIndexed
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import coroutines.JavaFx as onUi
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

class VariableEmployeeFixedMinibusController : Controller() {

    val minibus = 6

    val graphNameProperty = SimpleStringProperty("Fixný počet minibusov : $minibus")
    var graphName by graphNameProperty

    val data = observableArrayList<XYChart.Data<Number, Number>>()!!
    val line = (0..30)
        .map { it to 20 }
        .map { XYChart.Data(it.first as Number, it.second as Number) }
        .observable()

    private val thread = newSingleThreadContext("VariableEmployeeFixedMinibusController")

    private val simulations = (10..30)
        .map { AirCarConfig(numberOfMinibuses = minibus, numberOfEmployees = it) }
        .map { AirCarRentalSimulation(conf= it,maxSimTime = 60*60*24.0*30,numberOfReplication = 2) }
        .onEach {
            it.stopWatching()
            it.log = false
        }

    fun start() = simulations.asSequence().forEach { sim ->
        launch(thread) { sim.start() }
        val rep = sim.afterReplicationChannel
        launch(onUi) {
            rep
                .filter{it.size == sim.numberOfReplication- 1}
                .consumeEach {
                    data.add(sim.conf.numberOfEmployees to it.map { it.customersTimeInSystem / 60 }.average())
                }
        }

    }

    fun stop() = simulations.forEach { it.stop() }

    fun pause() = simulations.forEach { it.pause() }

    fun resume() = simulations.forEach { it.resume() }

}