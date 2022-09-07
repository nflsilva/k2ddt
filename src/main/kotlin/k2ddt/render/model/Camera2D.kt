package k2ddt.render.model

import org.joml.Matrix4f
import org.joml.Vector2f
import java.lang.Float.max
import java.lang.Float.min

class Camera2D(screenWidth: Int, screenHeight: Int, private val adjustToBounds: Boolean = true) {

    private val rightLimit = screenWidth.toFloat()
    private val topLimit = screenHeight.toFloat()
    private val leftLimit = 0.0f
    private val bottomLimit = 0.0f
    private val near = 0.0f
    private val far = 100f

    private var currentZoom = 0.0f
    private var minZoom = 0f
    private var maxZoom = 350f
    private var aspectRatio = rightLimit / topLimit
    private var bottomLeft = Vector2f(0.0f)
    private var topRight = Vector2f(rightLimit, topLimit)

    val bottom: Float
        get() = bottomLeft.y
    val left: Float
        get() = bottomLeft.x
    val top: Float
        get() = topRight.y
    val right: Float
        get() = topRight.x

    val projectionMatrix: Matrix4f
        get() = Matrix4f().setOrtho(left, right, bottom, top, near,far)

    fun move(deltaX: Float, deltaY: Float) {
        val delta = Vector2f(deltaX, deltaY)
        val newBottomLeft = Vector2f(bottomLeft).add(delta)
        val newTopRight = Vector2f(topRight).add(delta)

        if (adjustToBounds) {
            if (newBottomLeft.x < leftLimit || newBottomLeft.y < bottomLimit) return
            if (newTopRight.x > rightLimit || newTopRight.y > topLimit) return
        }

        bottomLeft = newBottomLeft
        topRight = newTopRight
    }

    fun zoom(percentage: Float) {
        if (percentage < 0f && currentZoom == minZoom) return
        if (percentage > 0f && currentZoom == maxZoom) return

        currentZoom = min(max(minZoom, currentZoom + percentage), maxZoom)
        val delta = Vector2f(percentage * aspectRatio, percentage)

        bottomLeft.add(delta)
        topRight.sub(delta)

        if (adjustToBounds) {
            var deltaX = 0.0f
            var deltaY = 0.0f
            if (bottomLeft.x < leftLimit) {
                deltaX = -bottomLeft.x
            } else if (topRight.x > rightLimit) {
                deltaX = rightLimit - topRight.x
            }

            if (bottomLeft.y < bottomLimit) {
                deltaY = -bottomLeft.y
            } else if (topRight.y > topLimit) {
                deltaY = topLimit - topRight.y
            }

            move(deltaX, deltaY)
        }

    }

}