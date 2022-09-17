package examples.collisions.domain

import examples.collisions.physics
import examples.physics.dto.PhysicalBody
import k2ddt.core.ExecutionContext
import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.*
import k2ddt.ui.dto.InputStateData
import org.joml.Vector2f
import java.text.DecimalFormat
import java.util.*

class Ball(
    centerX: Float,
    centerY: Float,
    private val mass: Float,
    private val color: Color
) {

    private val uuid = UUID.randomUUID().toString()
    private val transform = Transform(
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
        physics.createPhysicalBody(PhysicalBody(uuid, Vector2f(centerX, centerY), mass))
        physics.createCollider(uuid, radius)
    }

    fun tick(updateContext: UpdateContext) {

        physics.getPhysicalBody(uuid)?.let { body ->
            transform.position = body.position
            v = body.velocity
        }

        handleInputs(updateContext.input)
    }

    fun draw(context: ExecutionContext) {
        context.render(Shape(Shape.Type.CIRCLE, color), transform)

        drawVelocity(context)

        drawForceLine(context)

        /*
        collisions.forEach {
            context.render(
                Line(transform.position, it.transform.position, Color(0f, 1f, 0f, 1f)), Transform(0)
            )
        }*/
    }

    private fun drawVelocity(context: ExecutionContext) {
        val text = "${dec.format(v.x)} : ${dec.format(v.y)}"
        context.render(
            Text(text, 16f, Color(0f, 0f, 0f, 1f)),
            Transform(
                transform.position.x,
                transform.position.y,
                0
            )
        )
    }

    private fun drawForceLine(context: ExecutionContext) {
        if (isSelected) {
            context.render(line, lineTransform)
        }
    }

    fun applyForce(force: Vector2f) {
        physics.applyForce(uuid, force)
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

            val mag = distanceToMouse.lengthSquared() * 50f
            applyForce(distanceToMouse.normalize().mul(mag))
        }
    }

}