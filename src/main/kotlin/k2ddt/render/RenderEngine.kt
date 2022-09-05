package k2ddt.render

import k2ddt.render.batch.ParticlesBatch
import k2ddt.render.batch.ShapesBatch
import k2ddt.render.batch.SpriteBatch
import k2ddt.render.dto.*
import k2ddt.render.model.MultiSprite
import k2ddt.render.shader.*
import org.joml.Matrix4f
import org.joml.Vector2f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL30.glClearColor

class RenderEngine(private val screenWidth: Int, private val screenHeight: Int) {

    private var backgroundColor: Color = Color(0.0f)

    private lateinit var spriteBatches: MutableList<SpriteBatch>
    private lateinit var spriteShader: SpriteShader

    private lateinit var particleBatches: MutableList<ParticlesBatch>
    private lateinit var particleShader: ParticleShader

    private lateinit var shapeBatches: MutableList<ShapesBatch>
    private lateinit var shapeShader: ShapeShader

    private var maxTextureSlots: Int = -1
    private var suitableSpriteBatch: Int = 0

    private var zoom = 0.0f

    private val bottom = 0.0f
    private val left = 0.0f
    private val top = screenHeight * (1.0f - zoom)
    private val right = screenWidth * (1.0f - zoom)

    companion object {
        const val DEFAULT_BATCH_SIZE: Int = 100000
        const val DEFAULT_SCREEN_RENDER_MARGINS: Int = 100
    }

    fun onStart() {

        val mtsb = BufferUtils.createIntBuffer(1)
        glGetIntegerv(GL30.GL_MAX_TEXTURE_IMAGE_UNITS, mtsb)
        maxTextureSlots = mtsb.get()

        spriteBatches = mutableListOf(SpriteBatch(DEFAULT_BATCH_SIZE, maxTextureSlots))
        spriteShader = SpriteShader()

        particleShader = ParticleShader()
        particleBatches = mutableListOf()

        shapeShader = ShapeShader()
        shapeBatches = mutableListOf()

        glEnable(GL_DEPTH_TEST)
        glDepthFunc(GL_LESS)
    }

    fun onFrame() {

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a)

