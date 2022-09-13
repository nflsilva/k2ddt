package k2ddt.ui

import k2ddt.core.EngineConfiguration
import k2ddt.ui.dto.InputStateData
import k2ddt.ui.input.Keyboard
import k2ddt.ui.input.Mouse
import org.lwjgl.glfw.GLFW.glfwGetTime

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

    fun onFrame() {
        window.onFrame()
    }

    fun onUpdate(): InputStateData {
        keyboard.onUpdate()
        mouse.onUpdate()
        window.onUpdate()
        return InputStateData(keyboard, mouse)
    }
}