package k2ddt.physics

import k2ddt.physics.collision.*
import k2ddt.physics.dto.PhysicalBody
import k2ddt.physics.moviment.MovementSolver
import org.joml.Vector2f

class PhysicsEngine() {

    private val step = 0.0025f
    private val nUpdates = 50

    private val physicalBodies = mutableMapOf<String, PhysicalBody>()
    private val collisionMap = CollisionMap()
    private val colliders = mutableMapOf<String, CollisionBox>()

    fun onUpdate() {
        for (updates in 0 until nUpdates) {
            updatePhysicalBodies()
            updateCollisionMap()
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

    fun applyForce(id: String, force: Vector2f) {
        physicalBodies[id]?.let { body ->
            body.acceleration.add(force.div(body.mass))
        }
    }

    private fun updatePhysicalBodies() {
        physicalBodies.keys.forEach { id ->
            physicalBodies[id]?.let {
                if(!it.isStatic)
                    MovementSolver.computeVerlet(it, step)
            }
        }
    }

    //** Collisions **//
    fun createCircleCollider(body: PhysicalBody) {
        val c = CircleCollisionBox(body)
        colliders[body.id] = c
        collisionMap.addCollider(c)
    }

    fun createBoxCollider(body: PhysicalBody) {
        val c = RectangleCollisionBox(body)
        colliders[body.id] = c
        collisionMap.addCollider(c)
    }

    private fun updateCollisionMap() {
        for (c in colliders.values) {
            collisionMap.updateColliderChunks(c)
        }
    }

    private fun computeCollisions() {
        val activeChunks = collisionMap.getChunksWithColliders()
        for (chunk in activeChunks) {

            val collidersInChunk = collisionMap.getCollidersAt(chunk.y, chunk.x)

            for (b0i in collidersInChunk.indices) {

                val uuid0 = collidersInChunk[b0i]
                for (b1i in b0i + 1 until collidersInChunk.size) {
                    val uuid1 = collidersInChunk[b1i]
                    val collider0 = colliders[uuid0] ?: continue
                    val collider1 = colliders[uuid1] ?: continue

                    /*
                    if (collider0.collisionVectors.keys.contains(uuid1) ||
                        !collider0.aabb.collidesWith(collider1.aabb)
                    ) continue*/

                    collider0.collideWith(collider1)
                }
            }
        }
    }

    private fun updateCollidingBodies() {
        for (collider in colliders.values) {
            for (v in collider.collisionVectors) {
                val c0 = colliders[v.value.id0]!!
                val c1 = colliders[v.value.id1]!!

                CollisionSolver.computeStaticCollision(c0, c1, v.value)
                CollisionSolver.computeDynamicCollision(c0, c1, v.value)
            }
            collider.collisionVectors.clear()
        }
    }
}