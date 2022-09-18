package examples.vulcano

import examples.vulcano.domain.Ball
import k2ddt.core.ExecutionContext
import k2ddt.core.ExecutionDelegate
import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.Color
import k2ddt.render.dto.Line
import k2ddt.render.dto.Text
import k2ddt.render.dto.Transform
import k2ddt.tools.Log
import org.joml.Random
import org.joml.Vector2f
import k2ddt.ui.dto.InputStateData

private class Delegate : ExecutionDelegate() {

    private val balls = mutableListOf<Ball>()
    private val gravity = Vector2f(0f, -9.8f * 1000f)

    private val leftLimit = 300f
    private val rightLimit = 800f
    private val bottomLimit = 10f
    private val topLimit = 700f
    private var ballSize = 10f
    private var lastPrint = 0.0
    private var heatLocation = 1f
    private val heatWindow = 100
    private val heatEnergy = 10f


    override fun onStart() {

        executionContext.setBackgroundColor(Color(0.0f))

        addBall(300f, 320f)
    }

    override fun onUpdate(updateContext: UpdateContext) {

        for (b0i in 0 until balls.size) {

            val ball0 = balls[b0i]
            ball0.applyForce(gravity)
            ball0.tick(updateContext)

            for (b1i in b0i + 1 until balls.size) {
                val ball1 = balls[b1i]
                ball0.collideWith(ball1)
            }

            if(ball0.y < (bottomLimit + ball0.radius * 8)) {
                ball0.heatUp(heatEnergy)

                val center = leftLimit + ((rightLimit-leftLimit) / 2) * heatLocation
                if(ball0.x < center + heatWindow && ball0.x > center - heatWindow) {
                    ball0.heatUp(heatEnergy)
                }
            }

            applyLimits(ball0)
        }

        handleInput(updateContext.input)
        printProfiling()

    }

    override fun onFrame() {
        balls.forEach { it.draw(executionContext) }
        executionContext.render(
            Line(leftLimit, bottomLimit, leftLimit, bottomLimit + topLimit, Color((1f))), Transform(1)
        )
        executionContext.render(
            Line(rightLimit, bottomLimit, rightLimit, bottomLimit + topLimit, Color((1f))), Transform(1)
        )
        executionContext.render(
            Line(leftLimit, bottomLimit, rightLimit, bottomLimit, Color((1f))), Transform(1)
        )
        executionContext.render(
            Line(leftLimit, topLimit, rightLimit, topLimit, Color((1f))), Transform(1)
        )

        executionContext.render(
            Text("${balls.size} balls", 32f, Color(1f)),
            Transform(rightLimit + 200f, topLimit - bottomLimit / 2, 0)
        )
    }

    private fun handleInput(input: InputStateData) {

        if (input.isKeyPressed(InputStateData.KEY_B)) {
            addBall(input.mouseX.toFloat(), input.mouseY.toFloat())
        }
        if (input.isKeyPressed(InputStateData.KEY_SPACE)) {
            spawnBallCollection()
        }

        if (input.scrollY != 0) {
            executionContext.zoomCamera(input.scrollY.toFloat() * 10f)
        }
        if (input.isKeyPressed(InputStateData.KEY_A)) {
            executionContext.moveCamera(-10f, 0f)
        } else if (input.isKeyPressed(InputStateData.KEY_D)) {
            executionContext.moveCamera(10f,
                0f)
        }
        if (input.isKeyPressed(InputStateData.KEY_W)) {
            executionContext.moveCamera(0f, 10f)
        } else if (input.isKeyPressed(InputStateData.KEY_S)) {
            executionContext.moveCamera(0f, -10f)
        }

        if (input.isKeyPressed(InputStateData.KEY_J)) {
            heatLocation -= 0.1f
        } else if (input.isKeyPressed(InputStateData.KEY_K)) {
            heatLocation += 0.1f
        }
    }

    private fun addBall(createX: Float, createY: Float) {
        val b = Ball(createX, createY, ballSize)
        balls.add(b)
    }

    private fun spawnBallCollection() {
        var x = leftLimit + ballSize

        while (x < rightLimit) {
            addBall(x, 400f * Random().nextFloat())
            x += ballSize
        }
    }

    private fun applyLimits(ball: Ball) {
        if (ball.x + ball.radius > rightLimit) {
            ball.x = rightLimit - ball.radius
        } else if (ball.x - ball.radius < leftLimit) {
            ball.x = leftLimit + ball.radius
        }

        if (ball.y - ball.radius < bottomLimit) {
            ball.y = bottomLimit + ball.radius
        } else if (ball.y + ball.radius > topLimit) {
            ball.y = topLimit - ball.radius
        }
    }

    private fun printProfiling() {
        val data = executionContext.getProfileData()

        val r = data.timeStamp - lastPrint

        if (r < 5) {
            return
        }

        lastPrint = data.timeStamp

        var printedActivities = ""
        data.activities.forEach { printedActivities += "${it.first}: ${it.second}\n" }

        Log.d(
            "---\n" +
                    "${data.timeStamp} - FPS: ${data.framesPerSecond}\n" +
                    printedActivities
        )
    }
}

fun main(args: Array<String>) {

    val delegate = Delegate()
    val ec = ExecutionContext(delegate = delegate)
    ec.start()

    println("Done!")
}

