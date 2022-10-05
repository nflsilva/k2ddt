package examples.pong.entity

import k2ddt.core.GameEntity
import k2ddt.core.createCircleCollider
import k2ddt.core.createPhysicalBody
import k2ddt.core.render
import k2ddt.core.applyForce
import k2ddt.physics.dto.PhysicalBody
import k2ddt.render.dto.Color
import k2ddt.render.dto.Shape
import k2ddt.render.dto.Transform
import org.joml.Vector2f

class Ball(
    centerX: Float,
    centerY: Float,
    private val color: Color,
) : GameEntity() {

    override val transform = Transform(
        centerX,
        centerY,
        0f,
        10f * 2f,
        10f * 2f,
        1,
        true
    )

    private var body: PhysicalBody = PhysicalBody(this, PhysicalBody.Type.VERLET)

    init {
        createPhysicalBody(body)
        createCircleCollider(body)
    }

    fun draw() {
        render(Shape(Shape.Type.CIRCLE, color), transform)
    }

    fun applyForce(force: Vector2f) {
        applyForce(body, force)
    }

}