package k2ddt.render.shader

class LineShader : BaseShader(VERTEX_SHADER, FRAGMENT_SHADER) {

    init {
        bindAttributes()
        createUniforms()
    }

    override fun bindAttributes() {
        bindAttribute(0, POSITION_ATTRIBUTE)
        bindAttribute(1, COLOR_ATTRIBUTE)
        bindAttribute(2, TRANSLATION_ATTRIBUTE)
        bindAttribute(3, ROTATION_ATTRIBUTE)
        bindAttribute(4, SCALE_ATTRIBUTE)
    }

    override fun createUniforms() {
        addUniform(PROJECTION_MATRIX_UNIFORM)
    }

    override fun updateUniforms(data: ShaderUniforms) {
        setUniformMatrix4f(PROJECTION_MATRIX_UNIFORM, data.projectionMatrix)
    }

    companion object {
        private const val VERTEX_SHADER = "/shader/line/vertex.glsl"
        private const val FRAGMENT_SHADER = "/shader/line/fragment.glsl"

        private const val POSITION_ATTRIBUTE = "in_position"
        private const val COLOR_ATTRIBUTE = "in_color"
        private const val TRANSLATION_ATTRIBUTE = "in_translation"
        private const val ROTATION_ATTRIBUTE = "in_rotation"
        private const val SCALE_ATTRIBUTE = "in_scale"

        private const val PROJECTION_MATRIX_UNIFORM = "in_projectionMatrix"

    }
}