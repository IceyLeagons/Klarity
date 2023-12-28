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

package net.iceyleagons.klarity.test.customization

import net.iceyleagons.klarity.DefaultValueSource
import net.iceyleagons.klarity.KlarityAPI
import net.iceyleagons.klarity.api.FunctionProvider
import net.iceyleagons.klarity.script.functions.KlarityFunction
import net.iceyleagons.klarity.script.parsing.ScriptParser
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class FunctionTest {

    @BeforeTest
    fun init() {
        val functionProvider = FunctionProvider { TestFunction(it) }

        KlarityAPI.configure {
            sourceManagement {
                registerSource("en", DefaultValueSource())
            }
            pluginManagement {
                registerFunction(functionProvider)
                assertFails {
                    registerFunction(functionProvider)
                }
            }
        }
    }

    @Test
    fun testPrefixSuffix() {
        val expected = "working"
        val actual = KlarityAPI.translate("test", "{IF(TEST(), 'working', 'lol')}")

        assertEquals(expected, actual)
    }
}

class TestFunction(scriptParser: ScriptParser) : KlarityFunction("TEST", scriptParser) {

    override fun parse(input: String): String {
        return "true"
    }
}