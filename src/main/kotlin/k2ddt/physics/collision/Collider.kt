package k2ddt.physics.collision

import k2ddt.physics.dto.PhysicalBody

abstract class Collider(val body: PhysicalBody) {

    val collisionVectors = mutableMapOf<String, CollisionVector>()

    abstract val aabb: AABB
}