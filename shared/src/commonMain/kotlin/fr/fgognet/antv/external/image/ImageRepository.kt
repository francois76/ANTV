package fr.fgognet.antv.external.image

import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.UrlVfs
import io.github.aakira.napier.Napier
import io.ktor.http.*

private const val TAG = "ANTV/ImageRepository"

object ImageRepository {

    val imageCodeToBitmap: HashMap<String, Bitmap> = hashMapOf()


    suspend fun getLiveImage(image: String): Bitmap {
        Napier.v(
            "getLiveImage",
            tag = TAG
        )
        if (imageCodeToBitmap.contains(image)) {
            return imageCodeToBitmap[image]!!
        }
        Napier.i(
            "Calling $image",
            tag = TAG
        )
        val bitmap = UrlVfs(image.encodeURLPath()).readBitmap()
        imageCodeToBitmap[image] =
            bitmap
        return bitmap
    }
}