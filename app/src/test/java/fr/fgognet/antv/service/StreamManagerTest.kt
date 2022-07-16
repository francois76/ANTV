package fr.fgognet.antv.service

import fr.fgognet.antv.Editorial
import junit.framework.Assert.assertEquals
import org.junit.Test


internal class StreamManagerTest {

    @Test
    fun getLiveInfos() {
        assertEquals("coucou", StreamManager.getLiveInfos(), Editorial("", ""))
    }
}