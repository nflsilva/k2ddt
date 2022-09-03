package examples.shapesbatchrendering

import k2ddt.core.ExecutionContext
import k2ddt.core.ExecutionDelegate
import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.Color
import org.joml.Random

private class Delegate : ExecutionDelegate() {

    private var shapes: MutableList<RandomShape> = mutableListOf()
    private var done = false
    private val colors = listOf(
        Color(1f, 0f, 0f, 1f),
        Color(0f, 1f, 0f, 1f),
        Color(0f, 0f, 1f, 1f),
        Color(1f, 1f, 0f, 1f),
        Color(0f, 1f, 1f, 1f),
        Color(1f, 0f, 1f, 1f),
        Color(1f, 1f, 1f, 1f),
        Color(1f, 1f, 1f, 1f),
    )

    private fun drawStress(){
        val nShapes = 16e6.toInt()
        val size = 1f
        for (i in 0 until nShapes) {
            shapes.add(
                RandomShape(
                    Random().nextFloat() * 1280f,
                    Random().nextFloat() * 720f,
                    size,
                    colors[i % colors.size],
                    0
                )
            )
        }
    }
    private fun drawLimits(){
        val nShapes = 8
        val size = 90f
        for (i in 0 until nShapes) {
            shapes.add(
                RandomShape(
                    0f,
                    size * i,
                    size,
                    colors[i % colors.size],
                    1
                )
            )
        }
    }
    private fun drawOver(){
        shapes.add(
            RandomShape(
                45f,
                45f,
                90f,
                colors[4],
                999
            )
        )
    }

    override fun onStart() {
        //drawStress()
        drawOver()
        drawLimits()

    }

    override fun onUpdate(updateContext: UpdateContext) {
        shapes.forEach { it.tick(updateContext) }
    }

    override fun onFrame() {
        if(!done) {
            shapes.forEach { it.draw(executionContext) }
            done = true
        }

    }
}

fun main(args: Array<String>) {

    val delegate = Delegate()
    val ec = ExecutionContext(delegate = delegate)
    ec.start()

    println("Done!")
}

