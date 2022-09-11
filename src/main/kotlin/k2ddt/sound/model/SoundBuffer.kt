package k2ddt.sound.model

import k2ddt.tools.ResourceManager
import org.lwjgl.openal.AL10.*
import org.lwjgl.stb.*

class SoundBuffer(file: String) {

    val bufferId: Int = alGenBuffers()


    init {

        val soundData = ResourceManager.loadSoundFromFile(file)

        // Copy to buffer
        alBufferData(
            bufferId,
            if (soundData.channels === 1) AL_FORMAT_MONO16 else AL_FORMAT_STEREO16,
            soundData.data,
            soundData.sampleRate
        )
    }

    fun cleanup() {
        alDeleteBuffers(bufferId)
    }
}