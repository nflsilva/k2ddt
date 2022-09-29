package examples.collisions.domain

import examples.collisions.pe
import k2ddt.core.ExecutionContext
import k2ddt.core.GameEntity
import k2ddt.core.dto.UpdateContext
import k2ddt.physics.dto.PhysicalBody
import k2ddt.render.dto.*
import k2ddt.ui.dto.InputStateData
import org.joml.Vector2f
import java.text.DecimalFormat
import java.util.*

class Ball(
    centerX: Float,
    centerY: Float,
    private val mass: Float,
    private val color: Color,
) : GameEntity() {

    override val transform = Transform(
        centerX,
        centerY,
        0f,
        radius * 2f,
        radius * 2f,
        1,
        true
    )

    private val radius: Float
        get() = mass / 2f

    private var isSelected = false
    private var line = Line(centerX, centerY, 0f, 0f, Color(1f, 0f, 0f, 1f))
    private var lineTransform = Transform(0)

    private val dec = DecimalFormat("##.##")
    private var v = Vector2f(0f)

    init {
        val body = PhysicalBody(this, PhysicalBody.Type.VERLET, mass, 1f)
        pe.createPhysicalBody(body)
        pe.createCircleCollider(body)
    }

    fun tick(updateContext: UpdateContext) {
        handleInputs(updateContext.input)
    }

    fun draw() {
        ee.render(Shape(Shape.Type.CIRCLE, color), transform)
        //drawVelocity()
        drawForceLine()
    }

    private fun drawVelocity() {
        val text = "${dec.format(v.x)} : ${dec.format(v.y)}"
        ee.render(
            Text(text, 16f, Color(0f, 0f, 0f, 1f)),
            Transform(
                transform.position.x,
                transform.position.y,
                0
            )
        )
    }

    private fun drawForceLine() {
        if (isSelected) {
            ee.render(line, lineTransform)
        }
    }

    fun applyForce(force: Vector2f) {
        pe.applyForce(uuid, force)
    }

    private fun handleInputs(input: InputStateData) {

        line.startPoint.x = transform.position.x
        line.startPoint.y = transform.position.y
        line.endPoint.x = input.mouseX.toFloat()
        line.endPoint.y = input.mouseY.toFloat()

        val x = transform.position.x
        val y = transform.position.y

        if (input.isMouseHold(InputStateData.BUTTON_LEFT)) {
            if (isSelected) return

            val distance2 = Vector2f(x - input.mouseX, y - input.mouseY)

            isSelected = distance2.lengthSquared() <= (radius * radius)

        } else if (isSelected) {
            isSelected = false
            val distanceToMouse = Vector2f(
                input.mouseX - x,
                input.mouseY - y
            )

            val mag = distanceToMouse.lengthSquared() * 25f
            applyForce(distanceToMouse.normalize().mul(mag))
        }

    }

}