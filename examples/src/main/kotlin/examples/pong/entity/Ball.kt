package examples.pong.entity

import examples.pong.pe
import k2ddt.core.GameEntity
import k2ddt.physics.dto.PhysicalBody
import k2ddt.render.dto.*
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

    init {
        val body = PhysicalBody(this, PhysicalBody.Type.VERLET)
        pe.createPhysicalBody(body)
        pe.createCircleCollider(body)
    }

    fun draw() {
        ee.render(Shape(Shape.Type.CIRCLE, color), transform)
    }

    fun applyForce(force: Vector2f) {
        pe.applyForce(uuid, force)
    }

}