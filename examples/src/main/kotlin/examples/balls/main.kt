package examples.balls

import examples.balls.`object`.Ball
import k2ddt.core.ExecutionContext
import k2ddt.core.ExecutionDelegate
import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.Color
import k2ddt.render.dto.Line
import k2ddt.render.dto.Transform
import k2ddt.tools.Log
import org.joml.Random
import org.joml.Vector2f
import ui.dto.InputStateData

private class Delegate : ExecutionDelegate() {

    private val balls = mutableListOf<Ball>()
    private val gravity = Vector2f(0f, -9.8f * 1000f)

    private val leftLimit = 300f
    private val rightLimit = 800f
    private val bottomLimit = 10f
    private val topLimit = 700f
    private var ticksSinceLastBall = 0
    private var ballSize = 10f
    private var lastPrint = 0.0

    override fun onStart() {

        executionContext.setBackgroundColor(Color(0.0f))

        addBall(300f, 320f)
        addBall(300f, 340f)
        addBall(300f, 360f)
        addBall(300f, 380f)
        addBall(300f, 400f)
        //balls[1].energy = 1000f
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
                ball0.heatUp(10f)

                if(ball0.x < leftLimit + ((rightLimit-leftLimit) / 2 + 100) &&
                    ball0.x > leftLimit + ((rightLimit-leftLimit) / 2 - 100)) {
                    ball0.heatUp(10f)
                }
            }

            applyLimits(ball0)
        }

        handleInput(updateContext.input)

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

        printProfiling()

    }

    private fun handleInput(input: InputStateData) {

        if (ticksSinceLastBall == 10) {
            if (input.isKeyPressed(InputStateData.KEY_B)) {
                addBall(input.mouseX.toFloat(), input.mouseY.toFloat())
            }
            if (input.isKeyPressed(InputStateData.KEY_SPACE)) {
                spawnBallCollection()
            }
            ticksSinceLastBall = 0
        }
        ticksSinceLastBall += 1

        if (input.scrollY != 0) {
            executionContext.zoomCamera(input.scrollY.toFloat() * 10f)
        }
        if (input.isKeyPressed(InputStateData.KEY_A)) {
            executionContext.moveCamera(-10f, 0f)
        } else if (input.isKeyPressed(InputStateData.KEY_D)) {
            executionContext.moveCamera(10f, 0f)
        }
        if (input.isKeyPressed(InputStateData.KEY_W)) {
            executionContext.moveCamera(0f, 10f)
        } else if (input.isKeyPressed(InputStateData.KEY_S)) {
            executionContext.moveCamera(0f, -10f)
        }
    }

    private fun addBall(createX: Float, createY: Float) {
        val b = Ball(createX, createY, ballSize, Color(1f))
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
