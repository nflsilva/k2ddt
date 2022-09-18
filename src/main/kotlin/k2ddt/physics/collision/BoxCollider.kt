package k2ddt.physics.collision

import k2ddt.physics.dto.PhysicalBody

class BoxCollider(body: PhysicalBody) : Collider(body) {

    override val aabb = AABB(body.position, 15f)

    override fun collideWith(circle: CircleCollider): CollisionVector? {
        return CollisionSolver.computeCollision(circle, this)
    }

    override fun collideWith(box: BoxCollider): CollisionVector? {
        return CollisionSolver.computeCollision(this, box)
    }

}