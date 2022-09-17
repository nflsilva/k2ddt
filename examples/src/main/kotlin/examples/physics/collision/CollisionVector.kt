package examples.physics.collision

import org.joml.Vector2f

data class CollisionVector(
    val id0: String,
    val id1: String,
    val distance: Float,
    val vector: Vector2f,
)