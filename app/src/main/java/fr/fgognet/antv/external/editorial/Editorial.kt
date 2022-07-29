package fr.fgognet.antv.external.editorial

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


@Root(name = "editorial", strict = false)
data class Editorial(
    @field:Element(name = "titre")
    @param:Element(name = "titre")
    val titre: String = "",
    @field:Element(name = "introduction")
    @param:Element(name = "introduction")
    val introduction: String = "",
    @field:ElementList(entry = "diffusion", required = false, inline = true)
    @param:ElementList(entry = "diffusion", required = false, inline = true)
    var diffusions: List<Diffusion>? = null

)

@Root(name = "diffusion", strict = false)
class Diffusion(
    @field:Element(name = "id_organe", required = false)
    @param:Element(name = "id_organe", required = false)
    var id_organe: String? = null,
    @field:Element(name = "libelle", required = false)
    @param:Element(name = "libelle", required = false)
    var libelle: String? = null,
    @field:Element(name = "libelle_court", required = false)
    @param:Element(name = "libelle_court", required = false)
    var libelle_court: String? = null,
    @field:Element(name = "heure", required = false)
    @param:Element(name = "heure", required = false)
    var heure: String? = null,
    @field:Element(name = "flux", required = false)
    @param:Element(name = "flux", required = false)
    var flux: Int? = null,
    @field:Element(name = "sujet", required = false)
    @param:Element(name = "sujet", required = false)
    var sujet: String? = null,
    @field:Element(name = "uid_referentiel", required = false)
    @param:Element(name = "uid_referentiel", required = false)
    var uid_referentiel: String? = null,
    @field:Element(name = "lieu", required = false)
    @param:Element(name = "lieu", required = false)
    var lieu: String? = null,
    @field:Element(name = "lieu_ref", required = false)
    @param:Element(name = "lieu_ref", required = false)
    var lieu_ref: String? = null,
    @field:Element(name = "programme_ratp", required = false)
    @param:Element(name = "programme_ratp", required = false)
    var programme_ratp: String? = null,
    @field:Element(name = "teledistribution", required = false)
    @param:Element(name = "teledistribution", required = false)
    var teledistribution: String? = null,
    @field:Element(name = "video_url", required = false)
    @param:Element(name = "video_url", required = false)
    var video_url: String? = null,
    @field:Element(name = "indexation", required = false)
    @param:Element(name = "indexation", required = false)
    var indexation: String? = null,
    @field:Element(name = "titre", required = false)
    @param:Element(name = "titre", required = false)
    var titre: String? = null,
    @field:Attribute(name = "dateCreation", required = false)
    @param:Attribute(name = "dateCreation", required = false)
    var dateCreation: String? = null,
    @field:Attribute(name = "dateModification", required = false)
    @param:Attribute(name = "dateModification", required = false)
    var dateModification: String? = null,
    @field:Attribute(name = "dateSuppression", required = false)
    @param:Attribute(name = "dateSuppression", required = false)
    var dateSuppression: String? = null,
    @field:Attribute(name = "id", required = false)
    @param:Attribute(name = "id", required = false)
    var id: String? = null,
    @field:Attribute(name = "utilisateur", required = false)
    @param:Attribute(name = "utilisateur", required = false)
    var utilisateur: Int? = null
) {


    fun getFormattedHour(): String {
        if (heure == null || heure == "") {
            return ""
        }

        val firstCharacter = heure?.substring(0, 1)
        if (firstCharacter == "0") {
            return heure?.substring(1, 2) + "h" + heure?.substring(2, 4)
        } else {
            return heure?.substring(0, 2) + "h" + heure?.substring(2, 4)
        }
    }
}
