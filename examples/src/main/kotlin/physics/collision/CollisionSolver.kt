package k2ddt.physics.collision

import k2ddt.core.ExecutionContext
import k2ddt.render.dto.Color
import k2ddt.render.dto.Shape
import k2ddt.render.dto.Transform
import org.joml.Math.clamp
import org.joml.Vector2f
import java.lang.Float.max
import java.lang.Float.min

class CollisionSolver {
    companion object {

        fun computeCollision(c0: CircleCollisionBox, c1: CircleCollisionBox): CollisionVector? {

            val distanceVector = Vector2f(c1.body.position).sub(c0.body.position)
            val distance = distanceVector.length()
            val r2 = c0.radius + c1.radius
            val delta = distance - r2

            if (delta < 0) {
                return CollisionVector(c0.body.id, c1.body.id, delta, distanceVector.normalize())
            }
            return null
        }

        fun computeCollision(c0: CircleCollisionBox, b0: RectangleCollisionBox): CollisionVector? {

            val circleCenter = Vector2f(c0.center)

            val ee = ExecutionContext.getInstance()
            val points = b0.getPoints()
            for (p in points) {
                ee.render(
                    Shape(Shape.Type.CIRCLE, Color(0f, 1f, 0f, 1f)),
                    Transform(p.x, p.y, 0f, 10f, 10f, 0, centered = true)
                )
            }

            val slope = (points[1].y - points[0].y) / (points[1].x - points[0].x)
            val isAxisAligned = slope == 0f || slope == Float.POSITIVE_INFINITY

            var mxi = 0f
            var myi = 0f
            var collisionVector: CollisionVector? = null
            var minDistance = 100000f

            if (isAxisAligned) {
                if (
                    c0.top > b0.bottom &&
                    c0.bottom < b0.bottom &&
                    !(c0.left > b0.right || c0.right < b0.left)
                ) {
                    val delta = c0.top - b0.bottom
                    return CollisionVector(
                        c0.body.id,
                        b0.body.id,
                        delta,
                        Vector2f(0f, -1f)
                    )
                } else if (
                    c0.bottom < b0.top &&
                    c0.top > b0.top &&
                    !(c0.left > b0.right || c0.right < b0.left)
                ) {
                    val delta = b0.top - c0.bottom
                    return CollisionVector(
                        c0.body.id,
                        b0.body.id,
                        delta,
                        Vector2f(0f, 1f)
                    )
                } else if (
                    c0.left < b0.right &&
                    c0.right > b0.right &&
                    !(c0.bottom > b0.top || c0.top < b0.bottom)
                ) {
                    val delta = b0.right - c0.left
                    return CollisionVector(
                        c0.body.id,
                        b0.body.id,
                        delta,
                        Vector2f(1f, 0f)
                    )
                } else if (
                    c0.right > b0.left &&
                    c0.left < b0.left &&
                    !(c0.bottom > b0.top || c0.top < b0.bottom)
                ) {
                    val delta = c0.right - b0.left
                    return CollisionVector(
                        c0.body.id,
                        b0.body.id,
                        delta,
                        Vector2f(-1f, 0f)
                    )
                }
            } else {
                for (i in points.indices) {
                    val pointA = Vector2f(points[i])
                    val pointB = Vector2f(points[(i + 1) % points.size])

                    val a1 = (pointB.y - pointA.y) / (pointB.x - pointA.x)
                    val b1 = pointB.y - a1 * pointB.x

                    val normal = Vector2f(-(pointB.y - pointA.y), pointB.x - pointA.x).normalize()
                    val a2 = normal.y / normal.x
                    val b2 = circleCenter.y - a2 * circleCenter.x

                    val minX = min(pointA.x, pointB.x)
                    val maxX = max(pointA.x, pointB.x)
                    val minY = min(pointA.y, pointB.y)
                    val maxY = max(pointA.y, pointB.y)

                    val xi = clamp(minX, maxX, (b2 - b1) / (a1 - a2))
                    val yi = clamp(minY, maxY, a2 * xi + b2)

                    val distanceVector = Vector2f(xi, yi).sub(circleCenter)
                    val distance = distanceVector.length()
                    val delta = c0.radius - distance

                    if (delta > 0) {
                        if (collisionVector == null || minDistance > distance) {
                            mxi = xi
                            myi = yi
                            minDistance = distance
                            collisionVector =
                                CollisionVector(
                                    c0.body.id,
                                    b0.body.id,
                                    delta,
                                    normal
                                )
                        }
                    }
                }
            }

            ee.render(
                Shape(Shape.Type.CIRCLE, Color(1f, 0f, 0f, 1f)),
                Transform(Vector2f(mxi, myi), 0f, Vector2f(10f), 0, centered = true)
            )


            return collisionVector
        }

        fun computeCollision(b0: RectangleCollisionBox, b1: RectangleCollisionBox): CollisionVector? {

            return null
        }

        fun computeStaticCollision(c0: CollisionBox, c1: CollisionBox, v: CollisionVector) {
            if (!c0.body.isStatic && !c1.body.isStatic)
                v.distanceDelta /= 2f

            if (!c0.body.isStatic) {
                c0.body.apply {
                    position.add(Vector2f(v.normal).mul(v.distanceDelta))
                    computeVelocity = false
                }
            }

            if (!c1.body.isStatic) {
                c1.body.apply {
                    position.sub(Vector2f(v.normal).mul(v.distanceDelta))
                    computeVelocity = false
                }
            }
        }

        fun computeDynamicCollision(c0: CollisionBox, c1: CollisionBox, v: CollisionVector) {

            var v0 = Vector2f(0f)
            var v1 = Vector2f(0f)
            if (!c0.body.isStatic && !c1.body.isStatic) {
                v0 = computeElasticVelocity(
                    c0.body.position,
                    c1.body.position,
                    c0.body.mass,
                    c1.body.mass,
                    c0.body.velocity,
                    c1.body.velocity
                )

                v1 = computeElasticVelocity(
                    c1.body.position,
                    c0.body.position,
                    c1.body.mass,
                    c0.body.mass,
                    c1.body.velocity,
                    c0.body.velocity
                )
            } else if (!c0.body.isStatic) {
                v0 = Vector2f(c0.body.velocity).sub(Vector2f(v.normal).mul(c0.body.velocity.dot(v.normal) * 2))
            } else if (!c1.body.isStatic) {
                v1 = Vector2f(c1.body.velocity).sub(Vector2f(v.normal).mul(c1.body.velocity.dot(v.normal) * 2))
            }

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
            x0: Vector2f,
            x1: Vector2f,
            m0: Float,
            m1: Float,
            v0: Vector2f,
            v1: Vector2f
        ): Vector2f {

            val d = Vector2f(x1).sub(x0).length()

            val massAux = 2 * m1 / (m0 + m1)

            val crossAux0 = Vector2f(v0).sub(v1)
            val crossAux1 = Vector2f(x0).sub(x1)
            val crossAux = crossAux0.dot(crossAux1) / (d * d)

            val distanceAux = Vector2f(crossAux1)

            val bigAux = distanceAux.mul(massAux * crossAux)
            return Vector2f(v0).sub(bigAux)
        }
    }
}