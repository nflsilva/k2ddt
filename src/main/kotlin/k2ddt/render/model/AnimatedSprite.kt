package k2ddt.render.model

import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.Sprite

class AnimatedSprite(
    private val keyframes: List<Keyframe>,
    private val loop: Boolean = true
) {

    data class Keyframe(
        val sprite: Sprite,
        val duration: Float

    )

    companion object {
        fun fromCollection(nSprites: Int, basePath: String, loop: Boolean = true): AnimatedSprite {
            val frames = mutableListOf<Keyframe>()
            for (k in 0 until nSprites) {
                frames.add(
                    Keyframe(
                        Sprite(String.format(basePath, k)),
                        0.01f
                    ),
                )
            }
            return AnimatedSprite(frames, loop)
        }
    }

    var ended = false
    private var currentKeyframeIndex = 0
    private var currentKeyframeElapsedTime = 0.0f

    fun reset() {
        currentKeyframeIndex = 0
        currentKeyframeElapsedTime = 0f
    }

    fun update(context: UpdateContext) {

        val currentKeyframe = keyframes[currentKeyframeIndex]

        currentKeyframeElapsedTime += context.elapsedTime
        if (currentKeyframe.duration < currentKeyframeElapsedTime) {
            currentKeyframeIndex += 1
            ended = currentKeyframeIndex >= keyframes.size - 1 && loop
            currentKeyframeIndex %= keyframes.size

            currentKeyframeElapsedTime = 0.0f
        }
    }

    fun getCurrentSprite(): Sprite {
        return keyframes[currentKeyframeIndex].sprite
    }
}