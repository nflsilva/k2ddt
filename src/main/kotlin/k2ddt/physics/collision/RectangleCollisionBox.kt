package k2ddt.physics.collision

import k2ddt.physics.dto.PhysicalBody
import org.joml.Matrix2f
import org.joml.Vector2f

class RectangleCollisionBox(
    body: PhysicalBody,
    onCollisionCallback: ((other: String) -> Unit)?
) : CollisionBox(body, onCollisionCallback) {

    private var points = mutableListOf(
        Vector2f(center.x - body.scale.x / 2f, center.y - body.scale.y / 2f),   // botLeft
        Vector2f(center.x - body.scale.x / 2f, center.y + body.scale.y / 2f),   // topLeft
        Vector2f(center.x + body.scale.x / 2f, center.y + body.scale.y / 2f),   // topRight
        Vector2f(center.x + body.scale.x / 2f, center.y - body.scale.y / 2f),   // botRight
    )
    private var lastRotation = body.rotation
    private var lastPosition = body.position

    fun getPoints(): List<Vector2f> {
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

    override fun update(){
        updatePointsTranslation()
        updatePointsRotation()
    }

    private fun updatePointsRotation() {
        val delta = body.rotation - lastRotation
        lastRotation = body.rotation
        if (delta != 0f) {
            val rm = Matrix2f().rotation(-delta)
            for (p in points) {
                p.sub(center).mul(rm).add(center)
            }
        }
    }

    private fun updatePointsTranslation() {
        val delta = Vector2f(body.position).sub(lastPosition)
        lastPosition = Vector2f(body.position)
        if (delta.x != 0f || delta.y != 0f) {
            for(p in points) {
                p.add(delta)
            }
        }
    }
}