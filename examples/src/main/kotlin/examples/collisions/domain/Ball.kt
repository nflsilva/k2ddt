package examples.collisions.domain

import k2ddt.core.ExecutionContext
import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.Color
import k2ddt.render.dto.Line
import k2ddt.render.dto.Shape
import k2ddt.render.dto.Transform
import org.joml.Vector2f
import k2ddt.ui.dto.InputStateData

class Ball(
    centerX: Float,
    centerY: Float,
    val mass: Float,
    color: Color
) {

    private val color = Color(1.0f)
    val transform = Transform(
        centerX,
        centerY,
        0f,
        radius * 2f,
        radius * 2f,
        1,
        true
    )
    private var p0 = Vector2f(centerX, centerY)
    private var dt0 : Float = -1f
    private var acceleration = Vector2f(0f)

    var x: Float
        get() = transform.position.x
        set(value) {
            transform.position.x = value
        }

    var y: Float
        get() = transform.position.y
        set(value) {
            transform.position.y = value
        }

    val radius: Float
        get() = mass

    var isSelected = false
    var line = Line(centerX, centerY, 0f, 0f, Color(1f))
    var lineTransform = Transform(0)
    var collisions: MutableList<Ball> = mutableListOf()

    fun tick(updateContext: UpdateContext) {
        collisions.clear()
        computePosition(updateContext.elapsedTime)
        handleInputs(updateContext.input)
    }

    fun draw(context: ExecutionContext) {
        context.render(Shape(Shape.Type.CIRCLE, color), transform)
        if (isSelected) {
            context.render(line, lineTransform)
        }

        collisions.forEach {
            context.render(
                Line(transform.position, it.transform.position, Color(0f, 1f, 0f, 1f)), Transform(0)
            )
        }
    }

    fun collideWith(ball: Ball) {

    }

    fun applyForce(force: Vector2f) {
        acceleration.add(Vector2f(force).div(mass))
    }

    fun applyAcceleration(acceleration: Vector2f) {
        this.acceleration = Vector2f(acceleration)
    }

    private fun computePosition(deltaTime: Float) {

        if(dt0 == -1f) {
            dt0 = deltaTime
            return
        }

        //  x1 =
        //  x +
        // (x – x0) * dt / dt0 +
        //  a * dt * (dt + dt0) / 2

        val newPrevPosition = Vector2f(transform.position)

        val xx0dt = Vector2f(transform.position)
            .sub(p0)
            .mul(deltaTime)
            .div(dt0)

        val adtdtdt0 = Vector2f(acceleration)
            .mul(deltaTime)
            .mul((deltaTime + dt0) / 2)


        transform.position.add(xx0dt).add(adtdtdt0)

        p0 = newPrevPosition

        acceleration.zero()

    }

    private fun handleInputs(input: InputStateData) {

        line.startPoint.x = transform.position.x
        line.startPoint.y = transform.position.y
        line.endPoint.x = input.mouseX.toFloat()
        line.endPoint.y = input.mouseY.toFloat()

        if (input.isMousePressed(InputStateData.BUTTON_RIGHT)) {

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