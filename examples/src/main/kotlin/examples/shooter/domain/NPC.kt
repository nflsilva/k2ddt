package examples.shooter.domain

import examples.shooter.domain.levelObject.Drawable
import k2ddt.core.ExecutionContext
import k2ddt.render.model.AnimatedSprite

class NPC(startX: Float, startY: Float) : Drawable(startX, startY, 0f, SIZE, SIZE, LAYER, hasCollision = true) {

    companion object {
        const val SIZE = 32f * 4
        const val LAYER = 1
    }

    private val idleSprite: AnimatedSprite

    init {
        idleSprite = AnimatedSprite.fromCollection(20, "/sprite/soldier/handgun/idle/survivor-idle_handgun_%d.png")

    }

    override fun onFrame(context: ExecutionContext) {
        super.onFrame(context)
        context.render(idleSprite.getCurrentSprite(), transform)
    }

    override fun onHit() {
        super.onHit()
        println("HIT!")
    }


}