package examples.collisions

import examples.collisions.domain.Ball
import k2ddt.core.ExecutionContext
import k2ddt.core.ExecutionDelegate
import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.Color
import k2ddt.tools.Log

private class Delegate : ExecutionDelegate() {

    private val balls = mutableListOf<Ball>()

    private var ballSize = 25f
    private var lastPrint = 0.0

    override fun onStart() {

        executionContext.setBackgroundColor(Color(0.0f))

        addBall(300f, 300f)
        addBall(300f, 500f)
    }

    override fun onUpdate(updateContext: UpdateContext) {

        for (b0i in 0 until balls.size) {

            val ball0 = balls[b0i]
            ball0.tick(updateContext)

            for (b1i in b0i + 1 until balls.size) {
                val ball1 = balls[b1i]
                ball0.collideWith(ball1)
            }
        }

    }

    override fun onFrame() {
        balls.forEach { it.draw(executionContext) }

        printProfiling()
    }

    private fun addBall(createX: Float, createY: Float) {
        val b = Ball(createX, createY, ballSize, Color(1f))
        balls.add(b)
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

