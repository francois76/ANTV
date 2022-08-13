package fr.fgognet.antv.external.image

import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.UrlVfs
import io.github.aakira.napier.Napier

private const val TAG = "ANTV/ImageRepository"

object ImageRepository {

    val imageCodeToBitmap: HashMap<String, Bitmap> = hashMapOf()


    suspend fun getLiveImage(image: String): Bitmap {
        Napier.v( "getLiveImage")
        if (imageCodeToBitmap.contains(image)) {
            return imageCodeToBitmap[image]!!
        }
        Napier.i( "Calling $image")
        val bitmap = UrlVfs(image).readBitmap()
        imageCodeToBitmap[image] =
            bitmap
        return bitmap
    }
}