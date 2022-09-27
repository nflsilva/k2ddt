package k2ddt.physics.collision

import k2ddt.physics.dto.PhysicalBody
import org.joml.Matrix2f
import org.joml.Vector2f

class RectangleCollisionBox(body: PhysicalBody) : CollisionBox(body) {

    private val points = mutableListOf(
        Vector2f(center.x - body.scale.x / 2f, center.y - body.scale.y / 2f),   // botLeft
        Vector2f(center.x - body.scale.x / 2f, center.y + body.scale.y / 2f),   // topLeft
        Vector2f(center.x + body.scale.x / 2f, center.y + body.scale.y / 2f),   // topRight
        Vector2f(center.x + body.scale.x / 2f, center.y - body.scale.y / 2f),   // botRight
    )
    private var lastRotation = body.rotation

    fun getPoints(): List<Vector2f> {
        updatePoints()
        return points
    }

    override val top: Float
        get() = points[1].y
    override val bottom: Float
        get() = points[0].y
    override val left: Float
        get() = points[0].x
    override val right: Float
        get() = points[2].x

    override fun collideWith(circle: CircleCollisionBox): CollisionVector? {
        return CollisionSolver.computeCollision(circle, this)
    }

    override fun collideWith(box: RectangleCollisionBox): CollisionVector? {
        return CollisionSolver.computeCollision(this, box)
    }

    private fun updatePoints()  {
        val delta = body.rotation - lastRotation
        lastRotation = body.rotation
        if(delta != 0f) {
            val rm = Matrix2f().rotation(-delta)
            for(p in points) {
                p.sub(center).mul(rm).add(center)
            }
        }

    }

}