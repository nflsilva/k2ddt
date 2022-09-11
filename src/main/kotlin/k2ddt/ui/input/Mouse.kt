package k2ddt.ui.input

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE

class Mouse(window: Long) {

    val pressedButtons: MutableSet<Int> = mutableSetOf()
    private var isHold: Boolean = false
    var positionX = 0
    var positionY = 0
    var scrollX = 0
    var scrollY = 0
    private var startDragX: Int? = null
    private var startDragY: Int? = null
    var dragDeltaX = 0
    var dragDeltaY = 0

    var windowWith = intArrayOf(0)
    var windowHeight = intArrayOf(0)

    init {
        GLFW.glfwGetWindowSize(window, windowWith, windowHeight)

        GLFW.glfwSetCursorPosCallback(window) { _: Long, positionX: Double, positionY: Double ->
            onCursorChange(positionX, positionY)
        }
        GLFW.glfwSetMouseButtonCallback(window) { _: Long, button: Int, event: Int, _: Int ->
            onButtonChange(button, event)
        }
        GLFW.glfwSetScrollCallback(window) { _: Long, offsetX: Double, offsetY: Double ->
            onScrollChange(offsetX, offsetY)
        }
    }

    fun onUpdate() {
        scrollX = 0
        scrollY = 0
    }

    private fun onCursorChange(mouseX: Double, mouseY: Double) {
        if (isHold) {
            if(startDragX == null) { startDragX = positionX }
            if(startDragY == null) { startDragY = positionY }

            dragDeltaX = mouseX.toInt() - startDragX!!
            dragDeltaY = windowHeight[0] - mouseY.toInt() - startDragY!!
        }
        else {
            startDragX = null
            startDragY = null
        }

        positionX = mouseX.toInt()
        positionY = windowHeight[0] - mouseY.toInt()
    }

    private fun onButtonChange(button: Int, action: Int) {
        when (action) {
            GLFW_PRESS -> {
                pressedButtons.add(button)
                isHold = true
            }
            GLFW_RELEASE -> {
                pressedButtons.remove(button)
                dragDeltaX = 0
                dragDeltaY = 0
                isHold = false
            }
        }
    }

    private fun onScrollChange(offsetX: Double, offsetY: Double) {
        scrollX = offsetX.toInt()
        scrollY = offsetY.toInt()
    }

}