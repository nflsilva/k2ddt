package k2ddt.render.batch

import k2ddt.render.dto.Sprite
import k2ddt.render.dto.Transform
import k2ddt.render.model.Texture
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.GL_MAX_TEXTURE_IMAGE_UNITS

class SpriteBatch(maxSprites: Int) : BaseBatch<Sprite>(maxSprites, 4, 6) {

    private val maxTextures: Int
    private val textures: MutableList<Texture> = mutableListOf()

    companion object {
        const val POSITION_INDEX = 0
        const val TRANSLATION_INDEX = 1
        const val ROTATION_INDEX = 2
        const val SCALE_INDEX = 3
        const val TEXTURE_COORDS_INDEX = 4
        const val TEXTURE_INDEX = 5
        const val COLOR_INDEX = 6
        const val COLOR_PERCENTAGE_INDEX = 7
    }

    init {
        addFloatAttributeBuffer(POSITION_INDEX, 2)
        addFloatAttributeBuffer(TRANSLATION_INDEX, 2)
        addFloatAttributeBuffer(ROTATION_INDEX, 1)
        addFloatAttributeBuffer(SCALE_INDEX, 2)
        addFloatAttributeBuffer(TEXTURE_COORDS_INDEX, 2)
        addIntAttributeBuffer(TEXTURE_INDEX, 1)
        addFloatAttributeBuffer(COLOR_INDEX, 4)
        addFloatAttributeBuffer(COLOR_PERCENTAGE_INDEX, 1)

        val mtsb = BufferUtils.createIntBuffer(1)
        glGetIntegerv(GL_MAX_TEXTURE_IMAGE_UNITS, mtsb)
        maxTextures = mtsb.get()
    }

    override fun addEntity(entity: Sprite, transform: Transform) {

        if (isFull()) return

        val quad = getQuad(transform.centered)
        addAttributeData(
            POSITION_INDEX,
            quad.tl.x, quad.tl.y,
            quad.bl.x, quad.bl.y,
            quad.br.x, quad.br.y,
            quad.tr.x, quad.tr.y,
            perVertex = false
        )

        addAttributeData(TRANSLATION_INDEX, transform.position.x, transform.position.y)
        addAttributeData(ROTATION_INDEX, transform.rotation)
        addAttributeData(SCALE_INDEX, transform.scale.x, transform.scale.y)
        addAttributeData(
            TEXTURE_COORDS_INDEX,
            entity.startTextureCoordinates.x,   // TL
            entity.startTextureCoordinates.y,

            entity.startTextureCoordinates.x,   // BL
            entity.endTextureCoordinates.y,

            entity.endTextureCoordinates.x,     // BR
            entity.endTextureCoordinates.y,

            entity.endTextureCoordinates.x,     // TR
            entity.startTextureCoordinates.y,
            perVertex = false
        )

        var textureIndex: Int? = null
        for ((i, texture) in textures.withIndex()) {
            if (texture.id == entity.texture.id) {
                textureIndex = i
                break
            }
        }
        if (textureIndex == null) {
            textures.add(entity.texture)
            textureIndex = textures.size - 1
        }

        addAttributeData(TEXTURE_INDEX, textureIndex)
        addAttributeData(COLOR_INDEX, entity.color.r, entity.color.g, entity.color.b, entity.color.a)
        addAttributeData(COLOR_PERCENTAGE_INDEX, entity.colorPercentage)

        val indexOffset = nEntities * 4
        addIndexData(
            0 + indexOffset,
            1 + indexOffset,
            2 + indexOffset,
            2 + indexOffset,
            3 + indexOffset,
            0 + indexOffset
        )
        nEntities += 1
    }

    override fun bind() {
        super.bind()
        for (i in 0 until textures.size) {
            textures[i].bind(i)
        }
    }

    override fun unbind() {
        super.unbind()
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    override fun clear() {
        super.clear()
        textures.clear()
    }

    override fun isFull(): Boolean {
        val fullEntities = super.isFull()
        return fullEntities || textures.size >= maxTextures
    }

    fun hasTexture(texture: Texture): Boolean {
        return textures.find { it.id == texture.id } != null
    }

    fun getNumberOfTextures(): Int {
        return textures.size
    }

}