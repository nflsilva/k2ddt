package k2ddt.core

import k2ddt.render.RenderEngine
import k2ddt.render.dto.*
import k2ddt.sound.SoundEngine
import k2ddt.sound.dto.Sound
import k2ddt.ui.UIEngine

class ExecutionContext(
    configuration: EngineConfiguration = EngineConfiguration.default(),
    delegate: ExecutionDelegate? = null

) {

    private val engine: CoreEngine
    private val graphics: RenderEngine
    private val audio: SoundEngine
    private val ui: UIEngine

    init {
        graphics = RenderEngine(
            configuration.resolutionWidth,
            configuration.resolutionHeight
        )
        ui = UIEngine(configuration)
        audio = SoundEngine()
        engine = CoreEngine(configuration, graphics, ui, audio, delegate)
        delegate?.executionContext = this
    }

    fun start() {
        engine.start()
    }

    fun setBackgroundColor(color: Color) {
        graphics.setBackgroundColor(color)
    }

    fun moveCamera(deltaX: Float, deltaY: Float) {
        graphics.moveCamera(deltaX, deltaY)
    }

    fun zoomCamera(amount: Float) {
        graphics.zoomCamera(amount)
    }

    fun getProfileData(): ProfilingData {
        return engine.getProfilingData()
    }

    fun render(sprite: Sprite, transform: Transform) {
        graphics.render(sprite, transform)
    }

    fun render(shape: Shape, transform: Transform) {
        graphics.render(shape, transform)
    }

    fun render(particle: Particle, transform: Transform) {
        graphics.render(particle, transform)
    }

    fun render(text: Text, transform: Transform) {
        graphics.render(text, transform)
    }

    fun render(line: Line, transform: Transform) {
        graphics.render(line, transform)
    }

    fun playSound(sound: Sound) {
        audio.playSound(sound)
    }
}
