package examples.shooter.domain.levelObject.weapon

import examples.shooter.domain.GameUpdateContext
import examples.shooter.domain.levelObject.Drawable
import k2ddt.core.ExecutionContext
import k2ddt.sound.dto.Sound
import org.joml.Matrix2f
import org.joml.Vector2f

class Handgun(parent: Drawable) :
    Drawable(
        parent.transform.position.x,
        parent.transform.position.y + WEAPON_OFFSET,
        0f,
        0f,
        0f,
        2,
        parent = parent
    ) {

    companion object {
        const val BULLETS_PER_MINUTE = 400
        const val WEAPON_OFFSET = 32f
    }

    private val soundName = "/sound/380_single.ogg"
    private val soundSources = listOf("380_0", "380_1")
    private var soundSourceIndex = 0

    private var playSound = false
    private val activeBullets = mutableListOf<Bullet>()
    private var timeSinceLastBullet = 0f

    fun fire() {
        if (timeSinceLastBullet >= (1f / (BULLETS_PER_MINUTE / 60f))) {
            activeBullets.add(Bullet(this))
            timeSinceLastBullet = 0f
            playSound = true
        }
    }

    override fun onUpdate(context: GameUpdateContext) {
        super.onUpdate(context)
        parent?.let {
            val rot = Vector2f(0f, -WEAPON_OFFSET).mul(Matrix2f().rotation(-it.transform.rotation))

            transform.position = Vector2f(it.transform.position).add(rot)
            transform.rotation = it.transform.rotation
            direction = it.direction
        }
        activeBullets.forEach { it.onUpdate(context) }
        activeBullets.removeIf { it.toRemove }
        timeSinceLastBullet += context.baseContext.elapsedTime
    }

    override fun onFrame(context: ExecutionContext) {
        super.onFrame(context)
        activeBullets.forEach { it.onFrame(context) }
        if (playSound) {
            context.playSound(Sound(soundName, soundSources[soundSourceIndex], false))
            soundSourceIndex = (soundSourceIndex + 1) % soundSources.size
            playSound = false
        }
    }

}