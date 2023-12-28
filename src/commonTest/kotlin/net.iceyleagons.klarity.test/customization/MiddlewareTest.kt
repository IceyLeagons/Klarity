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
import net.iceyleagons.klarity.api.KlarityMiddleware
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class MiddlewareTest {

    @BeforeTest
    fun init() {
        val mid = TestMiddleware()
        KlarityAPI.configure {
            sourceManagement {
                registerSource("en", DefaultValueSource())
            }
            pluginManagement {
                registerMiddleware(mid)

                assertFails {
                    registerMiddleware(mid)
                }
            }
        }
    }

    @Test
    fun testPrefixSuffix() {
        val expected = "modified!"
        val actual = KlarityAPI.translate("test", "This should not be present when parsing.")

        assertEquals(expected, actual)
    }

    class TestMiddleware : KlarityMiddleware {
        override fun modify(input: String): String {
            return "modified!"
        }
    }
}