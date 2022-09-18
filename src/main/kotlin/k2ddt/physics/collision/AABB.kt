package k2ddt.physics.collision

import org.joml.Vector2f

class AABB(var center: Vector2f, var radius: Float) {

    val x: Float
        get() = center.x

    val y: Float
        get() = center.y

    val left: Float
        get() = x - radius

    val right: Float
        get() = x + radius

    val top: Float
        get() = y + radius

    val bottom: Float
        get() = y - radius

    fun collidesWith(other: AABB): Boolean {
        return (top > other.bottom && bottom < other.bottom) ||
                (bottom < other.top && top > other.top) ||
                (left < other.right && right > other.right) ||
                (right > other.left && left < other.left)
    }

}