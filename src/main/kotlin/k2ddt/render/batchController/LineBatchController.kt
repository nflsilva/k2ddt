package k2ddt.render.batchController

import k2ddt.render.batch.LineBatch
import k2ddt.render.batch.SpriteBatch
import k2ddt.render.shader.LineShader
import k2ddt.render.shader.ShaderUniforms
import k2ddt.render.shader.SpriteShader
import org.lwjgl.opengl.GL11.*

class LineBatchController(shader: LineShader) : RenderEntityBatchController<LineBatch>(shader) {

    override fun draw(layer: Int, uniforms: ShaderUniforms) {
        val batches = batchesPerLayer[layer]
        shader.bind()
        for (batch in batches) {
            batch.bind()
            shader.updateUniforms(uniforms)
            glDrawElements(GL_LINES, batch.nIndexes, GL_UNSIGNED_INT, 0)
            batch.unbind()
        }
        shader.unbind()
    }

    override fun addNewBatch(layer: Int) {
        batchesPerLayer[layer].add(LineBatch(DEFAULT_BATCH_SIZE))
    }

}