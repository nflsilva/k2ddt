package k2ddt.render.batch

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
        const val LAYER_INDEX = 6
        const val CENTERED_INDEX = 7
    }

    init {
        addFloatAttributeBuffer(POSITION_INDEX, 2)
        addFloatAttributeBuffer(TRANSLATION_INDEX, 2)
        addFloatAttributeBuffer(ROTATION_INDEX, 1)
        addFloatAttributeBuffer(SCALE_INDEX, 2)
        addIntAttributeBuffer(TYPE_INDEX, 1)
        addFloatAttributeBuffer(COLOR_INDEX, 4)
        addIntAttributeBuffer(LAYER_INDEX, 1)
        addIntAttributeBuffer(CENTERED_INDEX, 1)
    }

    fun addShape(shape: Shape, transform: Transform) {

        val quad = getQuad(transform.centered)
        addAttributeData(
            POSITION_INDEX,
            quad.tl.x, quad.tl.y,
            quad.bl.x, quad.bl.y,
            quad.br.x, quad.br.y,
            quad.tr.x, quad.tr.y,
            perVertex = false
        )
        addAttributeData(LAYER_INDEX, transform.layer)
        addAttributeData(TRANSLATION_INDEX, transform.position.x, transform.position.y)
        addAttributeData(ROTATION_INDEX, transform.rotation)
        addAttributeData(SCALE_INDEX, transform.scale.x, transform.scale.y)
        addAttributeData(TYPE_INDEX, shape.type.value)
        addAttributeData(COLOR_INDEX, shape.color.r, shape.color.g, shape.color.b, shape.color.a)
        addAttributeData(CENTERED_INDEX, if (transform.centered) 0 else 1)

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