package application.controller

import aircarrental.AirCarConfig
import aircarrental.AirCarRentalSimulation
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections.observableArrayList
import javafx.collections.ObservableList
import javafx.scene.chart.XYChart
import kotlinx.coroutines.experimental.ThreadPoolDispatcher
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.filter
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import tornadofx.*
import coroutines.JavaFx as onUi


class VariableMinibusFixedEmployeeController : Controller() {

    val employee = 6

    val graphNameProperty = SimpleStringProperty("Fixný počet pracovníkov : $employee")
    var graphName by graphNameProperty

    val data = observableArrayList<XYChart.Data<Number, Number>>()!!
    val line = (0..30).map { it to 20 }.map { XYChart.Data(it.first as Number, it.second as Number) }.observable()

    private val thread: ThreadPoolDispatcher = newSingleThreadContext("VariableMinibusFixedEmployeeController")
    private val simulations: List<AirCarRentalSimulation> = (3..30)
        .map { AirCarConfig(numberOfMinibuses = it, numberOfEmployees = employee) }
        .map { AirCarRentalSimulation(conf = it, maxSimTime = 60 * 60 * 24.0 * 30, numberOfReplication = 30) }
        .onEach {
            it.stopWatching()
            it.log = false
        }

    fun start() = simulations.asSequence().forEach { sim ->
        launch(thread) { sim.start() }
        launch(onUi) {
            sim.afterReplicationChannel
                .filter { it.size == sim.numberOfReplication - 1 }
                .consumeEach {
                    data.add(sim.conf.numberOfMinibuses to it.map { it.customersTimeInSystem / 60 }.average())
                    println(sim.conf.numberOfMinibuses to it.map { it.customersTimeInSystem / 60 }.average())
                }
        }
    }

    fun stop() = simulations.forEach { it.stop() }

    fun pause() = simulations.forEach { it.pause() }

    fun resume() = simulations.forEach { it.resume() }

}

fun <A, B> ObservableList<XYChart.Data<Number, Number>>.add(p: Pair<A, B>) = with(p) { add(XYChart.Data(first as Number, second as Number)) }