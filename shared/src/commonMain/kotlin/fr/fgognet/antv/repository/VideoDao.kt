package fr.fgognet.antv.repository

import io.github.aakira.napier.Napier

private const val TAG = "ANTV/VideoDao"

data class VideoEntity(
    val url: String,
    val imageCode: String,
    val title: String,
    val description: String
)

private val data = hashMapOf<String, VideoEntity>()

object VideoDao {
    fun insert(entity: VideoEntity): String {
        Napier.v("insert", tag = TAG)
        Napier.d("insert $entity", tag = TAG)
        data[entity.title] = entity
        return entity.title
    }

    fun get(key: String): VideoEntity? {
        Napier.v("get key = $key return ${data[key]}", tag = TAG)
        return data[key]
    }
}

