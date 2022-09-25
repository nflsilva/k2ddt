package k2ddt.physics.collision

import k2ddt.physics.dto.PhysicalBody
import org.joml.Vector2f

abstract class CollisionBox(val body: PhysicalBody) {

    val center = body.center
    val collisionVectors = mutableMapOf<String, CollisionVector>()

    abstract val top: Float
    abstract val bottom: Float
    abstract val left: Float
    abstract val right: Float

    fun collideWith(collisionBox: CollisionBox) {
        (collisionBox as? CircleCollisionBox)?.let {
            collideWith(it)?.let { cv ->
                collisionVectors[this.body.id] = cv
            }
        }
        (collisionBox as? RectangleCollisionBox)?.let {
            collideWith(it)?.let { cv ->
                collisionVectors[this.body.id] = cv
            }
        }
    }

    abstract fun collideWith(circle: CircleCollisionBox): CollisionVector?
    abstract fun collideWith(box: RectangleCollisionBox): CollisionVector?

}