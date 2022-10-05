package k2ddt.core

import k2ddt.physics.PhysicsEngine
import k2ddt.physics.dto.PhysicalBody
import k2ddt.render.RenderEngine
import k2ddt.render.dto.*
import k2ddt.sound.SoundEngine
import k2ddt.sound.dto.Sound
import k2ddt.ui.UIEngine
import org.joml.Vector2f

class ExecutionContext private constructor() {

    lateinit var engine: CoreEngine
    lateinit var graphics: RenderEngine
    lateinit var audio: SoundEngine
    lateinit var physics: PhysicsEngine
    lateinit var ui: UIEngine

    companion object {
        private var instance: ExecutionContext? = null

        fun getInstance(): ExecutionContext {
            if(instance == null) {
                instance = ExecutionContext()
            }
            return instance!!
        }
    }

    fun setup(configuration: EngineConfiguration = EngineConfiguration.default(),
              delegate: ExecutionDelegate? = null) {
        graphics = RenderEngine(
            configuration.resolutionWidth,
            configuration.resolutionHeight
        )
        ui = UIEngine(configuration)
        audio = SoundEngine()
        physics = PhysicsEngine()
        engine = CoreEngine(configuration, graphics, ui, audio, physics, delegate)
        delegate?.executionContext = this
    }

    fun start() {
        engine.start()
    }
}

fun setBackgroundColor(color: Color) {
    ExecutionContext.getInstance().graphics.setBackgroundColor(color)
}

fun moveCamera(deltaX: Float, deltaY: Float) {
    ExecutionContext.getInstance().graphics.moveCamera(deltaX, deltaY)
}

fun zoomCamera(amount: Float) {
    ExecutionContext.getInstance().graphics.zoomCamera(amount)
}

fun getProfileData(): ProfilingData {
    return ExecutionContext.getInstance().engine.getProfilingData()
}

fun render(sprite: Sprite, transform: Transform) {
    ExecutionContext.getInstance().graphics.render(sprite, transform)
}

fun render(shape: Shape, transform: Transform) {
    ExecutionContext.getInstance().graphics.render(shape, transform)
}

fun render(particle: Particle, transform: Transform) {
    ExecutionContext.getInstance().graphics.render(particle, transform)
}

fun render(text: Text, transform: Transform) {
    ExecutionContext.getInstance().graphics.render(text, transform)
}

fun render(line: Line, transform: Transform) {
    ExecutionContext.getInstance().graphics.render(line, transform)
}

fun playSound(sound: Sound) {
    ExecutionContext.getInstance().audio.playSound(sound)
}

fun createPhysicalBody(body: PhysicalBody) {
    ExecutionContext.getInstance().physics.createPhysicalBody(body)
}

fun removePhysicalBody(body: PhysicalBody) {
    ExecutionContext.getInstance().physics.removePhysicalBody(body.id)
}

fun createCircleCollider(body: PhysicalBody, onCollisionCallback: ((other: String) -> Unit)? = null) {
    ExecutionContext.getInstance().physics.createCircleCollider(body, onCollisionCallback)
}

fun createBoxCollider(body: PhysicalBody, onCollisionCallback: ((other: String) -> Unit)? = null) {
    ExecutionContext.getInstance().physics.createBoxCollider(body, onCollisionCallback)
}

fun getPhysicalBody(uuid: String): PhysicalBody? {
    return ExecutionContext.getInstance().physics.getPhysicalBody(uuid)
}

fun applyForce(body: PhysicalBody, force: Vector2f) {
    ExecutionContext.getInstance().physics.applyForce(body, force)
}


