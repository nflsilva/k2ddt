package examples.pong.entity

import examples.pong.pe
import k2ddt.core.GameEntity
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
    private val ttransform = Transform(x, y, 1)

    init {
        val body = PhysicalBody(this, PhysicalBody.Type.STATIC)
        pe.createPhysicalBody(body)
        pe.createBoxCollider(body) { oid -> onCollision(oid) }
    }

    fun draw() {
        ee.render(Shape(Shape.Type.SQUARE, color), transform)
        ee.render(Text("$health", 32f, Color(0f, 0f, 0f, 1f)), ttransform)
    }

    fun remove(){
        pe.removePhysicalBody(uuid)
    }

    private fun onCollision(other: String) {
        health -= 1
        isDead = health <= 0
        ee.playSound(pongSound)
    }
}