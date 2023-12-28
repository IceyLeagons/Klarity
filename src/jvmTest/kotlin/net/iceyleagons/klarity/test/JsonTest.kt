package net.iceyleagons.klarity.test

import net.iceyleagons.klarity.jvm.Klarity
import net.iceyleagons.klarity.jvm.sources.JsonSource
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.test.Test

class JsonTest {

    companion object {

        var path: Path? = null

        @JvmStatic
        @BeforeAll
        fun init() {
            path = Files.createTempFile("temporary.json", null)

            Klarity.configure {
                sourceManagement {
                    registerSource("en", JsonSource(path!!))
                }
            }
        }
    }

    @Test
    fun testDefaultValueAppending() {
        Klarity.translate("test.path", "This is the default value.")
        Files.newInputStream(path!!).use {
            // Somehow JSONOObject(it) does not read in the file...
            Scanner(it).use { scanner ->
                val json = JSONObject(scanner.nextLine())
                assertEquals("This is the default value.", json.getJSONObject("test").getString("path"))
            }
        }
    }
}