package XRandom

import java.util.*

class RandomRange(private val min: Double, private val max: Double, rndSeedNumber: Long) : NextRandom<Double> {

    init {
        if (min > max) throw IllegalArgumentException("max is bigger than min")
    }

    private val rnd = Random(rndSeedNumber)

    override fun next() = (rnd.nextDouble() * (max - min)) + min

}