package examples.game.domain.levelObject.weapon

import examples.game.domain.GameUpdateContext
import examples.game.domain.levelObject.Drawable
import k2ddt.core.ExecutionContext
import k2ddt.render.dto.Color
import k2ddt.render.dto.Shape
import org.joml.Vector2f

class Bullet(parent: Drawable) : Drawable(parent, 128f, 1f, parent.transform.layer + 1) {

    private val velocity = Vector2f(direction).mul(10000f)
    private val body = Shape(Shape.Type.SQUARE, Color(0.6f, 0.6f, 0.0f, 0.6f))

    override fun onUpdate(context: GameUpdateContext) {
        transform.position.add(Vector2f(velocity).mul(context.baseContext.elapsedTime))
    }

    override fun onFrame(context: ExecutionContext) {
        context.render(body, transform)
    }
}