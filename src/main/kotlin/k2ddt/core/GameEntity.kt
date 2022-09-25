package k2ddt.core

import k2ddt.render.dto.Transform
import org.joml.Vector2f
import java.util.*

abstract class GameEntity {

    val uuid = UUID.randomUUID().toString()
    protected abstract val transform: Transform
    protected val ee = ExecutionContext.getInstance()

    var position: Vector2f
        get() = transform.position
        set(value) {
            transform.position = value
        }

    val center: Vector2f
        get() = transform.center

    val scale: Vector2f
        get() = transform.scale

    val rotation: Float
        get() = transform.rotation

}