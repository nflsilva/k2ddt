package k2ddt.render.dto

/**
 * Shape data class
 *
 * Holds data to represent a shape to be drawn.
 *
 * @param type shape type.
 * @param color shape draw color.
 */
data class Shape(
    val type: Type,
    val color: Color
) {

    /**
     * Shape Types data class
     *
     * Represents the different types of supported shapes.
     *
     */
    enum class Type(val value: Int) {
        SQUARE(0),
        CIRCLE(1),
        DONUT(2),
    }
}