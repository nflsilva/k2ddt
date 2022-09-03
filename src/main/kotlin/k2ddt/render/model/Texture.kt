package k2ddt.render.model

import k2ddt.tools.ResourceManager
import k2ddt.tools.dto.ImageData
import org.lwjgl.opengl.GL13.*
import java.nio.ByteBuffer

class Texture(
    val width: Int,
    val height: Int,
    data: ByteBuffer
) {

    val id: Int = glGenTextures()

    constructor(textureData: ImageData) : this(textureData.width, textureData.height, textureData.data)
    constructor(resourceName: String) : this(ResourceManager.loadTextureFromFile(resourceName))

    init {
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data)
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    fun cleanUp() {
        glBindTexture(GL_TEXTURE_2D, 0)
        glDeleteTextures(id)
    }

    fun bind(slot: Int) {
        glActiveTexture(GL_TEXTURE0 + slot)
        glBindTexture(GL_TEXTURE_2D, id)
    }

    fun unbind() {
        glBindTexture(GL_TEXTURE_2D, 0)
    }
}