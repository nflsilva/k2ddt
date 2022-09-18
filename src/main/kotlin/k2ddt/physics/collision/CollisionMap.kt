package k2ddt.physics.collision

import org.joml.Vector2i
import kotlin.math.ceil
import kotlin.math.floor

class CollisionMap() {

    val verticalChunkSize = 64
    val horizontalChunkSize = 64

    private val activeChunks = mutableMapOf<String, Vector2i>()
    private val chunksPerBody = mutableMapOf<String, MutableSet<Vector2i>>()
    private val map: MutableMap<Int, MutableMap<Int, MutableSet<String>>> = mutableMapOf()

    fun updateColliderChunks(collider: Collider) {
        removeCollider(collider)
        addCollider(collider)
    }

    fun getChunksWithColliders(): List<Vector2i> {
        return activeChunks.values.toList()
    }

    fun getCollidersAt(row: Int, column: Int): List<String> {
        return map[row]?.get(column)?.toList() ?: listOf()
    }

    fun addCollider(collider: Collider) {
        val minRow = floor(collider.aabb.bottom / verticalChunkSize).toInt()
        val maxRow = ceil(collider.aabb.top / verticalChunkSize).toInt()

        val minColumn = floor(collider.aabb.left / horizontalChunkSize).toInt()
        val maxColumn = ceil(collider.aabb.right / horizontalChunkSize).toInt()

        var chunkPerBody = chunksPerBody[collider.body.id]
        if (chunkPerBody == null) {
            chunkPerBody = mutableSetOf()
            chunksPerBody[collider.body.id] = chunkPerBody
        }

        for (r in minRow until maxRow) {
            for (c in minColumn until maxColumn) {

                var row = map[r]
                if (row == null) {
                    row = mutableMapOf()
                    map[r] = row
                }

                var chunk = row[c]
                if (chunk == null) {
                    chunk = mutableSetOf()
                    row[c] = chunk
                }

                chunk.add(collider.body.id)
                if(chunk.size > 1) {
                    activeChunks["$c:$r"] = Vector2i(c, r)
                }

                chunkPerBody.add(Vector2i(c, r))
            }
        }
    }

    fun removeCollider(collider: Collider) {
        val chunks = chunksPerBody[collider.body.id] ?: return
        chunks.forEach {
            val bodiesInChunk = map[it.y]?.get(it.x)
            bodiesInChunk?.remove(collider.body.id)
            if (bodiesInChunk?.isEmpty() == true) {
                activeChunks.remove("${it.y}:${it.x}")
            }
        }
    }

    fun debug() {
        for (r in map.keys) {
            print("[$r]")
            for (c in map[r]!!.keys) {
                print("${map[r]!![c]!!.size} ")
            }
            println("")
        }
        println("=========")
    }

}