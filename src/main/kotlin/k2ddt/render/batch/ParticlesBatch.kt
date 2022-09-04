package k2ddt.render.batch

import k2ddt.render.dto.Particle
import k2ddt.render.dto.Transform

class ParticlesBatch(maxEntities: Int) :
    BaseBatch(maxEntities, 1, 1) {

    companion object {
        const val POSITION_INDEX = 0
        const val SIZE_INDEX = 1
        const val TYPE_INDEX = 2
        const val COLOR_INDEX = 3
        const val LAYER_INDEX = 4
        const val CENTERED_INDEX = 5
    }

    init {
        addFloatAttributeBuffer(POSITION_INDEX, 2)
        addFloatAttributeBuffer(SIZE_INDEX, 1)
        addIntAttributeBuffer(TYPE_INDEX, 1)
        addFloatAttributeBuffer(COLOR_INDEX, 4)
        addIntAttributeBuffer(LAYER_INDEX, 1)
        addIntAttributeBuffer(CENTERED_INDEX, 1)
    }

    fun addParticle(particle: Particle, transform: Transform) {
        addAttributeData(
            POSITION_INDEX,
            transform.position.x + particle.size / 2,
            transform.position.y + particle.size / 2
        )
        addAttributeData(LAYER_INDEX, transform.layer)
        addAttributeData(SIZE_INDEX, particle.size)
        addAttributeData(TYPE_INDEX, particle.type.value)
        addAttributeData(COLOR_INDEX, particle.color.r, particle.color.g, particle.color.b, particle.color.a)
        addAttributeData(CENTERED_INDEX, if(transform.centered) 1 else 0)
        addIndexData(nEntities)
        nEntities += 1
    }

}