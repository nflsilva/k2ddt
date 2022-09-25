package k2ddt.core

import k2ddt.core.dto.UpdateContext
import k2ddt.render.RenderEngine
import k2ddt.sound.SoundEngine
import k2ddt.tools.Profiler
import k2ddt.ui.UIEngine

class CoreEngine(
    configuration: EngineConfiguration? = null,
    private val renderEngine: RenderEngine,
    private val uiEngine: UIEngine,
    private val soundEngine: SoundEngine,
    //private val physicsEngine: PhysicsEngine,
    private val delegate: ExecutionDelegate? = null
) {

    companion object {
        private const val printsPerSecondCap: Int = 1
        private const val ticksPerSecondCap: Int = 128
        private const val framePerSecondCap: Int = 500
    }

    private val profiler = Profiler()
    private var profilerData = profiler.getData()
    private var isRunning = false
    private val configuration: EngineConfiguration

    init {
        this.configuration = configuration ?: EngineConfiguration.default()
    }

    private fun run() {

        val step = 0.01
        val timeSlice = 1f / 60

        var firstTime = 0.0
        var lastTime = uiEngine.getTime()
        var passedTime = 0.0
        var unprocessedTime = 0.0

        var render = false
        var frames = 0
        var passedSinceFPS = 0.0

        while (isRunning) {

            render = false
            firstTime = uiEngine.getTime()

            passedTime = firstTime - lastTime
            lastTime = firstTime

            unprocessedTime += passedTime
            passedSinceFPS += passedTime

            while (unprocessedTime >= timeSlice) {
                onUpdate(step)
                unprocessedTime -= timeSlice
                render = true
            }

            if (render) {
                onFrame()
                frames += 1
            }

            if(passedSinceFPS >= 1.0){
                passedSinceFPS = 0.0
                profilerData = profiler.getData()
                profilerData.framesPerSecond = frames
                frames = 0
            }

            if (!uiEngine.isRunning()) {
                isRunning = false
            }

            Thread.sleep(10)
        }

        onCleanUp()
    }

    private fun onFrame() {

        profiler.start("onFrame")
        uiEngine.onFrame()
        renderEngine.onFrame()

        profiler.end("onFrame")

        profiler.start("[user] onFrame")
        delegate?.onFrame()
        profiler.end("[user] onFrame")
    }

    private fun onUpdate(elapsedTime: Double) {

        profiler.start("onUIUpdate")
        val input = uiEngine.onUpdate()
        val updateContext = UpdateContext(elapsedTime.toFloat(), input)
        profiler.end("onUIUpdate")


        /*profiler.start("onPhysicsUpdate")
        physicsEngine.onUpdate()
        profiler.end("onPhysicsUpdate")*/

        profiler.start("[user] onUpdate")
        delegate?.onUpdate(updateContext)
        profiler.end("[user] onUpdate")
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

    fun getProfilingData(): ProfilingData {
        return profilerData
    }

}