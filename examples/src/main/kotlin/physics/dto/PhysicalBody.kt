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
    val type: Type,
    var mass: Float = 1f,
    var restituition: Float = 1f,
    var velocity: Vector2f = Vector2f().zero(),
    var acceleration: Vector2f = Vector2f().zero(),
    var oldPosition: Vector2f = entity.position,
    var computeVelocity: Boolean = true
) {

    enum class Type(id: Int) {
        STATIC(0),
        VERLET(1),
        EULER(2)
    }

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