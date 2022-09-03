package k2ddt.ui

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.createCapabilities

class Window(
    width: Int,
    height: Int,
    title: String,
    enableVsync: Boolean
) {

    val id: Long

    init {
        if (!glfwInit()) throw IllegalStateException("Unable to initialize GLFW")
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)

        id = glfwCreateWindow(width, height, title, 0, 0)

        glfwMakeContextCurrent(id)
        glfwSwapInterval(if (enableVsync) 1 else 0)
    }

    fun open() {
        createCapabilities()
        glfwShowWindow(id)
    }

    fun close() {
        glfwSetWindowShouldClose(id, true);
    }

    fun isOpen(): Boolean {
        return !glfwWindowShouldClose(id)
    }

    fun onFrame() {
        glfwSwapBuffers(id);
    }

    fun onUpdate() {
        glfwPollEvents();
    }
}