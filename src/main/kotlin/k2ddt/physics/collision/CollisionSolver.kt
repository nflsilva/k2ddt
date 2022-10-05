package k2ddt.physics.collision

import k2ddt.core.ExecutionContext
import k2ddt.physics.dto.PhysicalBody
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
            val points = b0.getPoints()
            val topSlope = (points[1].y - points[0].y) / (points[1].x - points[0].x)
            val isAxisAligned = topSlope == 0f || topSlope == Float.POSITIVE_INFINITY

            val possibleIntersectionPoints = mutableListOf<Pair<Vector2f, Vector2f>>()
            if (isAxisAligned) {

                val cornerError = 0.0001f
                val clampX = clamp(b0.left + cornerError, b0.right - cornerError, c0.center.x)
                val clampY = clamp(b0.bottom + cornerError, b0.top - cornerError, c0.center.y)

                possibleIntersectionPoints.add(Pair(Vector2f(clampX, b0.top), Vector2f(0f, 1f)))
                possibleIntersectionPoints.add(Pair(Vector2f(clampX, b0.bottom), Vector2f(0f, -1f)))
                possibleIntersectionPoints.add(Pair(Vector2f(b0.left, clampY), Vector2f(-1f, 0f)))
                possibleIntersectionPoints.add(Pair(Vector2f(b0.right, clampY), Vector2f(1f, 0f)))

            } else {

                for (i in points.indices) {

                    val pointA = Vector2f(points[i])
                    val pointB = Vector2f(points[(i + 1) % points.size])

                    val a1 = (pointB.y - pointA.y) / (pointB.x - pointA.x)
                    val b1 = pointB.y - a1 * pointB.x

                    val normal = Vector2f(-(pointB.y - pointA.y), pointB.x - pointA.x).normalize()
                    val a2 = normal.y / normal.x
                    val b2 = circleCenter.y - a2 * circleCenter.x

                    val rxi = (b2 - b1) / (a1 - a2)
                    val rxy = a2 * rxi + b2

                    val minX = min(pointA.x, pointB.x)
                    val maxX = max(pointA.x, pointB.x)
                    val minY = min(pointA.y, pointB.y)
                    val maxY = max(pointA.y, pointB.y)

                    val xi = clamp(minX, maxX, rxi)
                    val yi = clamp(minY, maxY, rxy)

                    possibleIntersectionPoints.add(Pair(Vector2f(xi, yi), normal))
                }
            }

            var mxi = 0f
            var myi = 0f
            var collisionVector: CollisionVector? = null
            var minDistance = 100000f

            for (p in possibleIntersectionPoints) {

                val point = p.first
                val normal = p.second

                val distanceVector = Vector2f(point.x, point.y).sub(circleCenter)
                val distance = distanceVector.length()
                val delta = c0.radius - distance

                if (delta > 0) {
                    if (collisionVector == null || minDistance > distance) {
                        mxi = point.x
                        myi = point.y
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

            /*
            val ee = ExecutionContext.getInstance()
            for (p in points) {
                ee.render(
                    Shape(Shape.Type.CIRCLE, Color(0f, 1f, 0f, 1f)),
                    Transform(p.x, p.y, 0f, 10f, 10f, 0, centered = true)
                )
            }
            ee.render(
                Shape(Shape.Type.CIRCLE, Color(1f, 0f, 0f, 1f)),
                Transform(Vector2f(mxi, myi), 0f, Vector2f(10f), 0, centered = true)
            )
            */

            return collisionVector
        }

        fun computeCollision(b0: RectangleCollisionBox, b1: RectangleCollisionBox): CollisionVector? {

            return null
        }

        fun computeStaticCollision(c0: CollisionBox, c1: CollisionBox, v: CollisionVector) {
            if (c0.body.type != PhysicalBody.Type.STATIC && c1.body.type != PhysicalBody.Type.STATIC)
                v.distanceDelta /= 2f

            if (c0.body.type != PhysicalBody.Type.STATIC) {
                c0.body.apply {
                    position.add(Vector2f(v.normal).mul(v.distanceDelta))
                    computeVelocity = false
                }
            }

            if (c1.body.type != PhysicalBody.Type.STATIC) {
                c1.body.apply {
                    position.sub(Vector2f(v.normal).mul(v.distanceDelta))
                    computeVelocity = false
                }
            }
        }

        fun computeDynamicCollision(c0: CollisionBox, c1: CollisionBox, v: CollisionVector) {

            var v0 = Vector2f(0f)
            var v1 = Vector2f(0f)
            if (c0.body.type != PhysicalBody.Type.STATIC && c1.body.type != PhysicalBody.Type.STATIC) {
                v0 = computeVelocityOld(
                    c0.body.position,
                    c1.body.position,
                    c0.body.mass,
                    c1.body.mass,
                    c0.body.velocity,
                    c1.body.velocity
                )

                v1 = computeVelocityOld(
                    c1.body.position,
                    c0.body.position,
                    c1.body.mass,
                    c0.body.mass,
                    c1.body.velocity,
                    c0.body.velocity
                )
            } else if (c0.body.type != PhysicalBody.Type.STATIC) {
                v0 = Vector2f(c0.body.velocity)
                    .sub(Vector2f(v.normal)
                        .mul(c0.body.velocity.dot(v.normal) * 2))
                    .mul(c0.body.restituition)
            } else if (c1.body.type != PhysicalBody.Type.STATIC) {
                v1 = Vector2f(c1.body.velocity)
                    .sub(Vector2f(v.normal)
                        .mul(c1.body.velocity.dot(v.normal) * 2))
                    .mul(c1.body.restituition)
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

        private fun computeVelocityOld(
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

        private fun computeVelocity(
            ma: Float,
            mb: Float,
            ua: Vector2f,
            ub: Vector2f,
            cr: Float
        ): Vector2f {

            // v' = Cr * mb (vb - va) + ma * va + mb * vb
            val ubua = Vector2f(ub).sub(ua)
            val mava = Vector2f(ua).mul(ma)
            val mbvb = Vector2f(ub).mul(mb)
            val mamb = ma + mb

            val numerator = Vector2f(ubua).mul(cr * mb).add(mava).add(mbvb)

            return numerator.div(mamb)
        }

    }
}