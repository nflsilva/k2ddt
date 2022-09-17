package examples.physics.collision

import examples.physics.dto.PhysicalBody

class CircleCollider(body: PhysicalBody, val radius: Float) : Collider(body) {

    override val aabb = AABB(body.position, radius)

}