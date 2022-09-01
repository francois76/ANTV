package fr.fgognet.antv.jetpackView.main

import fr.fgognet.antv.R

interface Route {
    val id: String
    val nameID: Int
    val iconID: Int
}

object LiveRoute : Route {
    override val id = "live"
    override val nameID = R.string.menu_live
    override val iconID = R.drawable.ic_baseline_live_tv_24
}

object PlaylistRoute : Route {
    override val id = "playlist"
    override val nameID = R.string.menu_playlist
    override val iconID = R.drawable.ic_baseline_ondemand_video_24
}

object SearchRoute : Route {
    override val id = "search"
    override val nameID = R.string.menu_search
    override val iconID = R.drawable.ic_baseline_search_24
}