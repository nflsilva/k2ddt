package k2ddt.render

import k2ddt.render.batchController.BatchControllerBundle
import k2ddt.render.dto.*
import k2ddt.render.font.DefaultFont
import k2ddt.render.model.BitmapFont
import k2ddt.render.model.Camera2D
import k2ddt.render.model.MultiSprite
import k2ddt.render.shader.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.glClearColor

class RenderEngine(private val screenWidth: Int, private val screenHeight: Int) {

    private lateinit var spriteShader: SpriteShader
    private lateinit var particleShader: ParticleShader
    private lateinit var shapeShader: ShapeShader

    private lateinit var entityBatches: BatchControllerBundle
    private lateinit var defaultFont: BitmapFont

    private var backgroundColor = Color(0.0f)
    private var camera = Camera2D(screenWidth, screenHeight)

    fun onStart() {

        spriteShader = SpriteShader()
        particleShader = ParticleShader()
        shapeShader = ShapeShader()

        val shaderBundle = ShaderBundle()
        entityBatches = BatchControllerBundle(shaderBundle)

        defaultFont = DefaultFont()

        glDisable(GL_DEPTH_TEST)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
    }

    fun onFrame() {

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a)
        glViewport(0, 0, screenWidth, screenHeight)

        draw()
        clearBatches()
    }

    fun setBackgroundColor(color: Color) {
        backgroundColor = color
    }

    fun moveCamera(deltaX: Float, deltaY: Float) {
        camera.move(deltaX, deltaY)
    }

    fun zoomCamera(percentage: Float) {
        camera.zoom(percentage)
    }

    fun render(sprite: Sprite, transform: Transform) {
        if (isVisible(transform)) {
            entityBatches.addToSuitableBatch(sprite, transform)
        }
    }

    fun render(particle: Particle, transform: Transform) {
        if (isVisible(transform)) {
            entityBatches.addToSuitableBatch(particle, transform)
        }
    }

    fun render(shape: Shape, transform: Transform) {
        if (isVisible(transform)) {
            entityBatches.addToSuitableBatch(shape, transform)
        }
    }

    fun render(multiSprite: MultiSprite, transform: Transform) {
        if (isVisible(transform)) {
            entityBatches.addToSuitableBatch(multiSprite, transform)
        }
    }

    fun render(text: Text, transform: Transform) {
        if (isVisible(transform)) {
            if (text.font == null) {
                text.font = defaultFont
            }
            entityBatches.addToSuitableBatch(text, transform)
        }
    }

    fun render(line: Line, transform: Transform) {
        if (isVisible(transform)) {
            entityBatches.addToSuitableBatch(line, transform)
        }
    }

    private fun isVisible(transform: Transform): Boolean {
        return transform.right >= camera.left ||
                transform.left <= camera.right ||
                transform.bottom >= camera.bottom ||
                transform.top <= camera.top
    }

    private fun draw() {
        val uniforms = ShaderUniforms(camera.projectionMatrix)
        entityBatches.draw(uniforms)
    }

    private fun clearBatches() {
        entityBatches.clearBatches()
    }

}