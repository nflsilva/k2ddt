package k2ddt.physics.collision

import k2ddt.physics.dto.PhysicalBody

class CircleCollider(body: PhysicalBody, val radius: Float) : Collider(body) {

    override val aabb = AABB(body.position, radius)

}