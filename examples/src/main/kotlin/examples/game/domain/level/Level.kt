package examples.game.domain.level

import examples.game.domain.levelObject.Drawable
import k2ddt.core.ExecutionContext
import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.Color

class Level(
    private val width: Int,
    private val height: Int,
    val blockSize: Float,
    private val map: List<Drawable?>
) {

    fun hasCollisionAt(row: Int, column: Int): Boolean {
        return map[((height - 1) - row) * width + column]?.hasCollision ?: false
    }

    fun update(context: UpdateContext) {

    }

    fun draw(context: ExecutionContext) {
        context.setBackgroundColor(Color(0.35f))
        for (row in 0 until height) {
            for (column in 0 until width) {
                map[row * width + column]?.onFrame(context)
            }
        }
    }

}