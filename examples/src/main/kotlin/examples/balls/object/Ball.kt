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
import java.lang.Float.min

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
    private var velocity = Vector2f(0f)
    private var acceleration = Vector2f(0f)
    private var drawConnections = false

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
    var maxEnergy = 1000f
    var collisions: MutableList<Ball> = mutableListOf()

    fun tick(updateContext: UpdateContext) {
        collisions.clear()
        computePosition(updateContext.elapsedTime)
        handleInputs(updateContext.input)
        computeColor()
    }

    fun heatUp(amount: Float) {
        energy += amount
        if (energy >= maxEnergy && acceleration.y <= 0) {
            applyForce(Vector2f(0f, 300f * energy))
        }

    }

    fun draw(context: ExecutionContext) {
        context.render(Shape(Shape.Type.CIRCLE, color), transform)
        if (isSelected) {
            context.render(line, lineTransform)
        }

        if (drawConnections) {
            collisions.forEach {
                context.render(
                    Line(transform.position, it.transform.position, Color(0f, 1f, 0f, 1f)), Transform(0)
                )
            }
        }
    }

    fun collideWith(ball: Ball) {
        computeStaticResolution(ball)
        computeEnergyTransfer()
        //computeDynamicResolution(ball)
    }

    fun applyForce(force: Vector2f) {
        acceleration.add(Vector2f(force).div(mass))
    }

    fun applyAcceleration(acceleration: Vector2f) {
        this.acceleration = Vector2f(acceleration)
    }

    private fun computeColor() {
        color.r = energy * 25f / maxEnergy
        color.g = energy * 5f / maxEnergy
        color.b = energy * 1f / maxEnergy
    }

    private fun computeStaticResolution(ball: Ball) {
        val distanceVector = Vector2f(ball.x - x, ball.y - y)
        val distance = distanceVector.length()
        val delta = distance - (ball.radius + radius)

        if (delta < 0) {

            val deltaTranslate = distanceVector
                .normalize()
                .mul(delta * 0.5f)

            deltaTranslate.mul(0.5f)

            x += deltaTranslate.x
            y += deltaTranslate.y

            ball.x -= deltaTranslate.x
            ball.y -= deltaTranslate.y

            collisions.add(ball)
        }

    }

    private fun computeDynamicResolution(ball: Ball) {
        val normal = Vector2f(ball.x - x, ball.y - y).normalize()
        val tangent = Vector2f(normal.y, -normal.x)

        val dpTan0 = Vector2f(velocity).dot(tangent)
        val dpTan1 = Vector2f(ball.velocity).dot(tangent)

        velocity = Vector2f(tangent).mul(dpTan0)
        ball.velocity = Vector2f(tangent).mul(dpTan1)

    }

    private fun computeEnergyTransfer() {
        for(ball in collisions) {
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
    }

    private fun computePosition(deltaTime: Float) {
        val newPrevPosition = Vector2f(transform.position)
        val dtt = Vector2f(acceleration).mul(deltaTime * deltaTime)

        velocity = Vector2f(transform.position)
            .sub(previousPosition)
            .mul(0.99f)

        energy = max(
            0f,
            energy * 0.99f)

        transform.position.add(velocity).add(dtt)

        previousPosition = newPrevPosition

        acceleration.zero()

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

        if (input.isKeyPressed(InputStateData.KEY_C)) {
            drawConnections = !drawConnections
        }
    }

}