package k2ddt.core

data class ProfilingData(
    val timeStamp: Double,
    var framesPerSecond: Int,
    val activities: MutableList<Pair<String, Double>> = mutableListOf()
)