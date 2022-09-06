package k2ddt.render.batchController

import k2ddt.render.batch.BaseBatch
import k2ddt.render.shader.BaseShader
import k2ddt.render.shader.ShaderUniforms
import java.lang.Integer.max

abstract class RenderEntityBatchController<T : BaseBatch<*>>(
    protected val shader: BaseShader
) {

    protected val batches: MutableList<T> = mutableListOf()
    private var suitableBatchIndex : Int = -1

    companion object {
        const val DEFAULT_BATCH_SIZE: Int = 2
    }

    fun getSuitableBatch(): T {
        if (batches.isEmpty() || batches[suitableBatchIndex].isFull()) {
            suitableBatchIndex += 1
            if(suitableBatchIndex == batches.size) {
                addNewBatch()
            }
        }
        return batches[suitableBatchIndex]
    }

    fun clearBatches() {
        batches.forEach { it.clear() }
        suitableBatchIndex = if(suitableBatchIndex == -1) -1 else 0
    }

    abstract fun draw(uniforms: ShaderUniforms)

    protected abstract fun addNewBatch()

}