package k2ddt.core

import k2ddt.core.dto.UpdateContext
import k2ddt.render.RenderEngine
import k2ddt.tools.Log
import k2ddt.ui.UIEngine
import ui.dto.InputStateData

class CoreEngine(
    configuration: EngineConfiguration? = null,
    private val renderEngine: RenderEngine,
    private val uiEngine: UIEngine,
    private val delegate: ExecutionDelegate? = null
) {

    companion object {
        private const val printsPerSecondCap: Int = 1
        private const val ticksPerSecondCap: Int = 128
        private const val framePerSecondCap: Int = 500
    }

    private var isRunning: Boolean = false

    private val configuration: EngineConfiguration

    init {
        this.configuration = configuration ?: EngineConfiguration.default()
    }

    private fun run() {

        var frameStart: Double
        var frameEnd: Double
        var frameDelta = 0.0

        var frames = 0
        var ticks = 0

        val tickTime: Double = 1.0 / ticksPerSecondCap
        val frameTime: Double = 1.0 / framePerSecondCap
        val printTime: Double = 1.0 / printsPerSecondCap

        var timeSinceTick = 0.0
        var timeSinceFrame = 0.0
        var timeSincePrint = 0.0

        while (isRunning) {

            frameStart = uiEngine.getTime()

            if (timeSinceTick >= tickTime) {
                onUpdate(tickTime, uiEngine.getInputState())
                ticks++
                timeSinceTick = 0.0
            }

            if (timeSinceFrame >= frameTime) {
                onFrame()
                frames++
                timeSinceFrame = 0.0
            }

            if (timeSincePrint >= printTime) {
                Log.d("FramesPerSecond: $frames\t\tFrameTime: ${frameDelta * 1000}\t\tTicks: $ticks")
                //Log.d("Camera: ${camera.position}")
                ticks = 0
                frames = 0
                timeSincePrint = 0.0
            }
            if (!uiEngine.isRunning()) {
                isRunning = false
            }

            frameEnd = uiEngine.getTime()
            frameDelta = frameEnd - frameStart
            timeSinceTick += frameDelta
            timeSinceFrame += frameDelta
            timeSincePrint += frameDelta

        }

        onCleanUp()
    }

    private fun onFrame() {
        uiEngine.onFrame()
        renderEngine.onFrame()
        delegate?.onFrame()
    }

    private fun onUpdate(elapsedTime: Double, input: InputStateData) {
        val updateContext = UpdateContext(elapsedTime.toFloat(), input)
        uiEngine.onUpdate()
        renderEngine.onUpdate()
        delegate?.onUpdate(updateContext)
    }

    private fun onCleanUp() {
        delegate?.onCleanUp()
    }

    fun start() {
        if (isRunning) return
        uiEngine.start()
        renderEngine.onStart()
        delegate?.onStart()

        isRunning = true
        run()
    }

}