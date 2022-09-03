package k2ddt.render.dto

import org.joml.Vector2f

/**
 * Transform data class
 *
 * Holds data to compute a 2D transform matrix.
 *
 * @param position the 2D position on the screen.
 * @param rotation the 2D rotation value.
 * @param scale the 2D scale value.
 * @param layer the drawing layer value [0-999]. 0 is the top, 999 is the bottom.
 * @throws IllegalArgumentException if drawing layer is outside expected values.
 *
 */
data class Transform(
    var position: Vector2f,
    var rotation: Float,
    var scale: Vector2f,
    var layer: Int
) {

    /**
     * Creates a transforms from primitive types
     *
     * @param positionX the X axis coordinate on the screen.
     * @param positionY the Y axis coordinate on the screen.
     * @param rotation the 2D rotation value
     * @param scaleX the X axis scale on the screen.
     * @param scaleY the Y axis scale on the screen.
     * @param layer the drawing layer value [0-999]. 0 is the top, 999 is the bottom.
     * @throws IllegalArgumentException if drawing layer is outside expected values.
     *
     */
    constructor(
        positionX: Float,
        positionY: Float,
        rotation: Float,
        scaleX: Float,
        scaleY: Float,
        layer: Int
    ) : this(Vector2f(positionX, positionY), rotation, Vector2f(scaleX, scaleY), layer)

    init {
        if(layer < 0 || layer > 999) {
            throw IllegalArgumentException("Layer should be between 0 and 999")
        }
    }

    /**
     * Translates current screen position by provided value
     *
     * @param value value to translate current position
     *
     */
    fun translate(value: Vector2f) {
        position.add(value)
    }

    /**
     * Translates current screen position by provided value
     *
     * @param x value to translate current position on the X axis.
     * @param y value to translate current position on the Y axis.
     *
     */
    fun translate(x: Float, y: Float) {
        translate(Vector2f(x, y))
    }

    /**
     * Overwrites current screen position with provided values
     *
     * @param x value to overwrite current position on the X axis.
     * @param y value to overwrite current position on the Y axis.
     *
     */
    fun setPosition(x: Float, y: Float) {
        position = Vector2f(x, y)
    }

    /**
     * Rotates transform by provided value
     *
     * @param angle the rotation value to apply.
     *
     */
    fun rotate(angle: Float) {
        rotation += angle
    }
}