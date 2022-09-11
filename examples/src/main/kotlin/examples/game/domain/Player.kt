package examples.game.domain

import examples.game.domain.level.Level
import examples.game.domain.levelObject.Drawable
import examples.game.domain.levelObject.weapon.Handgun
import k2ddt.core.ExecutionContext
import k2ddt.render.dto.Sprite
import k2ddt.render.dto.Transform
import k2ddt.render.model.AnimatedSprite
import org.joml.Vector2f
import ui.dto.InputStateData

class Player(startX: Float, startY: Float) : Drawable(startX, startY, 0f, SIZE, SIZE, LAYER) {

    companion object {
        const val SIZE = 32f * 4
        const val RUN_SPEED = 525f
        const val WALK_SPEED = 64f
        const val LAYER = 1
    }

    private val shootSprite: AnimatedSprite
    private val moveSprite: AnimatedSprite
    private val idleSprite: AnimatedSprite

    private val feetRunSprite: AnimatedSprite
    private val feetWalkSprite: AnimatedSprite

    private var moving = false
    private var speed = RUN_SPEED
    private val weapon: Handgun = Handgun(this)

    init {
        shootSprite = loadSprites(3, "/sprite/soldier/handgun/shoot/survivor-shoot_handgun_%d.png")
        idleSprite = loadSprites(20, "/sprite/soldier/handgun/idle/survivor-idle_handgun_%d.png")
        moveSprite = loadSprites(20, "/sprite/soldier/handgun/move/survivor-move_handgun_%d.png")

        feetRunSprite = loadSprites(20, "/sprite/soldier/feet/run/survivor-run_%d.png")
        feetWalkSprite = loadSprites(20, "/sprite/soldier/feet/walk/survivor-walk_%d.png")

        transform.centered = true
        childen.add(weapon)
    }

    private fun loadSprites(nSprites: Int, basePath: String): AnimatedSprite {
        val frames = mutableListOf<AnimatedSprite.Keyframe>()
        for (k in 0 until nSprites) {
            frames.add(
                AnimatedSprite.Keyframe(
                    Sprite(String.format(basePath, k)),
                    0.01f
                ),
            )
        }
        return AnimatedSprite(frames)
    }

    fun update(context: GameUpdateContext) {
        super.onUpdate(context)
        handleInput(context.baseContext.input, context.baseContext.elapsedTime, context.level)
        idleSprite.update(context.baseContext)
        moveSprite.update(context.baseContext)
        feetRunSprite.update(context.baseContext)
    }

    fun draw(context: ExecutionContext) {
        super.onFrame(context)

        val feetTransform = Transform(
            transform.position,
            transform.rotation,
            Vector2f(transform.scale).mul(0.4f),
            transform.layer + 1,
            centered = true
        )
        if (moving) {
            context.render(feetRunSprite.getCurrentSprite(), feetTransform)
            context.render(moveSprite.getCurrentSprite(), transform)
        } else {
            context.render(idleSprite.getCurrentSprite(), transform)
        }
    }

    private fun handleInput(input: InputStateData, deltaTime: Float, level: Level) {

        val isWalking = input.isKeyPressed(InputStateData.KEY_LEFT_SHIFT)
        speed = if (isWalking) WALK_SPEED else RUN_SPEED

        moving = false

        if (input.isKeyPressed(InputStateData.KEY_W)) {
            transform.position.y += speed * deltaTime
            handleTopCollision(level)
            moving = true
        } else if (input.isKeyPressed(InputStateData.KEY_S)) {
            transform.position.y += -speed * deltaTime
            handleBottomCollision(level)
            moving = true
        }

        if (input.isKeyPressed(InputStateData.KEY_A)) {
            transform.position.x += -speed * deltaTime
            handleLeftCollision(level)
            moving = true
        } else if (input.isKeyPressed(InputStateData.KEY_D)) {
            transform.position.x += speed * deltaTime
            handleRightCollision(level)
            moving = true
        }

        if (input.isMousePressed(InputStateData.BUTTON_RIGHT)) {
            weapon.fire()
        }

        direction = Vector2f(input.mouseX.toFloat(), input.mouseY.toFloat())
            .sub(Vector2f(transform.position.x, transform.position.y)).normalize()

        transform.rotation = -Vector2f(1f, 0f).angle(direction)
    }

    private fun handleTopCollision(level: Level) {
        val r = SIZE / 2f
        val nSizes = (SIZE / level.blockSize).toInt()

        val top = transform.position.y + r - 0.0001f
        val topBlockY = (top / level.blockSize).toInt()

        val left = transform.position.x - r
        val leftBlockX = (left / level.blockSize).toInt()

        for (n in 0 until nSizes) {
            if (level.hasCollisionAt(topBlockY, leftBlockX + n)) {
                transform.position.y = topBlockY * level.blockSize - r
            }
        }
    }

    private fun handleBottomCollision(level: Level) {
        val r = SIZE / 2f
        val nSizes = (SIZE / level.blockSize).toInt()

        val bottom = transform.position.y - r
        val bottomBlockY = (bottom / level.blockSize).toInt()

        val left = transform.position.x - r
        val leftBlockX = (left / level.blockSize).toInt()

        for (n in 0 until nSizes) {
            if (level.hasCollisionAt(bottomBlockY, leftBlockX + n)) {
                transform.position.y = bottomBlockY * level.blockSize + level.blockSize + r
            }
        }
    }

    private fun handleLeftCollision(level: Level) {
        val r = SIZE / 2f
        val nSizes = (SIZE / level.blockSize).toInt()

        val bottom = transform.position.y - r
        val bottomBlockY = (bottom / level.blockSize).toInt()

        val left = transform.position.x - r
        val leftBlockX = (left / level.blockSize).toInt()

        for (n in 0 until nSizes) {
            if (level.hasCollisionAt(bottomBlockY + n, leftBlockX)) {
                transform.position.x = leftBlockX * level.blockSize + level.blockSize + r
            }
        }
    }

    private fun handleRightCollision(level: Level) {
        val r = SIZE / 2f
        val nSizes = (SIZE / level.blockSize).toInt()

        val bottom = transform.position.y - r
        val bottomBlockY = (bottom / level.blockSize).toInt()

        val right = transform.position.x + r - 0.0001f
        val rightBlockX = (right / level.blockSize).toInt()

        for (n in 0 until nSizes) {
            if (level.hasCollisionAt(bottomBlockY + n, rightBlockX)) {
                transform.position.x = rightBlockX * level.blockSize - r
            }
        }
    }

}