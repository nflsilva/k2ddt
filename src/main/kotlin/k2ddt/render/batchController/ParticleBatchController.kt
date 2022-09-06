package k2ddt.render.batchController

import k2ddt.render.batch.ParticleBatch
import k2ddt.render.shader.ParticleShader
import k2ddt.render.shader.ShaderUniforms
import org.lwjgl.opengl.GL11.*

class ParticleBatchController(shader: ParticleShader) : RenderEntityBatchController<ParticleBatch>(shader) {
    override fun draw(uniforms: ShaderUniforms) {
        shader.bind()
        for (batch in batches) {
            batch.bind()
            shader.updateUniforms(uniforms)
            glDrawElements(GL_POINTS, batch.nIndexes, GL_UNSIGNED_INT, 0)
            batch.unbind()
        }
        shader.unbind()
    }

    override fun addNewBatch() {
        batches.add(ParticleBatch(DEFAULT_BATCH_SIZE))
    }
}