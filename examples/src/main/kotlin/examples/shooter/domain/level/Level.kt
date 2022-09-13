package examples.shooter.domain.level

import examples.shooter.domain.levelObject.Drawable
import k2ddt.core.ExecutionContext
import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.Color

class Level(
    private val width: Int,
    private val height: Int,
    val blockSize: Float,
    private val map: MutableList<Drawable?>
) {

    fun hasCollisionAt(row: Int, column: Int): Drawable? {
        val d = map[((height - 1) - row) * width + column]
        if(d?.hasCollision == true) {
            return d
        }
        return null
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