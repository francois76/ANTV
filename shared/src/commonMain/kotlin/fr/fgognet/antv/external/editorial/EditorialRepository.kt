package fr.fgognet.antv.external.editorial

import fr.fgognet.antv.config.Config
import fr.fgognet.antv.config.Environment
import fr.fgognet.antv.config.httpClient
import fr.fgognet.antv.config.no_handler
import io.github.aakira.napier.Napier
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.core.XmlVersion
import nl.adaptivity.xmlutil.serialization.XML

private const val TAG = "ANTV/EditorialRepository"

object EditorialRepository {

    @OptIn(ExperimentalXmlUtilApi::class)
    suspend fun getEditorialInformation(): Editorial {
        Napier.v("getEditorialInformation")
        when (Config.currentEnvironment) {
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
                        "" + Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
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
                        2,
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
                        2,
                    )
                )
            )
            Environment.REAL_TIME -> {
                val client = httpClient()
                Napier.i("Calling https://videos.assemblee-nationale.fr/php/getedito.php")
                val resultString =
                    client.request("https://videos.assemblee-nationale.fr/php/getedito.php")
                        .body<String>()
                val format = XML {
                    xmlVersion = XmlVersion.XML10
                    xmlDeclMode = XmlDeclMode.Charset
                    unknownChildHandler = no_handler
                }
                return format.decodeFromString(resultString)
            }
        }
    }
}