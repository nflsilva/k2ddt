package examples.mashup

import k2ddt.core.*
import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.*
import k2ddt.ui.dto.InputStateData
import org.joml.Random

private class Delegate : ExecutionDelegate() {

    private var shapes: MutableList<RandomShape> = mutableListOf()
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

    lateinit var sprite: Sprite
    var xx = 0f

    private fun setupStress() {
        val nShapes = 16e6.toInt()
        val size = 1f
        for (i in 0 until nShapes) {
            shapes.add(
                RandomShape(
                    Random().nextFloat() * 1280f,
                    Random().nextFloat() * 720f,
                    size,
                    colors[i % colors.size],
                    800
                )
            )
        }
    }

    private fun setupLimits() {
        val nShapes = 16
        val size = 90f
        for (i in 0 until nShapes) {
            shapes.add(
                RandomShape(
                    10f * Random().nextFloat() * 100f,
                    size / 2f * i,
                    size,
                    colors[i % (colors.size - 1)],
                    2
                )
            )
        }
    }

    private fun setupOver() {
        shapes.add(
            RandomShape(
                45f,
                45f,
                90f,
                colors[4],
                0
            )
        )
    }

    override fun onStart() {
        //setupStress()
        setupOver()
        setupLimits()

        sprite = Sprite("/sprite/cube.png")
    }

    override fun onUpdate(updateContext: UpdateContext) {
        shapes.forEach { it.tick(updateContext) }

        if (updateContext.input.scrollY != 0) {
            zoomCamera(updateContext.input.scrollY.toFloat() * 10f)
        }
        if (updateContext.input.isKeyPressed(InputStateData.KEY_A)) {
            moveCamera(-10f, 0f)
        } else if (updateContext.input.isKeyPressed(InputStateData.KEY_D)) {
            moveCamera(10f, 0f)
        }
        if (updateContext.input.isKeyPressed(InputStateData.KEY_W)) {
            moveCamera(0f, 10f)
        } else if (updateContext.input.isKeyPressed(InputStateData.KEY_S)) {
            moveCamera(0f, -10f)
        }
    }

    override fun onFrame() {

        for (i in shapes.indices) {
            if (i > 0 && i < shapes.size) {

                val s0 = shapes[i - 1]
                val s1 = shapes[i]
                val line = Line(
                    s0.position,
                    s1.position,
                    Color(1f)
                )
                render(line, Transform(5))
            }
        }
        shapes.forEach { it.draw() }

        val ds = 5
        val size = 128f * 2
        for (i in 0 until ds) {
            val t0 = Transform(
                i * size,
                i * size,
                0f,
                size,
                size,
                1 + i,
                true
            )
            render(sprite, t0)
        }

        val text = Text("Hello World!", 32f, Color(1f, 0f, 0f, 1f))
        render(
            text, Transform(250f, 300f, 0)
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

