package k2ddt.tools

import k2ddt.tools.dto.ImageData
import k2ddt.tools.dto.ShaderData
import k2ddt.tools.dto.SoundData
import k2ddt.tools.exception.ErrorLoadingResourceException
import k2ddt.tools.exception.ResourceNotFoundException
import org.lwjgl.BufferUtils
import org.lwjgl.stb.STBImage.*
import org.lwjgl.stb.STBVorbis
import java.io.BufferedInputStream
import java.net.URL
import java.nio.ByteBuffer
import java.nio.IntBuffer

object ResourceManager {

    private val textures: MutableMap<String, ImageData> = mutableMapOf()
    private val sounds: MutableMap<String, SoundData> = mutableMapOf()
    private val shaders: MutableMap<String, ShaderData> = mutableMapOf()

    //TODO: What a mess. Rethink about this.
    fun loadTextureFromFile(fileName: String): ImageData {
        if(fileName in textures.keys) { return textures[fileName]!! }
        val resourceUrl = getResourceURLFromFile(fileName)
        val width: IntBuffer = BufferUtils.createIntBuffer(1)
        val height: IntBuffer = BufferUtils.createIntBuffer(1)
        val components: IntBuffer = BufferUtils.createIntBuffer(1)

        val fileChunks: MutableList<ByteBuffer> = mutableListOf()
        var reader: BufferedInputStream? = null
        var totalSize = 0

        try {
            reader = BufferedInputStream(resourceUrl.openStream())
            val readChunk = ByteArray(4096)

            while (true)
            {
                val nReadBytes = reader.read(readChunk)
                if(nReadBytes <= 0){ break }

                totalSize += nReadBytes
                fileChunks.add(BufferUtils.createByteBuffer(nReadBytes))

                val buffer = fileChunks[fileChunks.size-1]
                if(buffer.capacity() < readChunk.count()){
                    for(i in 0 until buffer.capacity()){
                        buffer.put(readChunk[i])
                    }
                }
                else {
                    buffer.put(readChunk)
                }
            }

        } finally {
            reader?.close()
        }

        val data: ByteBuffer = BufferUtils.createByteBuffer(totalSize)
        fileChunks.forEach {
            data.put(it.flip())
        }

        stbi_load_from_memory(data.flip(), width, height, components, STBI_rgb_alpha)?.let {
            val textureData = ImageData(width.get(), height.get(), components.get(), it)
            textures[fileName] = textureData
            return textureData
        }

        throw ErrorLoadingResourceException(fileName + " R: " + stbi_failure_reason())
    }

    fun loadShaderSourceFromFile(fileName: String): ShaderData {
        if(fileName in shaders.keys) { return shaders[fileName]!! }
        val resourceUrl = getResourceURLFromFile(fileName)
        val shaderData = ShaderData(resourceUrl.readText())
        shaders[fileName] = shaderData
        return shaderData
    }

    fun loadSoundFromFile(fileName: String): SoundData {
        if(fileName in sounds.keys) { return sounds[fileName]!! }
        val resourceUrl = getResourceURLFromFile(fileName)

        val channels: IntBuffer = BufferUtils.createIntBuffer(1)
        val sampleRate: IntBuffer = BufferUtils.createIntBuffer(1)

        val data = STBVorbis.stb_vorbis_decode_filename(resourceUrl.path, channels, sampleRate)!!

        return SoundData(channels.get(), sampleRate.get(), data)
    }

    private fun getResourceURLFromFile(fileName: String): URL {
        object{}::class.java.getResource(fileName)?.let { url ->
            var resourcePath = url.path
            //Why do I need this hack in windows?
            //resourcePath = resourcePath.drop(1)
            return url
        }
        throw ResourceNotFoundException(fileName)
    }
}