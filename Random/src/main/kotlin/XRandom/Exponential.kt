package XRandom

class ExponentialRandom(private val lambda: Double, seed: Long) : XRandom<Double>(seed) {

    override fun next() = Math.log(1 - uniformRandom.nextDouble()) * (-lambda)

}