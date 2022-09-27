package examples.vulcano.domain

import examples.collisions.pe
import k2ddt.core.ExecutionContext
import k2ddt.core.GameEntity
import k2ddt.core.dto.UpdateContext
import k2ddt.physics.dto.PhysicalBody
import k2ddt.render.dto.Color
import k2ddt.render.dto.Line
import k2ddt.render.dto.Shape
import k2ddt.render.dto.Transform
import k2ddt.ui.dto.InputStateData
import org.joml.Math.abs
import org.joml.Vector2f
import java.lang.Float.max
import java.lang.Float.min

class Ball(
    centerX: Float,
    centerY: Float,
    val mass: Float
) : GameEntity() {

    private val color = Color(0.0f, 0.0f, 0.0f, 1.0f)
    override val transform = Transform(
        centerX,
        centerY,
        0f,
        mass * 2f,
        mass * 2f,
        1,
        true
    )
    private var drawConnections = false

    val pos : Vector2f
        get() = transform.position

    var energy = 0.0f
    var maxEnergy = 1000f
    var multiplier = 100f
    var collisions: MutableList<Ball> = mutableListOf()

    init {
        val body = PhysicalBody(this, mass, restituition = 1.0f)
        pe.createPhysicalBody(body)
        pe.createCircleCollider(body)
    }

    fun tick(updateContext: UpdateContext) {
        collisions.clear()
        handleInputs(updateContext.input)
        computeColor()
    }

    fun heatUp(amount: Float) {
        energy += amount
        if (energy >= maxEnergy) {
            applyForce(Vector2f(0f, multiplier * energy))
        }
    }

    fun draw(context: ExecutionContext) {
        context.render(Shape(Shape.Type.CIRCLE, color), transform)

        if (drawConnections) {
            collisions.forEach {
                context.render(
                    Line(transform.position, it.transform.position, Color(0f, 1f, 0f, 1f)), Transform(0)
                )
            }
        }
    }

    fun applyForce(force: Vector2f) {
        pe.applyForce(uuid, Vector2f(force).div(mass))
    }

    private fun computeColor() {
        color.r = energy * 25f / maxEnergy
        color.g = energy * 5f / maxEnergy
        color.b = energy * 1f / maxEnergy
    }

    fun computeEnergyTransfer(ball: Ball) {
        var energyDifference = energy - ball.energy
        if (energyDifference == 0f) return

        if (abs(energyDifference) < 1f) {
            energyDifference /= 2
        } else {
            energyDifference = min(10f, energyDifference * 0.00025f)
        }

        energy -= energyDifference
        ball.energy += energyDifference
    }

    private fun handleInputs(input: InputStateData) {


        if (input.isKeyPressed(InputStateData.KEY_C)) {
            drawConnections = !drawConnections
        }

        if(input.isKeyPressed(InputStateData.KEY_G)) {
            multiplier += 100f
        }
        if(input.isKeyPressed(InputStateData.KEY_B)) {
            multiplier -= 100f
        }

    }

}