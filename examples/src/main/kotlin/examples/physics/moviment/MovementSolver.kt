package examples.physics.moviment

import examples.physics.dto.PhysicalBody
import org.joml.Vector2f
import kotlin.Boolean

class MovementSolver {
    companion object {
        fun computeVerlet(
            body: PhysicalBody,
            deltaTime: Float) {

            val newPrevPosition = Vector2f(body.position)
            val dtt = Vector2f(body.acceleration).mul(deltaTime * deltaTime)

            if(body.computeVelocity) {
                body.velocity = Vector2f(body.position)
                    .sub(body.oldPosition)
            }
            body.computeVelocity = true

            body.position.add(body.velocity).add(dtt)
            body.oldPosition = newPrevPosition
            body.acceleration.zero()
        }

    }
}