package net.iceyleagons.klarity.test

import net.iceyleagons.klarity.DefaultValueSource
import net.iceyleagons.klarity.KlarityAPI
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SuffixPrefixTest {

    @BeforeTest
    fun init() {
        KlarityAPI.configure {
            sourceManagement {
                registerSource("en", DefaultValueSource())
            }
            withGlobals {
                prefix("[PREFIX] ")
                suffix(" [SUFFIX]")
            }
        }
    }

    @Test
    fun testPrefixSuffix() {
        val expected = "[PREFIX] Something [SUFFIX]"
        val actual = KlarityAPI.translate("test", "Something")

        assertEquals(expected, actual)
    }
}