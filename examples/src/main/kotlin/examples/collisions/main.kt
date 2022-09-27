package examples.collisions

import examples.collisions.domain.Ball
import examples.collisions.domain.Wall
import k2ddt.core.ExecutionContext
import k2ddt.core.ExecutionDelegate
import k2ddt.core.dto.UpdateContext
import k2ddt.physics.PhysicsEngine
import k2ddt.render.dto.Color

val pe = PhysicsEngine()

private class Delegate : ExecutionDelegate() {

    private val balls = mutableListOf<Ball>()
    private val walls = mutableListOf<Wall>()

    private var lastPrint = 0.0

    override fun onStart() {

        //executionContext.setBackgroundColor(Color(0.0f))

        //balls.add(Ball(600f, 500f, 200f, Color(1f), executionContext))
        balls.add(Ball(30f, 40f, 50f, Color(1f)))
        balls.add(Ball(600f, 500f, 100f, Color(1f)))

        //walls.add(Wall(100f, 0f, 1080f, 10f))
        walls.add(Wall(1280f / 2, 200f, 500f, 50f))

        val wallWith = 5f
        walls.add(Wall(0f, 720f - wallWith, 1280f, wallWith))
        walls.add(Wall(1280f - wallWith, 0f, wallWith, 720f))
        walls.add(Wall(0f, 0f, wallWith, 720f))
        walls.add(Wall(0f, 0f, 1280f, wallWith))
    }

    override fun onUpdate(updateContext: UpdateContext) {

        pe.onUpdate()

        balls.forEach { it.tick(updateContext) }
        walls.forEach { it.tick(updateContext) }

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

    }

    override fun onFrame() {

        balls.forEach { it.draw() }
        walls.forEach { it.draw() }

        printProfiling()
    }

    private fun printProfiling() {
        /*
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
        )*/
    }
}

fun main(args: Array<String>) {

    val delegate = Delegate()
    val ec = ExecutionContext.getInstance()
    ec.setup(delegate = delegate)
    ec.start()

    println("Done!")
}