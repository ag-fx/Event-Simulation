import XRandom.RandomRange
import io.kotlintest.matchers.fail
import io.kotlintest.specs.StringSpec
import java.util.*

class RandomRangeTes : StringSpec() {
    init {

        val r = Random()
        repeat(100) {
            val min = Math.abs(r.nextInt() * 1.0)
            val max = Math.abs(min + Math.abs( r.nextInt() * 1.0))
            "$min $max" {
                val range = RandomRange(min, max, r.nextLong())
                repeat(10000) {}
                val g = range.next()
                if (g > max || g < min) {
                    println("Gen $g max $max min $min")
                    fail("Gen $g max $max min $min")
                }
            }
        }
    }

}
