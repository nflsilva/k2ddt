package examples.pong.entity

import examples.pong.pe
import k2ddt.core.GameEntity
import k2ddt.core.dto.UpdateContext
import k2ddt.physics.dto.PhysicalBody
import k2ddt.render.dto.Color
import k2ddt.render.dto.Shape
import k2ddt.render.dto.Transform
import k2ddt.sound.dto.Sound
import k2ddt.ui.dto.InputStateData

class Platform(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    private val leftLimit: Float,
    private val rightLimit: Float,
): GameEntity() {

    private val pongSound = Sound("/sound/pong0.ogg", "default", false)
    private val color = Color(1.0f)
    override val transform = Transform(
        x,
        y,
        0f,
        width,
        height,
        1,
        true
    )

    init {
        val body = PhysicalBody(this, PhysicalBody.Type.STATIC)
        pe.createPhysicalBody(body)
        pe.createBoxCollider(body) { oid -> onCollision(oid) }
    }

    fun tick(updateContext: UpdateContext) {
        processInputs(updateContext.input, updateContext.elapsedTime)
        enforceLimits()
    }

    fun draw() {
        ee.render(Shape(Shape.Type.SQUARE, color), transform)
    }

    private fun processInputs(input: InputStateData, elapsedTime: Float) {
        transform.position.x = input.mouseX.toFloat()
        //transform.position.y = input.mouseY.toFloat()
        transform.rotation += input.scrollY * 0.15f
    }

    private fun enforceLimits() {
        if(transform.left <= leftLimit) {
            transform.position.x += (leftLimit - transform.left)
        }
        else if(transform.right >= rightLimit) {
            transform.position.x -= (transform.right - rightLimit)
        }
    }

    private fun onCollision(other: String) {
        ee.playSound(pongSound)
    }
}