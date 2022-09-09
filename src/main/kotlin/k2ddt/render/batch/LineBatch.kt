package k2ddt.render.batch

import k2ddt.render.dto.Line
import k2ddt.render.dto.Shape
import k2ddt.render.dto.Transform

class LineBatch(maxShapes: Int) :
    BaseBatch<Line>(maxShapes, 2, 2) {

    companion object {
        const val POSITION_INDEX = 0
        const val COLOR_INDEX = 1
        const val TRANSLATION_INDEX = 2
        const val ROTATION_INDEX = 3
        const val SCALE_INDEX = 4
    }

    init {
        addFloatAttributeBuffer(POSITION_INDEX, 2)
        addFloatAttributeBuffer(COLOR_INDEX, 4)
        addFloatAttributeBuffer(TRANSLATION_INDEX, 2)
        addFloatAttributeBuffer(ROTATION_INDEX, 1)
        addFloatAttributeBuffer(SCALE_INDEX, 2)
    }

    override fun addEntity(entity: Line, transform: Transform) {
        if (isFull()) return

        addAttributeData(POSITION_INDEX,
            entity.startPoint.x,
            entity.startPoint.y,
            entity.endPoint.x,
            entity.endPoint.y,
            perVertex = false
        )
        addAttributeData(COLOR_INDEX, entity.color.r, entity.color.g, entity.color.b, entity.color.a)
        addAttributeData(TRANSLATION_INDEX, transform.position.x, transform.position.y)
        addAttributeData(ROTATION_INDEX, transform.rotation)
        addAttributeData(SCALE_INDEX, transform.scale.x, transform.scale.y)

        val indexOffset = nEntities * 2
        addIndexData(
            0 + indexOffset,
            1 + indexOffset,
        )
        nEntities += 1
    }

}