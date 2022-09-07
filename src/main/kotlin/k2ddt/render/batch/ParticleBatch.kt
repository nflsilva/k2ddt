package k2ddt.render.batch

import k2ddt.render.dto.Particle
import k2ddt.render.dto.Transform

class ParticleBatch(maxEntities: Int) :
    BaseBatch<Particle>(maxEntities, 1, 1) {

    companion object {
        const val POSITION_INDEX = 0
        const val SIZE_INDEX = 1
        const val TYPE_INDEX = 2
        const val COLOR_INDEX = 3
    }

    init {
        addFloatAttributeBuffer(POSITION_INDEX, 2)
        addFloatAttributeBuffer(SIZE_INDEX, 1)
        addIntAttributeBuffer(TYPE_INDEX, 1)
        addFloatAttributeBuffer(COLOR_INDEX, 4)
    }

    override fun addEntity(entity: Particle, transform: Transform) {
        if(isFull()) return

        addAttributeData(POSITION_INDEX, transform.position.x, transform.position.y)
        addAttributeData(SIZE_INDEX, entity.size)
        addAttributeData(TYPE_INDEX, entity.type.value)
        addAttributeData(COLOR_INDEX, entity.color.r, entity.color.g, entity.color.b, entity.color.a)
        addIndexData(nEntities)
        nEntities += 1
    }
}