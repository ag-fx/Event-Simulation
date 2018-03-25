package application.controller

import aircarrental.AirCarConfig
import aircarrental.AirCarRentalSimulation
import javafx.collections.FXCollections.observableArrayList
import javafx.collections.ObservableList
import javafx.scene.chart.XYChart
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.filterIndexed
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import coroutines.JavaFx as onUi
import tornadofx.*

class FixedEmployeeController : Controller() {

    var employees = 20

    val data = observableArrayList<XYChart.Data<Number, Number>>()!!


    fun start() = runAsync {
        (1..30)
            .map { AirCarConfig(numberOfMinibuses = it, numberOfEmployees = employees) }
            .map { AirCarRentalSimulation(it) }
            .onEach {
                it.stopWatching()
                it.log = false
            }.forEach {
                it.start(newSingleThreadContext(it.toString()))
                launch(onUi) {
                    it.afterReplicationChannel
                        .filterIndexed { index, list -> index % 20 == 0 }
                        .consumeEach {
                            //  println("${it.last().config.numberOfMinibuses} ${it.map { it.customersTimeInSystem / 60 }.average()}")
                            data.add(
                                it.last().config.numberOfMinibuses to
                                    it.map { it.customersTimeInSystem / 60.0 }.average()
                            )
                        }
                }
            }
    }
}

fun <A, B> ObservableList<XYChart.Data<Number, Number>>.add(p: Pair<A, B>) = with(p) { add(XYChart.Data(first as Number, second as Number)) }
