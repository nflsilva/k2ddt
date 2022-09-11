package examples.game

import examples.game.domain.GameUpdateContext
import examples.game.domain.Player
import examples.game.domain.level.Level
import examples.game.domain.level.LevelLoader
import k2ddt.core.ExecutionContext
import k2ddt.core.ExecutionDelegate
import k2ddt.core.dto.UpdateContext
import org.joml.Vector2f

private class Delegate : ExecutionDelegate() {

    private val cameraCenter = Vector2f(0f)
    private lateinit var player: Player
    private lateinit var level: Level

    override fun onStart() {
        player = Player(32f * 4 * 3, 32f * 4 * 2.5f)
        val ll = LevelLoader()
        level = ll.loadLevel()

        executionContext.moveCamera(
            player.transform.position.x - 1280f,
            player.transform.position.y - 720f
        )
    }

    override fun onUpdate(updateContext: UpdateContext) {
        val context = GameUpdateContext(updateContext, level)
        player.update(context)
        level.update(updateContext)



    }

    override fun onFrame() {
        player.draw(executionContext)
        level.draw(executionContext)
    }
}

fun main(args: Array<String>) {

    val delegate = Delegate()
    val ec = ExecutionContext(delegate = delegate)
    ec.start()

    println("Done!")
}

