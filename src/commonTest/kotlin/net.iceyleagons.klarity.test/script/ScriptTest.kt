/*
 * MIT License
 *
 * Copyright (c) 2023 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.klarity.test.script

import net.iceyleagons.klarity.DefaultValueSource
import net.iceyleagons.klarity.KlarityAPI
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ScriptTest {

    @BeforeTest
    fun init() {
        KlarityAPI.configure {
            sourceManagement {
                registerSource("en", DefaultValueSource())
            }
            withGlobals {
                globalParameters {
                    "globalA" to "YES"
                }
                globalParameter("globalB", "YES2")
                globalParameters(mapOf("globalC" to "YES3"))
            }
        }
    }

    @Test
    fun testGlobalParameter() {
        val expected = "Works: YES YES2 YES3"
        val actual = KlarityAPI.translate("test", "Works: {globalA} {globalB} {globalC}")

        assertEquals(expected, actual)
    }

    @Test
    fun testValues() {
        val expected = "This is a test not a production"
        val actual = KlarityAPI.translate("test", "This is a {name} not a {name2}", mapOf(Pair("name", "test"), Pair("name2", "production")))

        assertEquals(expected, actual)
    }

    @Test
    fun testParsing() {
        val expected = "You have 2 apples"
        val actual = KlarityAPI.translate(
            "path.to.translation",
            "You have {IF(EQ(amount, '1'), IF(SW(item, 'a', 'e', 'i', 'o', 'u'), 'an', 'a'), amount)} {item}{IF(GT(amount, '1'), 's', '')}",
            mapOf(
                Pair("amount", "2"),
                Pair("item", "apple")
            )
        )

        assertEquals(expected, actual)
    }

    @Test
    fun testParsing2() {
        val expected = "You have an apple"
        val actual = KlarityAPI.translate(
            "path.to.translation",
            "You have {IF(EQ(amount, '1'), IF(SW(item, 'a', 'e', 'i', 'o', 'u'), 'an', 'a'), amount)} {item}{IF(GT(amount, '1'), 's', '')}",
            mapOf(
                Pair("amount", "1"),
                Pair("item", "apple")
            )
        )

        assertEquals(expected, actual)
    }

    @Test
    fun testParsing3() {
        val expected = "You have a pear"
        val actual = KlarityAPI.translate(
            "path.to.translation",
            "You have {IF(EQ(amount, '1'), IF(SW(item, 'a', 'e', 'i', 'o', 'u'), 'an', 'a'), amount)} {item}{IF(GT(amount, '1'), 's', '')}",
            mapOf(
                Pair("amount", "1"),
                Pair("item", "pear")
            )
        )

        assertEquals(expected, actual)
    }

    @Test
    fun testParsing4() {
        val expected = "You have 2 pears"
        val actual = KlarityAPI.translate(
            "path.to.translation",
            "You have {IF(EQ(amount, '1'), IF(SW(item, 'a', 'e', 'i', 'o', 'u'), 'an', 'a'), amount)} {item}{IF(GT(amount, '1'), 's', '')}",
            mapOf(
                Pair("amount", "2"),
                Pair("item", "pear")
            )
        )

        assertEquals(expected, actual)
    }
}