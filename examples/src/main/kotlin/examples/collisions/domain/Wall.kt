package examples.collisions.domain

import k2ddt.core.ExecutionContext
import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.*
import k2ddt.render.font.DefaultFont
import k2ddt.ui.dto.InputStateData
import org.joml.Vector2f
import java.text.DecimalFormat

class Wall(
    centerX: Float,
    centerY: Float,
    val width: Float,
    val height: Float
) {

    private val color = Color(1.0f)
    val transform = Transform(
        centerX,
        centerY,
        0f,
        width,
        height,
        1
    )

    val x: Float
        get() = transform.position.x
    val y: Float
        get() = transform.position.y

    val left: Float
        get() = transform.position.x

    val right: Float
        get() = transform.position.x + width

    val top: Float
        get() = transform.position.y + height

    val bottom: Float
        get() = transform.position.y

    fun draw(context: ExecutionContext) {
        context.render(Shape(Shape.Type.SQUARE, color), transform)
    }
}