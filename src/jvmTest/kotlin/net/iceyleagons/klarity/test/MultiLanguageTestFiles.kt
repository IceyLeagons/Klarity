package net.iceyleagons.klarity.test

import net.iceyleagons.klarity.DefaultValueSource
import net.iceyleagons.klarity.jvm.Klarity
import net.iceyleagons.klarity.jvm.sources.PropertiesSource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.file.Path

class MultiLanguageTestFiles {

    companion object {
        @JvmStatic
        @BeforeAll
        fun init() {
            Klarity.configure {
                sourceManagement {
                    registerSource("en", PropertiesSource(Path.of("./testFiles/en.properties")))
                    registerSource("hu", PropertiesSource(Path.of("./testFiles/hu.properties")))

                    assertThrows<IllegalStateException> {
                        registerSource("en", DefaultValueSource())
                    }
                }

                defaultLanguage("hu")
            }
        }
    }

    @Test
    @DisplayName("Test default language")
    fun testDefault() {
        val expected = "testHU"
        val actual = Klarity.translate("test", "testEN")

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Test language: en")
    fun testEn() {
        val expected = "testEN"
        val actual = Klarity.translate("test", "testEN", language = "en")

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Test language: hu")
    fun testHu() {
        val expected = "testHU"
        val actual = Klarity.translate("test", "testEN", language = "hu")

        assertEquals(expected, actual)
    }

}