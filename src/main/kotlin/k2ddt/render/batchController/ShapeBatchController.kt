package k2ddt.render.batchController

import k2ddt.render.batch.ShapeBatch
import k2ddt.render.shader.ShaderUniforms
import k2ddt.render.shader.ShapeShader
import org.lwjgl.opengl.GL11.*

class ShapeBatchController(shader: ShapeShader) : RenderEntityBatchController<ShapeBatch>(shader) {

    override fun draw(uniforms: ShaderUniforms) {
        shader.bind()
        for (batch in batches) {
            batch.bind()
            shader.updateUniforms(uniforms)
            glDrawElements(GL_TRIANGLES, batch.nIndexes, GL_UNSIGNED_INT, 0)
            batch.unbind()
        }
        shader.unbind()
    }

    override fun addNewBatch() {
        batches.add(ShapeBatch(DEFAULT_BATCH_SIZE))
    }
}