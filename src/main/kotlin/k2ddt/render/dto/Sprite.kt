package k2ddt.render.dto

import k2ddt.render.model.Texture
import org.joml.Vector2f

/**
 * Sprite data class
 *
 * Holds data to represent a sprite to be drawn.
 *
 * @param texture the texture resource that has this sprite.
 * @param startTextureCoordinates the top-left texture coordinates for the sprite.
 * @param endTextureCoordinates the bottom-right texture coordinates for the sprite.
 * @param color the color to blend with sprite data.
 * @param colorPercentage the percentage of color to blend with the texture
 */
data class Sprite(
    val texture: Texture,
    val startTextureCoordinates: Vector2f = Vector2f(0.0F),
    val endTextureCoordinates: Vector2f = Vector2f(1.0F),
    val color: Color = Color(0f),
    val colorPercentage: Float = 0f
): RenderEntity() {

    constructor(textureResource: String) : this(Texture(textureResource))

}