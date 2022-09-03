package k2ddt.ui.input

import org.lwjgl.glfw.GLFW.*

class Keyboard(window: Long) {

    val pressedKeys: MutableSet<Int> = mutableSetOf()

    init {
        glfwSetKeyCallback(window) { _: Long, key: Int, _: Int, action: Int, _: Int ->
            onKeyChange(key, action)
        }
    }

    private fun onKeyChange(key: Int, action: Int) {
        when (action) {
            GLFW_PRESS -> {
                pressedKeys.add(key)
            }
            GLFW_RELEASE -> {
                pressedKeys.remove(key)
            }
        }
    }
}