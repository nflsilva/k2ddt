package k2ddt.render.model

import k2ddt.core.dto.UpdateContext
import k2ddt.render.dto.Sprite

class AnimatedSprite(
    private val keyframesByState: List<Keyframe>
) {

    data class Keyframe(
        val sprite: Sprite,
        val duration: Float
    )

    private var currentKeyframeIndex: Int = 0
    private var currentKeyframeElapsedTime: Float = 0.0f

    fun update(context: UpdateContext) {

        val currentKeyframe = keyframesByState[currentKeyframeIndex]

        currentKeyframeElapsedTime += context.elapsedTime
        if (currentKeyframe.duration < currentKeyframeElapsedTime) {
            currentKeyframeIndex = (currentKeyframeIndex + 1) % keyframesByState.size
            currentKeyframeElapsedTime = 0.0f
        }
    }

    fun getCurrentSprite(): Sprite {
        return keyframesByState[currentKeyframeIndex].sprite
    }

}