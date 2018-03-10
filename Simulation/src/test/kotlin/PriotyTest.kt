import Core.Event
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.runBlocking
import test.*

class MyTests : StringSpec() {
    init {
        val testSim = TestSimulation()
        val events = listOf(TestEventFour(), TestEventOne(), TestEventTwo(), TestEventThree())
        events.forEach { testSim.plan(it) }

        with(testSim) {
            "events should be in chronological order" {
                val planned = mutableListOf<Event>()
                events.forEach { planned += poll() }
                planned shouldEqual events.sortedBy { it.occurrenceTime }
            }

            "try simulate"{
                runBlocking {
                    var events = ArrayList<Event>()
                    start().consumeEach {
                        events = it.executedEvents as ArrayList<Event>
                    }
                    events shouldEqual events.sortedBy { it.occurrenceTime }
                }

            }
        }

    }
}