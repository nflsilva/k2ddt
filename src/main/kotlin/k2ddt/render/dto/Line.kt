package k2ddt.render.dto

import org.joml.Vector2f

/**
 * Line data class
 *
 * Holds data to represent a line to be drawn.
 *
 * @param startPoint the start point of the line.
 * @param endPoint the end point of the line.
 * @param color the color to draw the line.
 */
data class Line(
    val startPoint: Vector2f,
    val endPoint: Vector2f,
    val color: Color = Color(0f, 0f, 0f, 1f),
) : RenderEntity() {

    constructor(startX: Float, startY: Float, endX: Float, endY: Float, color: Color) :
            this(Vector2f(startX, startY), Vector2f(endX, endY), color)

}