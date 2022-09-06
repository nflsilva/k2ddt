package k2ddt.render.batchController

import k2ddt.render.batch.SpriteBatch
import k2ddt.render.shader.ShaderUniforms
import k2ddt.render.shader.SpriteShader
import org.lwjgl.opengl.GL11.*

class SpriteBatchController(shader: SpriteShader) : RenderEntityBatchController<SpriteBatch>(shader) {

    override fun draw(uniforms: ShaderUniforms) {
        shader.bind()
        for (batch in batches) {
            batch.bind()
            uniforms.textureSlots = batch.getNumberOfTextures()
            shader.updateUniforms(uniforms)
            glDrawElements(GL_TRIANGLES, batch.nIndexes, GL_UNSIGNED_INT, 0)
            batch.unbind()
        }
        shader.unbind()
    }

    override fun addNewBatch() {
        batches.add(SpriteBatch(DEFAULT_BATCH_SIZE))
    }

}