package examples.physics.dto
/**
 * Physical Body data class
 *
 * Holds data to represent a 2D physical body.
 *
 * @param id id of the physical body.
 * @param mass mass of the physical body.
 * @param velocity current velocity of the physical body.
 * @param acceleration current acceleration of the physical body.
 * @param position the current position of the physical body
 * @param oldPosition the last position of the physical body
 */
import org.joml.Vector2f

data class PhysicalBody(
    val id: String,
    var position: Vector2f,
    var mass: Float,
    var velocity: Vector2f = Vector2f().zero(),
    var acceleration: Vector2f = Vector2f().zero(),
    var oldPosition: Vector2f = position,
    var computeVelocity: Boolean = true
)