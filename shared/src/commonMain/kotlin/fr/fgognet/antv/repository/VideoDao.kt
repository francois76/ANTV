package fr.fgognet.antv.repository

import io.github.aakira.napier.Napier

private const val TAG = "ANTV/VideoDao"

data class VideoEntity(
    val url: String,
    val imageCode: String,
    val title: String,
    val description: String
)


object VideoDao {
    private val data = hashMapOf<String, VideoEntity>()

    fun insert(entity: VideoEntity): String {
        Napier.v(tag = TAG, message = "insert")
        Napier.d(tag = TAG, message = "insert $entity")
        data[entity.title] = entity
        return entity.title
    }

    fun get(key: String): VideoEntity? {
        Napier.v(tag = TAG, message = "get key = $key return ${data[key]}")
        return data[key]
    }
}

