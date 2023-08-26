package fr.fgognet.antv.utils

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.text.ExperimentalTextApi

@OptIn(ExperimentalTextApi::class)
@Preview
@Composable
fun renderHtml() {
    val result = """
                ANTV <br/>
                ANTV est une <i>application</i> développée par François GOGNET. <br/>
                Le code  est disponible sur <a href="https://github.com/francois76/ANTV">github</a><br/>
                les images d'illustration ainsi que les différents contenus sont directement <b>affichés</b>
                depuis le site  <a href="https://videos.assemblee-nationale.fr">de l'assemblée nationale</a>,
                 aucune ressource appartenant  à l'assemblée nationale n\'a été intégrée à cette application
                <a href="https://www.assemblee-nationale.fr/dyn/info-site">Crédits du site de l'assemblée nationale</a>
    """
    HtmlText(htmlText = result)
}