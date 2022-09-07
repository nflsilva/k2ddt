package k2ddt.render.batchController

import k2ddt.render.batch.BaseBatch
import k2ddt.render.shader.BaseShader
import k2ddt.render.shader.ShaderUniforms

abstract class RenderEntityBatchController<T : BaseBatch<*>>(
    protected val shader: BaseShader
) {

    companion object {
        const val DEFAULT_BATCH_SIZE: Int = 2
        const val DEFAULT_N_LAYERS: Int = 10
    }

    protected val batchesPerLayer: Array<MutableList<T>> = Array(DEFAULT_N_LAYERS) { mutableListOf() }
    private var suitableBatchIndex : Array<Int> = Array(DEFAULT_N_LAYERS) { -1 }

    fun getSuitableBatch(layer: Int): T {
        if (batchesPerLayer[layer].isEmpty() || batchesPerLayer[layer][suitableBatchIndex[layer]].isFull()) {
            suitableBatchIndex[layer] += 1
            if(suitableBatchIndex[layer] == batchesPerLayer[layer].size) {
                addNewBatch(layer)
            }
        }
        return batchesPerLayer[layer][suitableBatchIndex[layer]]
    }

    fun clearBatches() {
        for(i in suitableBatchIndex.indices){
            batchesPerLayer[i].forEach { it.clear() }
            suitableBatchIndex[i] = if(suitableBatchIndex[i] == -1) -1 else 0
        }
    }

    abstract fun draw(layer: Int, uniforms: ShaderUniforms)

    protected abstract fun addNewBatch(layer: Int)

}