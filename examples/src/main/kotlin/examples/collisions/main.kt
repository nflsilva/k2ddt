package examples.collisions

import examples.collisions.domain.Ball
import examples.collisions.domain.Wall
import examples.physics.PhysicsEngine
import k2ddt.core.ExecutionContext
import k2ddt.core.ExecutionDelegate
import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.Color
import k2ddt.render.dto.Line
import k2ddt.render.dto.Transform
import k2ddt.tools.Log
import java.text.DecimalFormat

val physics = PhysicsEngine()

private class Delegate : ExecutionDelegate() {

    private val balls = mutableListOf<Ball>()
    private val walls = mutableListOf<Wall>()

    private var lastPrint = 0.0

    override fun onStart() {

        executionContext.setBackgroundColor(Color(0.0f))

        balls.add(Ball(600f, 500f, 200f, Color(1f)))
        balls.add(Ball(300f, 500f, 200f, Color(1f)))
        balls.add(Ball(800f, 500f, 100f, Color(1f)))

        //walls.add(Wall(1260f, 0f, 20f, 720f))
        //walls.add(Wall(0f, 700f, 1280f, 20f))
        //walls.add(Wall(0f, 0f, 1260f, 20f))

    }

    override fun onUpdate(updateContext: UpdateContext) {

        balls.forEach {
            it.tick(updateContext)
        }

        /*
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
        }*/

        printProfiling()

        physics.onUpdate()

    }

    override fun onFrame() {

        for (c in 0 until 1280 step physics.collisionMap.horizontalChunkSize) {
            executionContext.render(
                Line(
                    c.toFloat(),
                    0f,
                    c.toFloat(),
                    720f,
                    Color(1f, 1f, 1f, 0.5f)
                ),
                Transform(2)
            )
        }

        for (r in 0 until 720 step physics.collisionMap.verticalChunkSize) {
            executionContext.render(
                Line(
                    0f,
                    r.toFloat(),
                    1280f,
                    r.toFloat(),
                    Color(1f, 1f, 1f, 0.5f)
                ),
                Transform(2)
            )
        }

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

        val dec = DecimalFormat("##.######")
        var printedActivities = ""
        data.activities.forEach { printedActivities += "${it.first}: ${it.second}\n" }

        Log.d(
            "---\n" +
                    "${dec.format(data.timeStamp)}s - FPS: ${data.framesPerSecond}\n" +
                    printedActivities
        )
        physics.collisionMap.debug()
    }
}

fun main(args: Array<String>) {

    val delegate = Delegate()
    val ec = ExecutionContext(delegate = delegate)
    ec.start()

    println("Done!")
}

