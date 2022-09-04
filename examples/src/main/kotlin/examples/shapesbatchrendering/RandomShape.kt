package examples.shapesbatchrendering

import k2ddt.core.ExecutionContext
import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.Color
import k2ddt.render.dto.Particle
import k2ddt.render.dto.Shape
import k2ddt.render.dto.Transform
import org.joml.Random

class RandomShape(
    positionX: Float,
    positionY: Float,
    private val size: Float,
    color: Color,
    private val layer: Int
) {

    private val speed = 250f
    private var direction = if (Random().nextInt(2) == 0) -1 else 1
    private val transform = Transform(
        positionX,
        positionY,
        0f,
        size,
        size,
        layer,
        true
    )
    private val shape = Shape(Shape.Type.CIRCLE, color)

    fun tick(updateContext: UpdateContext) {
        transform.translate(speed * updateContext.elapsedTime * direction, 0f)
        transform.rotation += 0.05f

        if (transform.position.x <= 0f || transform.position.x >= 1280f) {
            direction *= -1
        }
    }

    fun draw(context: ExecutionContext) {
        context.render(shape, transform)
    }

}