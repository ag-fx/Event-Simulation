package application.controller

import aircarrental.AirCarConfig
import aircarrental.AirCarRentalSimulation
import javafx.collections.FXCollections.observableArrayList
import javafx.collections.ObservableList
import javafx.scene.chart.XYChart
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.filter
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import tornadofx.*
import coroutines.JavaFx as onUi


class VariableMinibusFixedEmployeeController : SimulationController() {

    val data = observableArrayList<XYChart.Data<Number, Number>>()!!
    val line = (0..30)
        .map { it to 20 }
        .map { XYChart.Data(it.first as Number, it.second as Number) }
        .observable()

    private val thread = newSingleThreadContext("VariableEmployeeFixedMinibusController")

    private fun simulations() = (minNumberOfMinibuses..maxNumberOfMinibuses)
        .map { AirCarConfig(numberOfMinibuses = it, numberOfEmployees = maxNumberOfEmployees) }
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
                        data.add(sim.conf.numberOfMinibuses to it.map { it.averageTimeOfCustomerInSystem / 60 }.average())
                    }
            }

        }
    }

    override fun stop()   = simulations.forEach { it.stop() }

    override fun pause()  = simulations.forEach { it.pause() }

    override fun resume() = simulations.forEach { it.resume() }

}

fun <A, B> ObservableList<XYChart.Data<Number, Number>>.add(p: Pair<A, B>) = with(p) { add(XYChart.Data(first as Number, second as Number)) }