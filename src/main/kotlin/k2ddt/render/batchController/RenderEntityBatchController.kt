package k2ddt.render.batchController

import k2ddt.render.batch.BaseBatch
import k2ddt.render.shader.BaseShader
import k2ddt.render.shader.ShaderUniforms

abstract class RenderEntityBatchController<T : BaseBatch<*>>(
    protected val shader: BaseShader
) {

    protected val batchesPerLayer: MutableList<T> = mutableListOf()
    private var suitableBatchIndex : Int = -1

    companion object {
        const val DEFAULT_BATCH_SIZE: Int = 2
    }

    fun getSuitableBatch(): T {
        if (batchesPerLayer.isEmpty() || batchesPerLayer[suitableBatchIndex].isFull()) {
            suitableBatchIndex += 1
            if(suitableBatchIndex == batchesPerLayer.size) {
                addNewBatch()
            }
        }
        return batchesPerLayer[suitableBatchIndex]
    }

    fun clearBatches() {
        batchesPerLayer.forEach { it.clear() }
        suitableBatchIndex = if(suitableBatchIndex == -1) -1 else 0
    }

    abstract fun draw(uniforms: ShaderUniforms)

    protected abstract fun addNewBatch()

}