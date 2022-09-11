package k2ddt.sound

import k2ddt.sound.dto.Sound
import k2ddt.sound.model.SoundBuffer
import k2ddt.sound.model.SoundListener
import k2ddt.sound.model.SoundSource
import k2ddt.tools.ResourceManager
import org.lwjgl.openal.AL
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC10.*
import org.lwjgl.openal.ALCCapabilities
import java.nio.ByteBuffer
import java.nio.IntBuffer


class SoundEngine {

    private var device: Long = 0
    private var context: Long = 0

    private var soundBuffers: MutableMap<String, SoundBuffer> = mutableMapOf()
    private var soundSources: MutableMap<String, SoundSource> = mutableMapOf()

    init {

        device = alcOpenDevice(null as ByteBuffer?)
        //check(device != null) { "Failed to open the default OpenAL device." }

        val deviceCaps: ALCCapabilities = ALC.createCapabilities(device)
        context = alcCreateContext(device, null as IntBuffer?)
        //check(context != null) { "Failed to create OpenAL context." }

        alcMakeContextCurrent(context)
        AL.createCapabilities(deviceCaps)
    }

    fun playSound(sound: Sound) {

        var source = soundSources[sound.source]
        if(source == null) {
            source = SoundSource(sound.loop, false)
            soundSources[sound.source] = source
        }

        var buffer = soundBuffers[sound.soundResource]
        if(buffer == null) {
            buffer = SoundBuffer(sound.soundResource)
            soundBuffers[sound.soundResource] = buffer
        }

        source.setBuffer(buffer.bufferId)

        source.setGain(1.0f)
        source.play()

    }
}