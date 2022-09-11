package k2ddt.tools.dto

import java.nio.ShortBuffer

data class SoundData(
    val channels: Int,
    val sampleRate: Int,
    val data: ShortBuffer
)