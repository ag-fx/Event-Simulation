package test

import Core.Event

class TestEventOne : Event(occurrenceTime = 1.00) {
    override fun execute() = println("TestEventOne")
}

class TestEventTwo : Event(occurrenceTime = 11.00) {
    override fun execute() = println("TestEventTwo")
}

class TestEventThree: Event(occurrenceTime = 11.10) {
    override fun execute() = println("TestEventThree")
}

class TestEventFour : Event(occurrenceTime = 15.10) {
    override fun execute() = println("TestEventFour")
}