        glViewport(0, 0, screenWidth, screenHeight)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
        draw()
        clearBatches()
    }

    fun onUpdate() {

    }

    fun onCleanUp() {
        for (batch in spriteBatches) {
            batch.cleanUp()
        }
    }

    fun setBackgroundColor(color: Color) {
        backgroundColor = color
    }

    fun render(sprite: Sprite, transform: Transform) {
        if (isVisible(transform, transform.scale)) {
            addToSuitableSpriteBatch(sprite, transform)
        }
    }

    fun render(particle: Particle, transform: Transform) {
        if (isVisible(transform, Vector2f(particle.size))) {
            addToSuitableParticleBatch(particle, transform)
        }
    }

    fun render(shape: Shape, transform: Transform) {
        if (isVisible(transform, transform.scale)) {
            addToSuitableShapeBatch(shape, transform)
        }
    }

    fun render(multiSprite: MultiSprite, transform: Transform) {
        if (isVisible(transform, transform.scale)) {
            addToSuitableSpriteBatch(multiSprite, transform)
        }
    }

    fun render(text: Text, transform: Transform) {
        val fullTextSprite = MultiSprite(1, text.data.length)
        val spriteSize = Vector2f(transform.scale)
            .div(Vector2f(fullTextSprite.columns.toFloat(), fullTextSprite.rows.toFloat()))

        for (c in 0 until text.data.length) {
            val charSprite = text.font.getCharacter(text.data[c])
            fullTextSprite.addSprite(0, c, spriteSize, charSprite)
        }
        render(fullTextSprite, transform)
    }

    fun zoomIn(zoom: Float) {

    }

    private fun isVisible(transform: Transform, size: Vector2f): Boolean {
        return transform.position.x > left - DEFAULT_SCREEN_RENDER_MARGINS &&
                transform.position.x + size.x < right + DEFAULT_SCREEN_RENDER_MARGINS &&
                transform.position.y < top + DEFAULT_SCREEN_RENDER_MARGINS &&
                transform.position.y + size.y > bottom - DEFAULT_SCREEN_RENDER_MARGINS
    }

    private fun incrementSuitableSpriteBatch() {
        suitableSpriteBatch += 1

        if (suitableSpriteBatch == spriteBatches.size) {
            spriteBatches.add(SpriteBatch(DEFAULT_BATCH_SIZE, maxTextureSlots))
        }
    }

    private fun addToSuitableSpriteBatch(data: Sprite, transform: Transform) {
        val suitableBatch = spriteBatches[suitableSpriteBatch]
        val isFull = suitableBatch.addSprite(data, transform)
        if (isFull) {
            incrementSuitableSpriteBatch()
        }
    }

    private fun addToSuitableSpriteBatch(data: MultiSprite, transform: Transform) {
        val suitableBatch = spriteBatches[suitableSpriteBatch]

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
                val isFull = suitableBatch.addSprite(sprite.sprite, t)
                if (isFull) {
                    incrementSuitableSpriteBatch()
                }
                currentRowX += 1f
            }
            currentRowY += 1f
        }
    }

    private fun addToSuitableParticleBatch(data: Particle, transform: Transform) {
        var suitableBatch: ParticlesBatch? = null
        for (batch in particleBatches) {
            if (batch.isFull()) {
                continue
            }
            suitableBatch = batch
            break
        }

        if (suitableBatch == null) {
            suitableBatch = ParticlesBatch(DEFAULT_BATCH_SIZE)
            particleBatches.add(suitableBatch)
        }

        suitableBatch.addParticle(data, transform)
    }

    private fun addToSuitableShapeBatch(data: Shape, transform: Transform) {
        var suitableBatch: ShapesBatch? = null
        for (batch in shapeBatches) {
            if (batch.isFull()) {
                continue
            }
            suitableBatch = batch
            break
        }

        if (suitableBatch == null) {
            suitableBatch = ShapesBatch(DEFAULT_BATCH_SIZE)
            shapeBatches.add(suitableBatch)
        }

        suitableBatch.addShape(data, transform)
    }

    private fun prepareShader(shader: BaseShader, uniformData: ShaderUniforms) {
        shader.bind()
        shader.updateUniforms(uniformData)
    }

    private fun draw() {
        val projectionMatrix: Matrix4f = Matrix4f()
            .setOrtho(left, right, bottom, top, 0f, 1000f)
        drawParticles(projectionMatrix)
        drawSprites(projectionMatrix)
        drawShapes(projectionMatrix)

        /*
        Log.d("Sprite batches: ${spriteBatches.size} " +
                "Particle batches: ${particleBatches.size} " +
                "Shape batches: ${shapeBatches.size}"
        )
        println(shapeBatches.firstOrNull()?.nEntities)
         */
    }

    private fun clearBatches() {
        spriteBatches.forEach { it.clear() }
        suitableSpriteBatch = 0

        particleBatches.forEach { it.clear() }
        shapeBatches.forEach { it.clear() }
    }

    private fun drawSprites(projectionMatrix: Matrix4f) {
        for (batch in spriteBatches) {
            batch.bind()
            prepareShader(
                spriteShader,
                ShaderUniforms(
                    projectionMatrix = projectionMatrix,
                    textureSlots = batch.getNumberOfTextures()
                )
            )
            glDrawElements(GL_TRIANGLES, batch.nIndexes, GL_UNSIGNED_INT, 0)
            batch.unbind()
        }
    }

    private fun drawParticles(projectionMatrix: Matrix4f) {
        for (batch in particleBatches) {
            batch.bind()
            prepareShader(
                particleShader,
                ShaderUniforms(projectionMatrix = projectionMatrix)
            )
            glDrawElements(GL_POINTS, batch.nIndexes, GL_UNSIGNED_INT, 0)
            batch.unbind()
        }
    }

    private fun drawShapes(projectionMatrix: Matrix4f) {
        for (batch in shapeBatches) {
            batch.bind()
            prepareShader(
                shapeShader,
                ShaderUniforms(projectionMatrix = projectionMatrix)
            )
            glDrawElements(GL_TRIANGLES, batch.nIndexes, GL_UNSIGNED_INT, 0)
            batch.unbind()
        }
    }

}