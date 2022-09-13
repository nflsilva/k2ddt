package examples.shooter.domain.levelObject

import examples.shooter.domain.GameUpdateContext
import k2ddt.core.ExecutionContext
import k2ddt.render.dto.Transform
import org.joml.Vector2f

abstract class Drawable(
    positionX: Float,
    positionY: Float,
    rotation: Float,
    sizeX: Float,
    sizeY: Float,
    layer: Int,
    var direction: Vector2f = Vector2f(1f, 0f),
    var hasCollision: Boolean = false,
    var parent: Drawable? = null
) {

    protected val childen = mutableListOf<Drawable>()
    val transform = Transform(
        positionX,
        positionY,
        rotation,
        sizeX,
        sizeY,
        layer
    )

    constructor(parent: Drawable, sizeX: Float, sizeY: Float, layer: Int) :
            this(
                parent.transform.position.x,
                parent.transform.position.y,
                parent.transform.rotation,
                sizeX,
                sizeY,
                layer,
                parent.direction,
                parent = parent
            )

    open fun onFrame(context: ExecutionContext) {
        childen.forEach { it.onFrame(context) }
    }

    open fun onUpdate(context: GameUpdateContext) {
        childen.forEach { it.onUpdate(context) }
    }

    open fun onHit() {
        childen.forEach { it.onHit() }
    }

}