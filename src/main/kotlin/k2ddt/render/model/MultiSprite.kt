package k2ddt.render.model

import k2ddt.render.dto.Sprite
import org.joml.Vector2f

class MultiSprite(
    val rows: Int,
    val columns: Int
) {

    data class SpriteInfo(val sprite: Sprite, val size: Vector2f)

    private val sprites: Array<Array<SpriteInfo?>> = Array(rows) { Array(columns) { null } }
    private val textureSet = mutableSetOf<Int>()

    init {
        if (rows < 1 || columns < 1) {
            throw IllegalArgumentException("Rows and Columns must be > 0")
        }
    }

    val nTextures: Int
        get() = textureSet.size

    var nSprites: Int = 0
    var isTransparent = false

    fun addSprite(row: Int, column: Int, size: Float, sprite: Sprite) {
        addSprite(row, column, Vector2f(size), sprite)
    }

    fun addSprite(row: Int, column: Int, size: Vector2f, sprite: Sprite) {
        if (row < 0 || row >= rows || column < 0 || column >= columns) return
        sprites[row][column] = SpriteInfo(sprite, size)
        textureSet.add(sprite.texture.id)
        nSprites += 1
        isTransparent = isTransparent || sprite.color.a < 1.0f
    }

    fun getSprite(row: Int, column: Int): SpriteInfo? {
        if (row < 0 || row >= rows || column < 0 || column >= columns) return null
        return sprites[row][column]
    }
}