package k2ddt.core

import k2ddt.render.RenderEngine
import k2ddt.render.dto.*
import k2ddt.ui.UIEngine

class ExecutionContext(
    configuration: EngineConfiguration = EngineConfiguration.default(),
    delegate: ExecutionDelegate? = null

) {

    private val engine: CoreEngine
    private val graphics: RenderEngine
    private val ui: UIEngine

    init {
        graphics = RenderEngine(
            configuration.resolutionWidth,
            configuration.resolutionHeight
        )
        ui = UIEngine(configuration)
        engine = CoreEngine(configuration, graphics, ui, delegate)
        delegate?.executionContext = this
    }

    fun start(){
        engine.start()
    }

    fun render(
        sprite: Sprite,
        transform: Transform
    ) {
        graphics.render(sprite, transform)
    }

    fun render(
        shape: Shape,
        transform: Transform
    ) {
        graphics.render(shape, transform)
    }

    fun render(
        particle: Particle,
        transform: Transform
    ) {
        graphics.render(particle, transform)
    }

    fun render(
        text: Text,
        transform: Transform
    ) {
        graphics.render(text, transform)
    }

}
