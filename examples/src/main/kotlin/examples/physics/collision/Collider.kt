package examples.physics.collision

import examples.physics.dto.PhysicalBody
import org.joml.Vector2f

abstract class Collider(val body: PhysicalBody) {

    val collisionVectors = mutableMapOf<String, CollisionVector>()

    abstract val aabb: AABB
}