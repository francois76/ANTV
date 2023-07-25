package fr.fgognet.antv.widget

expect class MediaController {

    fun isInit(): Boolean
    fun seekBack()
    fun seekForward()
    fun pause()
    fun play()
    fun seekTo(toLong: Long)
}