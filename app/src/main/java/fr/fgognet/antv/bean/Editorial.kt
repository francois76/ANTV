package fr.fgognet.antv

data class Editorial(
    val titre: String? = null,
    val introduction: String? = null,
    val element: List<Diffusion>? = null

)

data class Diffusion(
    val key: String? = null,
    val value: String? = null
)