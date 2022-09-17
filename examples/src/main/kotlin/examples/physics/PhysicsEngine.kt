package examples.physics

import examples.physics.collision.*
import examples.physics.dto.*
import examples.physics.moviment.MovementSolver
import org.joml.Vector2f

class PhysicsEngine() {

    private val step = 0.1f
    private val nUpdates = 1

    private val physicalBodies = mutableMapOf<String, PhysicalBody>()
    val collisionMap = CollisionMap()
    private val colliders = mutableMapOf<String, Collider>()

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
            body.acceleration.add(Vector2f(force).div(body.mass))
        }
    }

    private fun updatePhysicalBodies() {
        physicalBodies.keys.forEach { id ->
            physicalBodies[id]?.let {
                MovementSolver.computeVerlet(it, step)
            }
        }
    }

    //** Collisions **//
    fun createCollider(id: String, radius: Float) {
        val body = physicalBodies[id] ?: return
        val c = CircleCollider(body, radius)
        colliders[id] = c
        collisionMap.addCollider(c)
    }

    private fun updateCollisionMap() {
        for(c in colliders.values) {
            collisionMap.updateColliderChunks(c)
        }
    }

    private fun computeCollisions() {
        val activeChunks = collisionMap.getChunksWithColliders()
        for(chunk in activeChunks) {

            val collidersInChunk = collisionMap.getCollidersAt(chunk.y, chunk.x)
            if (collidersInChunk.size <= 1) continue

            for (b0i in collidersInChunk.indices) {

                val uuid0 = collidersInChunk[b0i]
                for (b1i in b0i + 1 until collidersInChunk.size) {
                    val uuid1 = collidersInChunk[b1i]

                    val collider0 = colliders[uuid0] as? CircleCollider ?: continue
                    val collider1 = colliders[uuid1] as? CircleCollider ?: continue
                    CollisionSolver.computeCollision(collider0, collider1)?.let {
                        collider0.collisionVectors["$uuid0:$uuid1"] = it
                    }
                }
            }
        }
    }

    private fun updateCollidingBodies() {
        for(collider in colliders.values) {
            for(v in collider.collisionVectors) {
                val c0 = colliders[v.value.id0] as? CircleCollider ?: continue
                val c1 = colliders[v.value.id1] as? CircleCollider ?: continue
                CollisionSolver.computeElasticCollision(c0, c1, v.value)
                //println("${c0.body.id} ${c1.body.id}")
            }
            collider.collisionVectors.clear()
        }
        //println("=====")
    }
}