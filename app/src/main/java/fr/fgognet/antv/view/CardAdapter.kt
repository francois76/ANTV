package fr.fgognet.antv.view

import android.widget.Button
import android.widget.TextView
import fr.fgognet.antv.view.card.CardData

private const val TAG = "ANTV/CardAdapter"


class CardAdapter<T : CardData>(private val buildCard: (T, TextView, Button) -> Unit)
