package fr.fgognet.antv.view.card

import androidx.compose.runtime.Composable

@Composable
actual fun CompositeCardView(
    data: GenericCardData,
    buttonClicked: () -> Unit
) {
    LandscapeCompositeCardView(data = data, buttonClicked = buttonClicked)
}