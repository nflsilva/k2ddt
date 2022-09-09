package examples.balls.`object`

import k2ddt.core.ExecutionContext
import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.Color
import k2ddt.render.dto.Line
import k2ddt.render.dto.Shape
import k2ddt.render.dto.Transform
import org.joml.Math.abs
import org.joml.Vector2f
import ui.dto.InputStateData
import java.lang.Float.max

class Ball(
    centerX: Float,
    centerY: Float,
    val mass: Float,
    color: Color
) {

    private val color = Color(0.0f, 0.0f, 0.0f, 1.0f)
    private val transform = Transform(
        centerX,
        centerY,
        0f,
        radius * 2f,
        radius * 2f,
        1,
        true
    )
    private var previousPosition = Vector2f(centerX, centerY)
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
    var energy = 0.0f
    var collisions : MutableList<Vector2f> = mutableListOf()

    fun tick(updateContext: UpdateContext) {
        collisions.clear()
        computePosition(updateContext.elapsedTime)
        handleInputs(updateContext.input)
    }

    private fun computePosition(deltaTime: Float) {
        val newPrevPosition = Vector2f(transform.position)
        val dtt = Vector2f(acceleration).mul(deltaTime * deltaTime)

        val v = Vector2f(transform.position).sub(previousPosition).mul(0.99f)
        transform.position.add(v).add(dtt)

        previousPosition = newPrevPosition

        acceleration.zero()

        if (transform.position.y <= 15f + radius) {
            energy += 100f
        }
        else {
            energy = max(0f, energy - 5f)
        }

        if (energy > 1000f && acceleration.y <= 0) {
            applyForce(Vector2f(0f, energy * 50f))
        }

        color.r = energy * 0.001f
    }

    private fun handleInputs(input: InputStateData) {

        line.startPoint.x = transform.position.x
        line.startPoint.y = transform.position.y
        line.endPoint.x = input.mouseX.toFloat() + input.dragDeltaX
        line.endPoint.y = input.mouseY.toFloat() + input.dragDeltaY

        if (input.isMousePressed(InputStateData.BUTTON_RIGHT)) {

            if (isSelected) return

            val distance2 = Vector2f(x - input.mouseX, y - input.mouseY)

            isSelected = distance2.lengthSquared() <= (radius * radius)

        } else if (isSelected) {
            isSelected = false
            val distanceToMouse = Vector2f(
                input.mouseX + input.dragDeltaX - x,
                input.mouseY + input.dragDeltaY - y
            )

            val mag = distanceToMouse.lengthSquared() * -50f
            applyForce(distanceToMouse.normalize().mul(mag))
        }
    }

    fun draw(context: ExecutionContext) {
        context.render(Shape(Shape.Type.CIRCLE, color), transform)
        if (isSelected) {
            context.render(line, lineTransform)
        }
        collisions.forEach { context.render(
            Line(transform.position, it, Color(0f, 1f, 0f, 1f)), Transform(0))
        }
    }

    fun collideWith(ball: Ball) {
        computeStaticCollision(ball)
    }

    private fun computeStaticCollision(ball: Ball) {
        val distanceVector = Vector2f(ball.x - x, ball.y - y)
        val distance = distanceVector.length()
        val delta = distance - (ball.radius + radius)

        if (delta < -0.5) {
            val deltaTranslate = distanceVector.normalize().mul(delta / 2f)

            x += deltaTranslate.x
            y += deltaTranslate.y

            ball.x -= deltaTranslate.x
            ball.y -= deltaTranslate.y
            //collisions.add(ball.transform.position)
        } else if (abs(delta) < 0.5) {

            if (energy > ball.energy) {
                energy -= 35.0f
                ball.energy += 35.0f
                collisions.add(ball.transform.position)

            } else if (energy < ball.energy) {
                energy += 35.0f
                ball.energy -= 35.0f
                collisions.add(ball.transform.position)
            }

        }
    }

    fun applyForce(force: Vector2f) {
        acceleration.add(Vector2f(force).div(mass))
    }

    fun applyAcceleration(acceleration: Vector2f) {
        this.acceleration = Vector2f(acceleration)
    }
}