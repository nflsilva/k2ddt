package examples.pong.entity

import examples.pong.pe
import k2ddt.core.GameEntity
import k2ddt.physics.dto.PhysicalBody
import k2ddt.render.dto.Color
import k2ddt.render.dto.Shape
import k2ddt.render.dto.Transform

class Wall(
    x: Float,
    y: Float,
    width: Float,
    height: Float
): GameEntity() {

    private val color = Color(1.0f)
    override val transform = Transform(
        x,
        y,
        0f,
        width,
        height,
        1,
        false
    )

    init {
        val body = PhysicalBody(this, PhysicalBody.Type.STATIC)
        pe.createPhysicalBody(body)
        pe.createBoxCollider(body)
    }

    fun draw() {
        ee.render(Shape(Shape.Type.SQUARE, color), transform)
    }
}