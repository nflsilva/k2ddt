package k2ddt.ui

import org.lwjgl.glfw.GLFW.glfwGetTime
import k2ddt.core.EngineConfiguration
import ui.dto.InputStateData
import k2ddt.ui.input.Keyboard
import k2ddt.ui.input.Mouse

class UIEngine(configuration: EngineConfiguration) {

    private val window: Window = Window(
        configuration.resolutionWidth,
        configuration.resolutionHeight,
        configuration.windowTitle,
        configuration.enableVsync
    )
    private val keyboard: Keyboard = Keyboard(window.id)
    private val mouse: Mouse = Mouse(window.id)

    fun getTime(): Double {
        return glfwGetTime()
    }

    fun start() {
        window.open()
    }

    fun stop() {
        window.close()
    }

    fun isRunning(): Boolean {
        return window.isOpen()
    }

    fun getInputState(): InputStateData {
        return InputStateData(keyboard, mouse)
    }

    fun onFrame() {
        window.onFrame()
    }

    fun onUpdate() {
        mouse.onUpdate()
        window.onUpdate()
    }
}