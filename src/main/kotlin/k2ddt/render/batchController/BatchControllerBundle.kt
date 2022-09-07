package k2ddt.render.batchController

import k2ddt.render.dto.*
import k2ddt.render.model.MultiSprite
import k2ddt.render.shader.ShaderBundle
import k2ddt.render.shader.ShaderUniforms
import org.joml.Vector2f

class BatchControllerBundle(shaderBundle: ShaderBundle) {

    private val spriteBatches: SpriteBatchController = SpriteBatchController(shaderBundle.spriteShader)
    private val shapeBatches: ShapeBatchController = ShapeBatchController(shaderBundle.shapeShader)
    private val particleBatches: ParticleBatchController = ParticleBatchController(shaderBundle.particleShader)

    fun addToSuitableBatch(data: Sprite, transform: Transform) {
        val suitableBatch = spriteBatches.getSuitableBatch(transform.layer)
        suitableBatch.addEntity(data, transform)
    }

    fun addToSuitableBatch(data: MultiSprite, transform: Transform) {

        val spriteSize = Vector2f(transform.scale)
            .div(Vector2f(data.columns.toFloat(), data.rows.toFloat()))

        var currentRowY = data.rows / -2f
        for (r in 0 until data.rows) {
            var currentRowX = data.columns / -2f
            for (c in 0 until data.columns) {
                val sprite = data.getSprite(r, c) ?: continue
                val t = Transform(
                    Vector2f(transform.position)
                        .add(
                            Vector2f(currentRowX, currentRowY)
                                .mul(spriteSize)
                                .mul(sprite.size)
                        ),
                    transform.rotation,
                    Vector2f(spriteSize).mul(sprite.size),
                    transform.layer
                )
                val suitableBatch = spriteBatches.getSuitableBatch(transform.layer)
                suitableBatch.addEntity(sprite.sprite, t)
                currentRowX += 1f
            }
            currentRowY += 1f
        }
    }

    fun addToSuitableBatch(data: Particle, transform: Transform) {
        val suitableBatch = particleBatches.getSuitableBatch(transform.layer)
        suitableBatch.addEntity(data, transform)
    }

    fun addToSuitableBatch(data: Shape, transform: Transform) {
        val suitableBatch = shapeBatches.getSuitableBatch(transform.layer)
        suitableBatch.addEntity(data, transform)
    }

    fun addToSuitableBatch(text: Text, transform: Transform) {
        val fullTextSprite = MultiSprite(1, text.data.length)
        val spriteSize = Vector2f(transform.scale)
            .div(Vector2f(fullTextSprite.columns.toFloat(), fullTextSprite.rows.toFloat()))

        for (c in 0 until text.data.length) {
            val charSprite = text.font.getCharacter(text.data[c])
            fullTextSprite.addSprite(0, c, spriteSize, charSprite)
        }
        addToSuitableBatch(fullTextSprite, transform)
    }

    fun draw(uniforms: ShaderUniforms) {
        for(layer in 0 until RenderEntityBatchController.DEFAULT_N_LAYERS){
            val layerToDraw = RenderEntityBatchController.DEFAULT_N_LAYERS - layer - 1
            spriteBatches.draw(layerToDraw, uniforms)
            shapeBatches.draw(layerToDraw, uniforms)
            particleBatches.draw(layerToDraw, uniforms)
        }
    }

    fun clearBatches() {
        spriteBatches.clearBatches()
        shapeBatches.clearBatches()
        particleBatches.clearBatches()
    }

}