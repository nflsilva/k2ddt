package k2ddt.tools

import k2ddt.core.ProfilingData
import org.lwjgl.glfw.GLFW.glfwGetTime

class Profiler {

    private val activities: MutableMap<String, Double> = mutableMapOf()
    var framesPerSecond: Int = 0

    fun start(name: String) {
        activities[name] = getTime()
    }

    fun end(name: String) {
        activities[name]?.let {
            activities[name] = getTime() - it
        }
    }

    fun getData(): ProfilingData {
        val data = mutableListOf<Pair<String, Double>>()
        for (k in activities.keys) {
            data.add(Pair(k, activities[k]!!))
        }

        return ProfilingData(
            getTime(),
            framesPerSecond,
            data
        )
    }

    private fun getTime(): Double {
        return glfwGetTime()
    }
}