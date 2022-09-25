package k2ddt.physics.collision

import k2ddt.physics.dto.PhysicalBody
import java.lang.Float.max

class CircleCollisionBox(body: PhysicalBody) : CollisionBox(body) {

    val radius = max(body.scale.x, body.scale.y) / 2f

    override val top: Float
        get() = body.center.y + radius
    override val bottom: Float
        get() = body.center.y - radius
    override val left: Float
        get() = body.center.x - radius
    override val right: Float
        get() = body.center.x + radius

    override fun collideWith(circle: CircleCollisionBox): CollisionVector? {
        return CollisionSolver.computeCollision(this, circle)
    }

    override fun collideWith(box: RectangleCollisionBox): CollisionVector? {
        return CollisionSolver.computeCollision(this, box)
    }

}