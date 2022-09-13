package examples.shooter.domain.levelObject.weapon

import examples.shooter.domain.GameUpdateContext
import examples.shooter.domain.level.Level
import examples.shooter.domain.levelObject.Drawable
import k2ddt.core.ExecutionContext
import k2ddt.render.dto.Color
import k2ddt.render.dto.Shape
import org.joml.Vector2f

class Bullet(parent: Drawable) : Drawable(parent, SIZE.x, SIZE.y, parent.transform.layer + 1) {

    companion object {
        val SIZE = Vector2f(128f, 1f)
    }

    private val velocity = Vector2f(direction).mul(10000f)
    private val body = Shape(Shape.Type.SQUARE, Color(0.6f, 0.6f, 0.0f, 0.6f))
    var toRemove: Boolean = false

    override fun onUpdate(context: GameUpdateContext) {
        transform.position.add(Vector2f(velocity).mul(context.baseContext.elapsedTime))

        handleBottomCollision(context.level)

    }

    override fun onFrame(context: ExecutionContext) {
        context.render(body, transform)
    }

    private fun handleBottomCollision(level: Level) {
        val rx = SIZE.x / 2f
        val ry = SIZE.y / 2f
        val nSizesX = (SIZE.x / level.blockSize).toInt()

        val bottom = transform.position.y - ry
        val bottomBlockY = (bottom / level.blockSize).toInt()

        val left = transform.position.x - rx
        val leftBlockX = (left / level.blockSize).toInt()

        for (n in 0 until nSizesX) {
            level.hasCollisionAt(bottomBlockY, leftBlockX + n)?.let {
                toRemove = true
                it.onHit()
            }
        }
    }

}