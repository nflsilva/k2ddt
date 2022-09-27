package k2ddt.physics.dto
/**
 * Physical Body class
 *
 * Represents a 2D physical body.
 *
 * @param entity the corresponding game entity.
 * @param mass mass of the physical body
 * @param velocity current velocity of the physical body
 * @param acceleration current acceleration of the physical body
 * @param position the current position of the physical body
 * @param oldPosition the last position of the physical body
 * @param isStatic defines whether of not the body should move
 * @param computeVelocity indicates if the velocity should be computed on the next update cycle
 */
import k2ddt.core.GameEntity
import k2ddt.physics.collision.CollisionBox
import k2ddt.physics.collision.CollisionSolver
import k2ddt.physics.collision.CollisionVector
import org.joml.Vector2f

class PhysicalBody(
    val entity: GameEntity,
    var mass: Float = 0f,
    var drag: Float = 0f,
    var velocity: Vector2f = Vector2f().zero(),
    var acceleration: Vector2f = Vector2f().zero(),
    var oldPosition: Vector2f = entity.position,
    var isStatic: Boolean = false,
    var computeVelocity: Boolean = true
) {

    val id: String
        get() = entity.uuid

    var position: Vector2f
        get() = entity.position
        set(value) {
            entity.position = value
        }

    val center: Vector2f
        get() = entity.center

    val scale: Vector2f
        get() = entity.scale

    val rotation: Float
        get() = entity.rotation

}