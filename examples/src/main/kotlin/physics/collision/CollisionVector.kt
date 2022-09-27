package k2ddt.physics.collision

import org.joml.Vector2f

data class CollisionVector(
    val thisId: String,
    val otherId: String,
    var distanceDelta: Float,
    var normal: Vector2f,
)