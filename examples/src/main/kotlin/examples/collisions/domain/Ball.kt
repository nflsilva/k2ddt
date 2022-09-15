package examples.collisions.domain

import k2ddt.core.ExecutionContext
import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.*
import k2ddt.render.font.DefaultFont
import k2ddt.ui.dto.InputStateData
import org.joml.Vector2f
import java.text.DecimalFormat

class Ball(
    centerX: Float,
    centerY: Float,
    private val mass: Float,
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
    private var dt0: Float = -1f
    private var acceleration = Vector2f(0f)
    private var velocity = Vector2f(0f)
    private var computeVelocity = true

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

    private val radius: Float
        get() = mass

    private var isSelected = false
    private var line = Line(centerX, centerY, 0f, 0f, Color(1f, 0f, 0f, 1f))
    private var lineTransform = Transform(0)
    private var collisions: MutableList<Ball> = mutableListOf()
    private val dec = DecimalFormat("##.##")

    fun tick(updateContext: UpdateContext) {
        collisions.clear()
        computePosition(updateContext.elapsedTime)
        handleInputs(updateContext.input)
    }

    fun draw(context: ExecutionContext) {
        context.render(Shape(Shape.Type.CIRCLE, color), transform)
        drawVelocity(context)

        drawForceLine(context)

        collisions.forEach {
            context.render(
                Line(transform.position, it.transform.position, Color(0f, 1f, 0f, 1f)), Transform(0)
            )
        }
    }

    private fun drawVelocity(context: ExecutionContext) {
        val text = "${dec.format(velocity.x)} : ${dec.format(velocity.y)}"
        context.render(
            Text(text, 5f),
            Transform(
                transform.position.x + 100f,
                transform.position.y + 50f,
                0
            )
        )
    }

    private fun drawForceLine(context: ExecutionContext) {
        if (isSelected) {
            context.render(line, lineTransform)
        }
    }

    fun collideWith(ball: Ball) {

        val distanceVector = Vector2f(ball.x - x, ball.y - y)
        val distance = distanceVector.length()
        val delta = distance - (ball.radius + radius)

        if (delta < 0) {
            val x2 = Vector2f(ball.transform.position)
            val v2 = Vector2f(ball.velocity)
            val m2 = ball.mass

            ball.computeNewSpeed(Vector2f(transform.position), Vector2f(velocity), mass, distance * distance)
            computeNewSpeed(x2, v2, m2, distance * distance)
        }
    }

    fun collideWith(wall: Wall) {

        val left = x - radius
        val right = x + radius
        val top = y + radius
        val bottom = y - radius


        val hitLeft = (left < wall.right && right > wall.right)
        val hitRight = (right > wall.left && left < wall.left)

        if (hitLeft || hitRight) {

            x = if (hitLeft) {
                wall.right + radius
            } else {
                wall.left - radius
            }

            velocity = Vector2f(-velocity.x, velocity.y)
            computeVelocity = false
            return
        }

        val hitTop = (top > wall.bottom && bottom < wall.bottom)
        val hitBottom = (bottom < wall.top && top > wall.top)
        if (hitTop || hitBottom) {

            y = if (hitTop) {
                wall.bottom - radius
            } else {
                wall.top + radius
            }

            velocity = Vector2f(velocity.x, -velocity.y)
            computeVelocity = false
        }
    }

    private fun computeNewSpeed(x2: Vector2f, v2: Vector2f, m2: Float, distance2: Float) {

        val massAux = 2 * m2 / (mass + m2)

        val crossAux0 = Vector2f(velocity).sub(v2)
        val crossAux1 = Vector2f(transform.position).sub(x2)
        val crossAux = crossAux0.dot(crossAux1) / distance2

        val distanceAux = Vector2f(crossAux1)

        val bigAux = distanceAux.mul(massAux * crossAux)
        velocity.sub(bigAux)
        computeVelocity = false

    }

    fun applyForce(force: Vector2f) {
        acceleration.add(Vector2f(force).div(mass))
    }

    fun applyAcceleration(acceleration: Vector2f) {
        this.acceleration = Vector2f(acceleration)
    }

    private fun computePosition(deltaTime: Float) {

        if (dt0 == -1f) {
            dt0 = deltaTime
            return
        }

        //  x1 =
        //  x +
        // (x – x0) * dt / dt0 +
        //  a * dt * (dt + dt0) / 2

        val newPrevPosition = Vector2f(transform.position)

        if (computeVelocity) {
            velocity = Vector2f(transform.position).sub(p0)
        }
        computeVelocity = true

        val xx0dt = Vector2f(velocity)
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