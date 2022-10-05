package examples.pong.entity

import k2ddt.core.*
import k2ddt.physics.dto.PhysicalBody
import k2ddt.render.dto.Color
import k2ddt.render.dto.Shape
import k2ddt.render.dto.Text
import k2ddt.render.dto.Transform
import k2ddt.sound.dto.Sound

class Block(
    x: Float,
    y: Float,
    width: Float,
    height: Float
): GameEntity() {

    var isDead = false
    private var health = 10
    private val pongSound = Sound("/sound/pong1.ogg", "default", false)
    private val color = Color(1.0f)
    override val transform = Transform(
        x,
        y,
        0f,
        width,
        height,
        2,
        true
    )
    private val textTransform = Transform(x, y, 1)
    private var body: PhysicalBody = PhysicalBody(this, PhysicalBody.Type.STATIC)

    init {
        createPhysicalBody(body)
        createBoxCollider(body) { oid -> onCollision(oid) }
    }

    fun draw() {
        render(Shape(Shape.Type.SQUARE, color), transform)
        render(Text("$health", 32f, Color(0f, 0f, 0f, 1f)), textTransform)
    }

    fun remove(){
        removePhysicalBody(body)
    }

    private fun onCollision(other: String) {
        health -= 1
        isDead = health <= 0
        playSound(pongSound)
    }
}