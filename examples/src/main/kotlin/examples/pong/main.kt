package examples.pong

import examples.pong.entity.Ball
import examples.pong.entity.Block
import examples.pong.entity.Platform
import examples.pong.entity.Wall
import k2ddt.core.ExecutionContext
import k2ddt.core.ExecutionDelegate
import k2ddt.core.dto.UpdateContext
import k2ddt.physics.PhysicsEngine
import k2ddt.render.dto.Color
import k2ddt.render.dto.Text
import k2ddt.render.dto.Transform
import k2ddt.tools.Log
import org.joml.Random
import org.joml.Vector2f

val pe = PhysicsEngine()

private class Delegate : ExecutionDelegate() {

    private var score = 0
    private val walls = mutableListOf<Wall>()
    private val blocks = mutableListOf<Block>()
    private lateinit var ball: Ball
    private lateinit var platform: Platform

    private val wallWith = 5f
    private var lastPrint = 0.0

    private val rightLimit = 1000f
    private val topLimit = 720f
    private val leftLimit = 0f
    private val bottomLimit = 50f
    private var isGameOver = false

    override fun onStart() {

        executionContext.setBackgroundColor(Color(0.0f))

        ball = Ball((rightLimit - leftLimit) * Random().nextFloat(), bottomLimit + 50f, Color(1f))
        ball.applyForce(Vector2f(0f, 8000f))

        walls.add(Wall(leftLimit, topLimit - wallWith, rightLimit - leftLimit, wallWith))
        walls.add(Wall(leftLimit, bottomLimit, wallWith, topLimit - bottomLimit))
        walls.add(Wall(rightLimit - wallWith, bottomLimit, wallWith, topLimit - bottomLimit))

        for (x in (leftLimit.toInt() + 150) until (rightLimit.toInt() - 150) step 150) {
            for (y in (bottomLimit.toInt() + 200) until topLimit.toInt() step 100) {
                blocks.add(Block(x.toFloat(), y.toFloat(), 100f, 50f))
            }
        }

        platform = Platform(
            (rightLimit - leftLimit) / 2,
            bottomLimit,
            100f,
            20f,
            leftLimit,
            rightLimit
        )
    }

    override fun onUpdate(updateContext: UpdateContext) {
        pe.onUpdate()

        if(isGameOver) return

        platform.tick(updateContext)

        blocks.forEach { if (it.isDead) { it.remove(); score += 1 } }
        blocks.removeIf { it.isDead }

        if(ball.position.y < 0f) {
            isGameOver = true
        }

        printProfiling()
    }

    override fun onFrame() {
        if(isGameOver) {
            executionContext.render(Text("Game Over!", 64f, Color(1f)),
                Transform((rightLimit-leftLimit) / 2f, (topLimit-bottomLimit) / 2f, 0)
            )
            executionContext.render(Text(" Final score: $score", 64f, Color(1f)),
                Transform((rightLimit-leftLimit) / 2f, (topLimit-bottomLimit) / 2f - 64f, 0)
            )
        }
        else {
            ball.draw()
            walls.forEach { it.draw() }
            blocks.forEach { it.draw() }
            platform.draw()
            executionContext.render(Text("Score: $score", 32f, Color(1f)),
                Transform(rightLimit + 130f, topLimit - 32f, 0)
            )
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
    val ec = ExecutionContext.getInstance()
    ec.setup(delegate = delegate)
    ec.start()

    println("Done!")
}