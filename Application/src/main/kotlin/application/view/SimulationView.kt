package application.view

import application.controller.MyController
import application.controller.map
import application.view.converter.XConverter
import application.view.converter.decimalFormat
import javafx.geometry.Insets
import org.nield.kotlinstatistics.geometricMean
import tornadofx.*

class SimulationView : View("Simulácia") {

    private val controller: MyController by inject()

    override val root = borderpane {
        top = vbox { this += ControlsView() }
        left = vbox {
            padding = Insets(24.0)
            minWidth = 300.0
            hbox {
                label("Priemerny cas v systeme")
                spacer()
                label(controller.currentRepProperty, converter = XConverter {
                    it?.statistics?.avgTimeInSystem?.average()?.let { decimalFormat.format(it / 60.0) }?.toString()
                        ?: "0.0"
                })
            }
            separator()
            label("90% Interval spolahlivosti")

            label(controller.currentRepProperty, converter = XConverter {
                it?.statistics?.interval?.map { it / 60.0 }?.toString() ?: "0"
            })
            hbox {
                label("Replikacia")
                spacer()
                label(controller.currentRepProperty, converter = XConverter {
                    it?.replicationNumber?.toString() ?: "err"
                })
            }
            separator()
            hbox {
                label("Priemerny cas v rade Terminal 1")
                spacer()
                label(controller.currentRepProperty, converter = XConverter {
                    it?.statistics?.avgWaitTimeTerminal1?.average()?.let { decimalFormat.format(it / 60.0) }?.toString()
                        ?: "0.0"
                })
            }
            hbox {
                label("Priemerna dlzka radu Terminal 1")
                spacer()
                label(controller.currentRepProperty, converter = XConverter {
                    it?.statistics?.avgQueueSizeTerminal1?.average()?.let { decimalFormat.format(it) }?.toString()
                        ?: "0.0"
                })
            }
            separator()
            hbox {
                label("Priemerny cas v rade Terminal 2")
                spacer()
                label(controller.currentRepProperty, converter = XConverter {
                    it?.statistics?.avgWaitTimeTerminal2?.average()?.let { decimalFormat.format(it / 60.0) }?.toString()
                        ?: "0.0"
                })
            }
            hbox {
                label("Priemerna dlzka radu Terminal 2")
                spacer()
                label(controller.currentRepProperty, converter = XConverter {
                    it?.statistics?.avgQueueSizeTerminal2?.average()?.let { decimalFormat.format(it) }?.toString()
                        ?: "0.0"
                })
            }
            separator()
            hbox {
                label("Priemerny cas v rade AirCarRental")
                spacer()
                label(controller.currentRepProperty, converter = XConverter {
                    it?.statistics?.avgWaitTimeAirCarRental?.average()?.let { decimalFormat.format(it / 60.0) }?.toString()
                        ?: "0.0"
                })
            }
            hbox {
                label("Priemerna dlzka radu AirCarRental")
                spacer()
                label(controller.currentRepProperty, converter = XConverter {
                    it?.statistics?.avgQueueSizeAirCarRental?.average()?.let { decimalFormat.format(it) }?.toString()
                        ?: "0.0"
                })
            }
        }
        right = vbox {
            listOf(
                "Minibus" to controller.maxNumberOfMinibusesProperty,
                "Pracovnici" to controller.maxNumberOfEmployeesProperty,
                "Replikacie" to controller.numberOfReplicationProperty,
                "Dni" to controller.numberOfDaysProperty
            ).forEach {
                val l = if(it.first.contentEquals("Replikacie")) listOf(1,10,50,100,200,300,500,1000,5000,10000).toList() else (1..30).toList()
                hbox {
                    label(it.first)
                    spacer()
                    choicebox(values = l) {
                        valueProperty().bindBidirectional(it.second)
                    }
                }
            }

        }
    }
}

