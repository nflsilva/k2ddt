package k2ddt.physics

import k2ddt.physics.collision.*
import k2ddt.physics.dto.PhysicalBody
import k2ddt.physics.moviment.MovementSolver
import org.joml.Vector2f

class PhysicsEngine() {

    private val step = 0.01f
    private val nUpdates = 10

    private val physicalBodies = mutableMapOf<String, PhysicalBody>()
    private val collisionBoxes = mutableMapOf<String, CollisionBox>()

    fun onUpdate() {
        for (updates in 0 until nUpdates) {
            updatePhysicalBodies()
            computeCollisions()
            updateCollidingBodies()
        }
    }

    //** Movement **//
    fun createPhysicalBody(body: PhysicalBody) {
        physicalBodies[body.id] = body
    }

    fun getPhysicalBody(id: String): PhysicalBody? {
        return physicalBodies[id]
    }

    fun removePhysicalBody(id: String) {
        collisionBoxes.remove(id)
        physicalBodies.remove(id)
    }

    fun applyForce(body: PhysicalBody, force: Vector2f) {
        physicalBodies[body.id]?.let {
            it.acceleration.add(force.div(it.mass))
        }
    }

    private fun updatePhysicalBodies() {
        physicalBodies.keys.forEach { id ->
            physicalBodies[id]?.let {
                when(it.type) {
                    PhysicalBody.Type.VERLET -> MovementSolver.computeVerlet(it, step)
                    PhysicalBody.Type.EULER -> MovementSolver.computeVerlet(it, step)
                    else -> return
                }
            }
        }
    }

    //** Collisions **//
    fun createCircleCollider(body: PhysicalBody, onCollisionCallback: ((other: String) -> Unit)?) {
        val c = CircleCollisionBox(body, onCollisionCallback)
        collisionBoxes[body.id] = c
    }

    fun createBoxCollider(body: PhysicalBody, onCollisionCallback: ((other: String) -> Unit)?) {
        val c = RectangleCollisionBox(body, onCollisionCallback)
        collisionBoxes[body.id] = c
    }

    fun getCollisions(id: String): List<CollisionVector> {
        return collisionBoxes[id]?.collisionVectors?.toList() ?: listOf()
    }

    private fun computeCollisions() {
        val boxes = collisionBoxes.values.toList()
        for (i0 in boxes.indices) {
            val cb0 = boxes[i0]
            for (i1 in i0 + 1 until boxes.size) {
                val cb1 = boxes[i1]
                cb0.update()
                cb1.update()
                cb0.collideWith(cb1)
            }
        }

    }

    private fun updateCollidingBodies() {
        for (collider in collisionBoxes.values) {
            for (v in collider.collisionVectors) {
                val c0 = collisionBoxes[v.thisId]!!
                val c1 = collisionBoxes[v.otherId]!!

                CollisionSolver.computeStaticCollision(c0, c1, v)
                CollisionSolver.computeDynamicCollision(c0, c1, v)
            }
            collider.collisionVectors.clear()
        }
    }
}