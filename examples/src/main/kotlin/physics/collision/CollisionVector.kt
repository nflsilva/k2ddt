package k2ddt.physics.collision

import org.joml.Vector2f

data class CollisionVector(
    val id0: String,
    val id1: String,
    var distance: Float,
    var distanceDelta: Float,
    var normal: Vector2f,
)