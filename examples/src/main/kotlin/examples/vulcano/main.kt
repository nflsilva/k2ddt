package examples.vulcano

import examples.collisions.pe
import examples.vulcano.domain.Wall
import examples.vulcano.domain.Ball
import k2ddt.core.ExecutionContext
import k2ddt.core.ExecutionDelegate
import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.Color
import k2ddt.render.dto.Text
import k2ddt.render.dto.Transform
import k2ddt.tools.Log
import org.joml.Random
import org.joml.Vector2f
import k2ddt.ui.dto.InputStateData

private class Delegate : ExecutionDelegate() {

    private val balls = mutableListOf<Ball>()
    private val walls = mutableListOf<Wall>()

    private val gravity = Vector2f(0f, -9.8f * 2000f)

    private val leftLimit = 100f
    private val rightLimit = 1000f
    private val bottomLimit = 10f
    private val topLimit = 700f
    private var ballSize = 10f
    private var lastPrint = 0.0
    private var heatLocation = 1f
    private val heatWindow = 100
    private val heatEnergy = 10f
    private val wallSize = 3f


    override fun onStart() {

        executionContext.setBackgroundColor(Color(0.3f))

        walls.add(Wall(leftLimit, bottomLimit, rightLimit - leftLimit + wallSize, wallSize * 2))
        walls.add(Wall(leftLimit, topLimit, rightLimit - leftLimit + wallSize, wallSize * 2))

        walls.add(Wall(leftLimit, bottomLimit, wallSize, topLimit-bottomLimit))
        walls.add(Wall(rightLimit, bottomLimit, wallSize, topLimit-bottomLimit))

        addBall(leftLimit + ballSize * 3f, 320f)
        addBall(leftLimit + ballSize * 3f, 340f)
        addBall(leftLimit + ballSize * 3f, 360f)

        addBall(leftLimit + ballSize * 7f, 320f)
        addBall(leftLimit + ballSize * 12f, 320f)
        //addBall(leftLimit + ballSize * 15f, 340f)
        //addBall(leftLimit + ballSize * 4f, 360f)
    }

    override fun onUpdate(updateContext: UpdateContext) {

        pe.onUpdate()

        for (b in balls) {

            b.applyForce(gravity)
            b.tick(updateContext)


            if(b.pos.y < (bottomLimit + 100f)) {
                b.heatUp(heatEnergy)

                val center = leftLimit + ((rightLimit-leftLimit) / 2) * heatLocation
                if(b.pos.x < center + heatWindow && b.pos.x > center - heatWindow) {
                    b.heatUp(heatEnergy)
                }
            }

            val cs = pe.getCollisions(b.uuid)
            for(c in cs) {
                balls.find { it.uuid == c.otherId }?.let { other ->
                    b.computeEnergyTransfer(other)
                }
            }

        }

        handleInput(updateContext.input)
        printProfiling()

    }

    override fun onFrame() {
        balls.forEach { it.draw(executionContext) }
        walls.forEach { it.draw() }

        executionContext.render(
            Text("${balls.size} balls", 32f, Color(1f)),
            Transform(rightLimit + 50f, topLimit - bottomLimit / 2, 0)
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
        var x = leftLimit + ballSize * 5f

        while (x < rightLimit) {
            addBall(x, 360f)
            x += ballSize * 4f
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

