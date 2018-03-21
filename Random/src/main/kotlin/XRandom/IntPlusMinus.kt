package XRandom

import java.util.*

class IntPlusMinus(private val fixedNumber: Int, private val variableNumber: Int, rndSeedNumber: Long, rndSeedPlusMinus: Long) : NextRandom<Int> {

    private val rnd = Random(rndSeedNumber)
    private val rndPlusMinus = Random(rndSeedPlusMinus)

    override fun next(): Int {
        val plusMinus = rnd.nextInt(variableNumber + 1)
        return if (rndPlusMinus.nextBoolean())
            fixedNumber + plusMinus
        else
            fixedNumber - plusMinus
    }


}