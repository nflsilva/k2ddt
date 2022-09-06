package examples.shapesbatchrendering

import k2ddt.core.ExecutionContext
import k2ddt.core.ExecutionDelegate
import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.Color
import k2ddt.render.dto.Sprite
import k2ddt.render.dto.Text
import k2ddt.render.dto.Transform
import k2ddt.render.font.DefaultFont
import k2ddt.render.model.BitmapFont
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
    lateinit var font: BitmapFont

    private fun setupStress(){
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
    private fun setupLimits(){
        val nShapes = 8
        val size = 90f
        for (i in 0 until nShapes) {
            shapes.add(
                RandomShape(
                    10f,
                    size * i,
                    size,
                    colors[i % (colors.size - 1)],
                    2
                )
            )
        }
    }
    private fun setupOver(){
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
        font = DefaultFont()

    }

    override fun onUpdate(updateContext: UpdateContext) {
        shapes.forEach { it.tick(updateContext) }
    }

    override fun onFrame() {

        shapes.forEach { it.draw(executionContext) }

        val size = 128f * 2
        for(i in 0 until 5) {
            val t0 = Transform(
                i * size,
                i * size,
                0f,
                size,
                size,
                1 + i,
                false)
            executionContext.render(sprite, t0)
        }

        val text = Text("Hello World!", font)
        val tt = Transform(
            300f,
            300f,
            0f,
            55f,
            5f,
            0)
        executionContext.render(text, tt)

    }
}

fun main(args: Array<String>) {

    val delegate = Delegate()
    val ec = ExecutionContext(delegate = delegate)
    ec.start()

    println("Done!")
}

