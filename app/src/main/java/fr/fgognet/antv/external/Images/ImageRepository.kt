package fr.fgognet.antv.external.Images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.net.URL

private const val TAG = "ANTV/ImageRepository"

object ImageRepository {

    var imageCodeToBitmap: HashMap<String, Bitmap> = hashMapOf()


    fun getLiveImage(image: String): Bitmap {
        Log.v(TAG, "getLiveImage")
        if (imageCodeToBitmap.contains(image)) {
            return imageCodeToBitmap[image]!!
        }
        Log.d(TAG, "fetching image $image")
        return BitmapFactory.decodeStream(
            URL(image).openStream()
        )
    }
}