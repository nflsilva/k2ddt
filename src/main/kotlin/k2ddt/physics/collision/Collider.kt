package k2ddt.physics.collision

import k2ddt.physics.dto.PhysicalBody

abstract class Collider(val body: PhysicalBody) {

    val collisionVectors = mutableMapOf<String, CollisionVector>()

    abstract val aabb: AABB
    fun collideWith(collider: Collider) {
        (collider as? CircleCollider)?.let {
            collideWith(it)?.let { cv ->
                collisionVectors[this.body.id] = cv
            }
        }

        (collider as? BoxCollider)?.let {
            collideWith(it)?.let { cv ->
                collisionVectors[this.body.id] = cv
            }
        }
    }

    abstract fun collideWith(circle: CircleCollider): CollisionVector?
    abstract fun collideWith(box: BoxCollider): CollisionVector?

}