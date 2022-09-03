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

    fun renderSprite(
        sprite: Sprite,
        transform: Transform,
        color: Color,
        colorPercentage: Float
    ) {
        graphics.render(sprite, transform)
    }

    fun renderShape(
        shape: Shape,
        transform: Transform
    ) {
        graphics.render(shape, transform)
    }

    fun renderParticle(
        particle: Particle,
        transform: Transform
    ) {
        graphics.render(particle, transform)
    }

}
