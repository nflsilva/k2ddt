package k2ddt.ui.input

import org.lwjgl.glfw.GLFW.*

class Keyboard(window: Long) {

    val pressedKeys: MutableSet<Int> = mutableSetOf()
    val holdKeys: MutableSet<Int> = mutableSetOf()

    init {
        glfwSetKeyCallback(window) { _: Long, key: Int, _: Int, action: Int, _: Int ->
            onKeyChange(key, action)
        }
    }

    fun onUpdate() {
        for(k in pressedKeys) {
            holdKeys.add(k)
        }
        pressedKeys.clear()
    }

    private fun onKeyChange(key: Int, action: Int) {
        when (action) {
            GLFW_PRESS -> {
                pressedKeys.add(key)
            }
            GLFW_RELEASE -> {
                pressedKeys.remove(key)
                holdKeys.remove(key)
            }
        }
    }

}