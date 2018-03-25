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

class FixedMinibusController : Controller() {

    var minibus = 20

    val data = observableArrayList<XYChart.Data<Number, Number>>()!!

    fun start() = runAsync {
        (1..30)
            .map { AirCarConfig(numberOfMinibuses = minibus, numberOfEmployees = it) }
            .map { AirCarRentalSimulation(it) }
            .onEach {
                it.stopWatching()
                it.log = false
            }.forEach {
                it.start()
                launch(onUi) {
                    it.afterReplicationChannel
//                        .filterIndexed { index, list -> index % 100 == 0 }
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

