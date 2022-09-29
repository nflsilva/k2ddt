package examples.collisions.domain

import examples.collisions.pe
import k2ddt.core.GameEntity
import k2ddt.core.dto.UpdateContext
import k2ddt.physics.dto.PhysicalBody
import k2ddt.render.dto.Color
import k2ddt.render.dto.Shape
import k2ddt.render.dto.Transform

open class Wall(
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
        val body = PhysicalBody(this, PhysicalBody.Type.STATIC, 0f)
        pe.createPhysicalBody(body)
        pe.createBoxCollider(body)
        //transform.rotate(0.25f)
    }

    open fun tick(updateContext: UpdateContext) {
        //transform.rotate(0.25f * updateContext.elapsedTime)
    }

    open fun draw() {
        ee.render(Shape(Shape.Type.SQUARE, color), transform)
    }
}