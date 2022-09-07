package k2ddt.render.batch

import org.joml.Vector2f
import k2ddt.render.dto.Shape
import k2ddt.render.dto.Transform

class ShapesBatch(maxShapes: Int) :
    BaseBatch(maxShapes, 4, 6) {

    companion object {
        const val POSITION_INDEX = 0
        const val TRANSLATION_INDEX = 1
        const val ROTATION_INDEX = 2
        const val SCALE_INDEX = 3
        const val TYPE_INDEX = 4
        const val COLOR_INDEX = 5
    }

    init {
        addFloatAttributeBuffer(POSITION_INDEX, 2)
        addFloatAttributeBuffer(TRANSLATION_INDEX, 2)
        addFloatAttributeBuffer(ROTATION_INDEX, 1)
        addFloatAttributeBuffer(SCALE_INDEX, 2)
        addIntAttributeBuffer(TYPE_INDEX, 1)
        addFloatAttributeBuffer(COLOR_INDEX, 4)
    }

    fun addShape(shape: Shape, transform: Transform) {

        val tl = Vector2f(0f, 1f)
        val bl = Vector2f(0f, 0f)
        val br = Vector2f(1f, 0f)
        val tr = Vector2f(1f, 1f)

        addAttributeData(
            POSITION_INDEX,
            tl.x, tl.y,
            bl.x, bl.y,
            br.x, br.y,
            tr.x, tr.y,
            perVertex = false
        )

        addAttributeData(TRANSLATION_INDEX, transform.position.x, transform.position.y)
        addAttributeData(ROTATION_INDEX, transform.rotation)
        addAttributeData(SCALE_INDEX, transform.scale.x, transform.scale.y)
        addAttributeData(TYPE_INDEX, shape.type.value)
        addAttributeData(COLOR_INDEX, shape.color.r, shape.color.g, shape.color.b, shape.color.a)

        val indexOffset = nEntities * 4
        addIndexData(
            0 + indexOffset,
            1 + indexOffset,
            2 + indexOffset,
            2 + indexOffset,
            3 + indexOffset,
            0 + indexOffset
        )
        nEntities += 1
    }

}