package fr.fgognet.antv.external.editorial

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlChildrenName
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@XmlSerialName("editorial", "", "")
@Serializable
data class Editorial(

    @XmlElement(true)
    var titre: String = "",
    @XmlElement(true)
    var introduction: String = "",
    @XmlElement(true)
    @XmlChildrenName("diffusion", "", "")
    var diffusions: List<Diffusion>? = null

)

@Serializable
class Diffusion(
    @XmlElement(true)
    var id_organe: String? = null,
    @XmlElement(true)
    var libelle: String? = null,
    @XmlElement(true)
    var libelle_court: String? = null,
    @XmlElement(true)
    var heure: String? = null,
    @XmlElement(true)
    var flux: Int? = null,
    @XmlElement(true)
    var sujet: String? = null,
    @XmlElement(true)
    var uid_referentiel: String? = null,
    @XmlElement(true)
    var lieu: String? = null,
    @XmlElement(true)
    var lieu_ref: String? = null,
    @XmlElement(true)
    var programme_ratp: String? = null,
    @XmlElement(true)
    var teledistribution: String? = null,
    @XmlElement(true)
    var video_url: String? = null,
    @XmlElement(true)
    var indexation: String? = null,
    @XmlElement(true)
    var titre: String? = null,
    @XmlElement(false)
    var dateCreation: String? = null,
    @XmlElement(false)
    var dateModification: String? = null,
    @XmlElement(false)
    var dateSuppression: String? = null,
    @XmlElement(false)
    var id: String? = null,
    @XmlElement(false)
    var utilisateur: Int? = null
) {


    fun getFormattedHour(): String {
        if (heure == null || heure == "") {
            return ""
        }

        var firstCharacter = heure?.substring(0, 1)
        if (firstCharacter == "0") {
            return heure?.substring(1, 2) + "h" + heure?.substring(2, 4)
        } else {
            return heure?.substring(0, 2) + "h" + heure?.substring(2, 4)
        }
    }
}
