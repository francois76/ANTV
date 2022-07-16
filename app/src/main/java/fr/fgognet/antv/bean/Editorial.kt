package fr.fgognet.antv

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "editorial")
data class Editorial(
    @XmlElement(name = "titre")
    val titre: String = "",
    @XmlElement(name = "introduction")
    val introduction: String = "",
    @XmlElement(name = "element")
    var element: List<Diffusion>? = null

)

data class Diffusion(
    @XmlElement(name = "id_organe")
    var id_organe: String? = null,
    @XmlElement(name = "libelle")
    var libelle: String? = null,
    @XmlElement(name = "libelle_court")
    var libelle_court: String? = null,
    @XmlElement(name = "heure")
    var heure: String? = null,
    @XmlElement(name = "flux")
    var flux: Int? = null,
    @XmlElement(name = "sujet")
    var sujet: String? = null,
    @XmlElement(name = "uid_referentiel")
    var uid_referentiel: String? = null,
    @XmlElement(name = "lieu")
    var lieu: String? = null,
    @XmlElement(name = "lieu_ref")
    var lieu_ref: String? = null,
    @XmlElement(name = "programme_ratp")
    var programme_ratp: String? = null,
    @XmlElement(name = "teledistribution")
    var teledistribution: String? = null,
    @XmlElement(name = "video_url")
    var video_url: String? = null

)