package k2ddt.render

import k2ddt.render.batch.ParticleBatch
import k2ddt.render.batch.ShapeBatch
import k2ddt.render.batch.SpriteBatch
import k2ddt.render.batchController.BatchControllerBundle
import k2ddt.render.dto.*
import k2ddt.render.model.MultiSprite
import k2ddt.render.shader.*
import org.joml.Matrix4f
import org.joml.Vector2f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.glClearColor

class RenderEngine(private val screenWidth: Int, private val screenHeight: Int) {

    private var backgroundColor: Color = Color(0.0f)

    private lateinit var spriteShader: SpriteShader
    private lateinit var particleShader: ParticleShader
    private lateinit var shapeShader: ShapeShader

    private lateinit var opaqueSpriteBatches: BatchControllerBundle
    private lateinit var transparentSpriteBatches: BatchControllerBundle

    private var zoom = 0.0f
    private val bottom = 0.0f
    private val left = 0.0f
    private val top = screenHeight * (1.0f - zoom)
    private val right = screenWidth * (1.0f - zoom)

    companion object {
        const val DEFAULT_SCREEN_RENDER_MARGINS: Int = 100
    }

    fun onStart() {

        spriteShader = SpriteShader()
        particleShader = ParticleShader()
        shapeShader = ShapeShader()

        val shaderBundle = ShaderBundle()
        opaqueSpriteBatches = BatchControllerBundle(shaderBundle)
        transparentSpriteBatches = BatchControllerBundle(shaderBundle)

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

    }

    fun setBackgroundColor(color: Color) {
        backgroundColor = color
    }

    fun render(sprite: Sprite, transform: Transform) {
        if (isVisible(transform, transform.scale)) {
            opaqueSpriteBatches.addToSuitableBatch(sprite, transform)
        }
    }

    fun render(particle: Particle, transform: Transform) {
        if (isVisible(transform, Vector2f(particle.size))) {
            opaqueSpriteBatches.addToSuitableBatch(particle, transform)
        }
    }

    fun render(shape: Shape, transform: Transform) {
        if (isVisible(transform, transform.scale)) {
            opaqueSpriteBatches.addToSuitableBatch(shape, transform)
        }
    }

    fun render(multiSprite: MultiSprite, transform: Transform) {
        if (isVisible(transform, transform.scale)) {
            opaqueSpriteBatches.addToSuitableBatch(multiSprite, transform)
        }
    }

    fun render(text: Text, transform: Transform) {
        if (isVisible(transform, transform.scale)) {
            opaqueSpriteBatches.addToSuitableBatch(text, transform)
        }
    }

    private fun isVisible(transform: Transform, size: Vector2f): Boolean {
        return transform.position.x > left - DEFAULT_SCREEN_RENDER_MARGINS &&
                transform.position.x + size.x < right + DEFAULT_SCREEN_RENDER_MARGINS &&
                transform.position.y < top + DEFAULT_SCREEN_RENDER_MARGINS &&
                transform.position.y + size.y > bottom - DEFAULT_SCREEN_RENDER_MARGINS
    }

    private fun prepareShader(shader: BaseShader, uniformData: ShaderUniforms) {
        shader.bind()
        shader.updateUniforms(uniformData)
    }

    private fun draw() {
        val projectionMatrix = Matrix4f()
            .setOrtho(left, right, bottom, top, 0f, 1000f)
        val uniforms = ShaderUniforms(projectionMatrix)
        opaqueSpriteBatches.draw(uniforms)
        transparentSpriteBatches.draw(uniforms)

        /*
        Log.d("Sprite batches: ${spriteBatches.size} " +
                "Particle batches: ${particleBatches.size} " +
                "Shape batches: ${shapeBatches.size}"
        )
        println(shapeBatches.firstOrNull()?.nEntities)
         */
    }

    private fun clearBatches() {
        opaqueSpriteBatches.clearBatches()
        transparentSpriteBatches.clearBatches()
    }

}