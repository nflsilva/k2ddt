package k2ddt.core

data class ProfilingData(
    val timeStamp: Double,
    val framesPerSecond: Int,
    val activities: MutableList<Pair<String, Double>> = mutableListOf()
)