package examples.physics.collision

import org.joml.Vector2f

class CollisionSolver {
    companion object {

        fun computeCollision(c0: CircleCollider, c1: CircleCollider) : CollisionVector? {

            val distanceVector = Vector2f(c0.body.position).sub(c1.body.position)
            val distance2 = distanceVector.lengthSquared()
            val r2 = c0.radius + c1.radius
            val delta = distance2 - (r2 * r2)

            if (delta < 0) {
                return CollisionVector(c0.body.id, c1.body.id, distance2, distanceVector.normalize())
            }
            return null
        }

        fun computeElasticCollision(c0: CircleCollider, c1: CircleCollider, v: CollisionVector) {

            val v0 = computeElasticVelocity(
                c0.body.position,
                c1.body.position,
                c0.body.mass,
                c1.body.mass,
                c0.body.velocity,
                c1.body.velocity,
                v.distance
            )

            val v1 = computeElasticVelocity(
                c1.body.position,
                c0.body.position,
                c1.body.mass,
                c0.body.mass,
                c1.body.velocity,
                c0.body.velocity,
                v.distance
            )

            c0.body.apply {
                velocity = v0
                computeVelocity = false
            }

            c1.body.apply {
                velocity = v1
                computeVelocity = false
            }
        }

        private fun computeElasticVelocity(
            x1: Vector2f,
            x2: Vector2f,
            m1: Float,
            m2: Float,
            v1: Vector2f,
            v2: Vector2f,
            d2: Float
        ): Vector2f {

            val massAux = 2 * m2 / (m1 + m2)

            val crossAux0 = Vector2f(v1).sub(v2)
            val crossAux1 = Vector2f(x1).sub(x2)
            val crossAux = crossAux0.dot(crossAux1) / d2

            val distanceAux = Vector2f(crossAux1)

            val bigAux = distanceAux.mul(massAux * crossAux)
            return Vector2f(v1).sub(bigAux)
        }
    }
}