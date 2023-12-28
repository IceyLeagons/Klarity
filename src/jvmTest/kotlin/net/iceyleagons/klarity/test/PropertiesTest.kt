package net.iceyleagons.klarity.test

import net.iceyleagons.klarity.jvm.Klarity
import net.iceyleagons.klarity.jvm.sources.PropertiesSource
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.test.assertEquals

class PropertiesTest {

    companion object {

        var path: Path? = null

        @JvmStatic
        @BeforeAll
        fun init() {
            path = Files.createTempFile("temporary.properties", null)

            Klarity.configure {
                sourceManagement {
                    registerSource("en", PropertiesSource(path!!))
                }
            }
        }
    }

    @Test
    fun testDefaultValueAppending() {
        Klarity.translate("test.path", "This is the default value.")
        Files.newInputStream(path!!).use {
            val prop = Properties()
            prop.load(it)
            assertEquals("This is the default value.", prop.getProperty("test.path"))
        }
    }
}