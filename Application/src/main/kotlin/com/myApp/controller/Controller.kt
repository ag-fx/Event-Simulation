package com.myApp.controller

import com.myApp.model.MyModel
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.experimental.channels.*
import kotlinx.coroutines.experimental.launch
import coroutines.JavaFx as onUi
import tornadofx.*

class MyController : Controller() {
    private val myModel = MyModel()

    val textProperty = SimpleStringProperty("1")
    private var text by textProperty

    fun run() {
        launch(onUi) {
            myModel
                    .hello
                    .filter { it % 1000 == 0 }
                    .consumeEach {
                        text = "$it"
                    }
        }

    }
}