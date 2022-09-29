package k2ddt.physics.collision

import k2ddt.physics.dto.PhysicalBody

abstract class CollisionBox(
    val body: PhysicalBody,
    val onCollisionCallback: ((other: String) -> Unit)? = null
) {

    val center = body.center
    val collisionVectors = mutableListOf<CollisionVector>()

    abstract val top: Float
    abstract val bottom: Float
    abstract val left: Float
    abstract val right: Float

    fun collideWith(collisionBox: CollisionBox) {
        (collisionBox as? CircleCollisionBox)?.let {
            collideWith(it)?.let { cv ->
                collisionVectors.add(cv)
                collisionBox.onCollisionCallback?.let { cc -> cc(cv.thisId) }
                onCollisionCallback?.let { cc -> cc(cv.otherId) }
            }
        }
        (collisionBox as? RectangleCollisionBox)?.let {
            collideWith(it)?.let { cv ->
                collisionVectors.add(cv)
                collisionBox.onCollisionCallback?.let { cc -> cc(cv.thisId) }
                onCollisionCallback?.let { cc -> cc(cv.otherId) }
            }
        }
    }

    abstract fun collideWith(circle: CircleCollisionBox): CollisionVector?
    abstract fun collideWith(box: RectangleCollisionBox): CollisionVector?
    abstract fun update()
}