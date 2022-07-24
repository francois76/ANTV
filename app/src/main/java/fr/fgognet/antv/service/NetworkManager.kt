package fr.fgognet.antv.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import fr.fgognet.antv.Diffusion
import fr.fgognet.antv.Editorial
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.net.URL
import java.time.LocalDateTime

enum class Environment {
    NOTHING, // no current stream
    FIXED, // fixed on 13 july 2022
    REAL_TIME // current live
}

val TAG = "ANTV/NetworkManager"

object NetworkManager {


    private val currentEnvironment = Environment.FIXED

    var imageCodeToBitmap: HashMap<String, Bitmap> = hashMapOf()


    fun getLiveImage(image: String): Bitmap {
        Log.d(TAG, "getLiveImage")
        if (imageCodeToBitmap.contains(image)) {
            return imageCodeToBitmap[image]!!
        }
        return BitmapFactory.decodeStream(
            URL(image).openStream()
        )
    }

    fun getEditorialInfos(): Editorial {
        Log.d(TAG, "getEditorialInfos")
        when (currentEnvironment) {
            Environment.NOTHING -> return Editorial(
                "Titre mock nothing",
                "Programme du jour",
                arrayListOf()
            )
            Environment.FIXED -> return Editorial(
                "Mercredi 13 juillet 2022",
                "Programme du jour",
                arrayListOf(
                    Diffusion(
                        "59048",
                        "Commission des finances, de l'économie générale et du contrôle budgétaire",
                        "CION_FIN",
                        "0930",
                        26,
                        "" + LocalDateTime.now(),
                        "RUANR5L16S2022IDC445325",
                        "Salle 6350 – Palais Bourbon, 1er étage",
                        "SLANPBS6350",
                        "1",
                        "40",
                        "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8",
                        "FM/RR",
                        "titre1",
                        "1657711803",
                        "1657711803",
                        "1657711803",
                        "",
                        2
                    ),
                    Diffusion(
                        "419604",
                        "Commission des affaires culturelles et de l'éducation",
                        "CION-CEDU",
                        "0935",
                        26,
                        "- Table ronde sur la réforme du financement de l’audiovisuel public réunissant les responsables des sociétés et établissement concernés<br>",
                        "RUANR5L16S2022IDC445325",
                        "Salle 6350 – Palais Bourbon, 1er étage",
                        "SLANPBS6350",
                        "1",
                        "40",
                        "https://anorigin.vodalys.com/videos/definst/mp4/ida/domain1/2022/07/hemi_20220713144505.smil/chunklist_b1000000.m3u8",
                        "FM/RR",
                        "titre1",
                        "1657711803",
                        "1657711803",
                        "1657711803",
                        "",
                        2
                    )
                )
            )
            Environment.REAL_TIME -> {
                val serializer: Serializer = Persister()
                return serializer.read(
                    Editorial::class.java, URL(
                        "https://videos.assemblee-nationale.fr/php/getedito.php"
                    ).openStream()
                )
            }
        }
    }
}