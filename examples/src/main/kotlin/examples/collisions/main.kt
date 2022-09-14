package examples.collisions

import examples.collisions.domain.Ball
import examples.collisions.domain.Wall
import k2ddt.core.ExecutionContext
import k2ddt.core.ExecutionDelegate
import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.Color
import k2ddt.tools.Log

private class Delegate : ExecutionDelegate() {

    private val balls = mutableListOf<Ball>()
    private val walls = mutableListOf<Wall>()

    private var lastPrint = 0.0

    override fun onStart() {

        executionContext.setBackgroundColor(Color(0.0f))

        balls.add(Ball(300f, 300f, 25f, Color(1f)))
        //balls.add(Ball(300f, 500f, 100f, Color(1f)))

        /*
        walls.add(Wall(0f, 0f, 20f, 720f))
        walls.add(Wall(1260f, 0f, 20f, 720f))
        walls.add(Wall(0f, 700f, 1280f, 20f))

        walls.add(Wall(0f, 0f, 1260f, 20f))
        walls.add(Wall(40f, 40f, 10f, 10f))
        walls.add(Wall(50f, 50f, 10f, 10f))
*/
    }

    override fun onUpdate(updateContext: UpdateContext) {

        for (b0i in 0 until balls.size) {

            val ball0 = balls[b0i]
            ball0.tick(updateContext)

            for (b1i in b0i + 1 until balls.size) {
                val ball1 = balls[b1i]
                ball0.collideWith(ball1)
            }

            for (wall in walls) {
                ball0.collideWith(wall)
            }
        }


        printProfiling()

    }

    override fun onFrame() {
        balls.forEach { it.draw(executionContext) }
        walls.forEach { it.draw(executionContext) }

        printProfiling()
    }


    private fun printProfiling() {
        val data = executionContext.getProfileData()

        val r = data.timeStamp - lastPrint

        if (r < 1) {
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

