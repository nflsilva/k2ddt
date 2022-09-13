package examples.shooter.domain.levelObject.scenery

import examples.shooter.domain.levelObject.Drawable
import k2ddt.core.ExecutionContext
import k2ddt.render.dto.Color
import k2ddt.render.dto.Shape

class Wall(
    positionX: Float,
    positionY: Float,
    size: Float
) : Drawable(positionX, positionY, 0f, size, size, 1, hasCollision = true) {

    private var shape = Shape(Shape.Type.SQUARE, Color(0f, 0f, 0f, 1f))

    override fun onFrame(context: ExecutionContext) {
        super.onFrame(context)
        context.render(shape, transform)
    }

    override fun onHit() {
        super.onHit()
        shape = Shape(Shape.Type.SQUARE, Color(1f))
    }

}