package k2ddt.physics.collision

import org.joml.Vector2f

class CollisionSolver {
    companion object {

        fun computeCollision(c0: CircleCollider, c1: CircleCollider) : CollisionVector? {

            val distanceVector = Vector2f(c0.body.position).sub(c1.body.position)
            val distance2 = distanceVector.length()
            val r2 = c0.radius + c1.radius
            val delta = distance2 - r2

            if (delta < 0) {
                return CollisionVector(c0.body.id, c1.body.id, distance2, distanceVector.normalize())
            }
            return null
        }

        fun computeCollision(c0: CircleCollider, b0: BoxCollider) : CollisionVector? {

            return null
        }

        fun computeCollision(b0: BoxCollider, b1: BoxCollider) : CollisionVector? {

            return null
        }

        fun computeStaticResolution(c0: CircleCollider, c1: CircleCollider, v: CollisionVector) {
            val dist = ((c0.radius + c1.radius) - v.distance) / 2f
            c0.body.apply {
                position.add(Vector2f(v.vector).mul(dist))
                computeVelocity = false
            }
            c1.body.apply {
                position.sub(Vector2f(v.vector).mul(dist))
                computeVelocity = false
            }
        }

        fun computeElasticCollision(c0: CircleCollider, c1: CircleCollider, v: CollisionVector) {

            //computeDynamicResolution(c0, c1, v)
            //return

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
            d: Float
        ): Vector2f {

            val massAux = 2 * m2 / (m1 + m2)

            val crossAux0 = Vector2f(v1).sub(v2)
            val crossAux1 = Vector2f(x1).sub(x2)
            val crossAux = crossAux0.dot(crossAux1) / (d * d)

            val distanceAux = Vector2f(crossAux1)

            val bigAux = distanceAux.mul(massAux * crossAux)
            return Vector2f(v1).sub(bigAux)
        }
    }
}