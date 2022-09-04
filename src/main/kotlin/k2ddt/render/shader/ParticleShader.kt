package k2ddt.render.shader

import org.lwjgl.opengl.GL11.glDisable
import org.lwjgl.opengl.GL11.glEnable
import org.lwjgl.opengl.GL20.GL_VERTEX_PROGRAM_POINT_SIZE

class ParticleShader : BaseShader(VERTEX_SHADER, FRAGMENT_SHADER) {

    init {
        bindAttributes()
        createUniforms()

    }

    override fun bindAttributes() {
        bindAttribute(0, POSITION_ATTRIBUTE)
        bindAttribute(1, SIZE_ATTRIBUTE)
        bindAttribute(2, TYPE_ATTRIBUTE)
        bindAttribute(3, COLOR_ATTRIBUTE)
        bindAttribute(4, LAYER_ATTRIBUTE)
    }

    override fun bind() {
        super.bind()
        glEnable(GL_VERTEX_PROGRAM_POINT_SIZE)
    }

    override fun unbind() {
        super.unbind()
        glDisable(GL_VERTEX_PROGRAM_POINT_SIZE)
    }

    override fun createUniforms() {
        addUniform(PROJECTION_MATRIX_UNIFORM)
    }

    override fun updateUniforms(data: ShaderUniforms) {
        setUniformMatrix4f(PROJECTION_MATRIX_UNIFORM, data.projectionMatrix)
    }

    companion object {
        private const val VERTEX_SHADER = "/shader/particle/vertex.glsl"
        private const val FRAGMENT_SHADER = "/shader/particle/fragment.glsl"

        private const val POSITION_ATTRIBUTE = "in_position"
        private const val SIZE_ATTRIBUTE = "in_size"
        private const val TYPE_ATTRIBUTE = "in_type"
        private const val COLOR_ATTRIBUTE = "in_color"
        private const val LAYER_ATTRIBUTE = "in_layer"

        private const val PROJECTION_MATRIX_UNIFORM = "in_projectionMatrix"
    }
}