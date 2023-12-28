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
import kotlin.test.assertContains
import kotlin.test.assertEquals

class FunctionTests {

    @BeforeTest
    fun init() {
        KlarityAPI.configure {
            sourceManagement {
                registerSource("en", DefaultValueSource())
            }
        }
    }

    @Test
    fun testGteq() {
        val value = "{IF(GTEQ(value, 2), 'matches', 'no match')}"

        assertEquals("matches", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "2"))))
        assertEquals("matches", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "3"))))
        assertEquals("no match", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "0"))))
        assertEquals("no match", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "1"))))
    }

    @Test
    fun testGT() {
        val value = "{IF(GT(value, 2), 'matches', 'no match')}"

        assertEquals("matches", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "3"))))
        assertEquals("no match", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "2"))))
        assertEquals("no match", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "1"))))
    }

    @Test
    fun testLT() {
        val value = "{IF(LT(value, 2), 'matches', 'no match')}"

        assertEquals("matches", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "1"))))
        assertEquals("no match", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "2"))))
        assertEquals("no match", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "3"))))
    }

    @Test
    fun testLTEQ() {
        val value = "{IF(LTEQ(value, 2), 'matches', 'no match')}"

        assertEquals("matches", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "1"))))
        assertEquals("matches", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "2"))))
        assertEquals("no match", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "3"))))
    }

    @Test
    fun testEW() {
        val value = "{IF(EW(value, 'f'), 'matches', 'no match')}"

        assertEquals("matches", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "asdf"))))
        assertEquals("matches", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "asdf"))))
        assertEquals("no match", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "asdfg"))))
    }

    @Test
    fun testEmpty() {
        val value = "{IF(ISEMPTY(value), 'matches', 'no match')}"

        assertEquals("matches", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", ""))))
        assertEquals("no match", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "asdfg"))))
    }

    @Test
    fun testNE() {
        val value = "{IF(NE(value, 'asdfg'), 'matches', 'no match')}"

        assertEquals("matches", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", ""))))
        assertEquals("no match", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("value", "asdfg"))))
    }

    @Test
    fun testAndTrueFalse() {
        assertEquals("matches", KlarityAPI.translate("path.to.translation", "{IF(AND(TRUE(), NOT(FALSE())), 'matches', 'no match')}"))
        assertEquals("no match", KlarityAPI.translate("path.to.translation", "{IF(AND(TRUE(), FALSE()), 'matches', 'no match')}"))
    }

    @Test
    fun testOr() {
        assertEquals("matches", KlarityAPI.translate("path.to.translation", "{IF(OR(TRUE(), FALSE()), 'matches', 'no match')}"))
        assertEquals("matches", KlarityAPI.translate("path.to.translation", "{IF(OR(TRUE(), TRUE()), 'matches', 'no match')}"))
        assertEquals("no match", KlarityAPI.translate("path.to.translation", "{IF(OR(FALSE(), FALSE()), 'matches', 'no match')}"))
    }

    @Test
    fun testConcat() {
        assertEquals("asd f", KlarityAPI.translate("path.to.translation", "{CONCAT('a','sd',' f')}"))
    }

    @Test
    fun testJoin() {
        assertEquals("hello;hello2", KlarityAPI.translate("path.to.translation", "{JOIN(';','hello','hello2')}"))
    }

    @Test
    fun testAdd() {
        assertEquals("2", KlarityAPI.translate("path.to.translation", "{ADD('1', '1')}"))
        assertEquals("4", KlarityAPI.translate("path.to.translation", "{ADD('1', '3')}"))
    }

    @Test
    fun testSub() {
        assertEquals("2", KlarityAPI.translate("path.to.translation", "{SUB('4', '2')}"))
        assertEquals("4", KlarityAPI.translate("path.to.translation", "{SUB('7', '3')}"))
    }

    @Test
    fun testMul() {
        assertEquals("2", KlarityAPI.translate("path.to.translation", "{MUL('1', '2')}"))
        assertEquals("4", KlarityAPI.translate("path.to.translation", "{MUL('2', '2')}"))
    }

    @Test
    fun testDiv() {
        assertEquals("2", KlarityAPI.translate("path.to.translation", "{DIV('4', '2')}"))
        assertEquals("4", KlarityAPI.translate("path.to.translation", "{DIV('8', '2')}"))
    }

    @Test
    fun testMod() {
        assertEquals("1", KlarityAPI.translate("path.to.translation", "{MOD('4', '3')}"))
        assertEquals("0", KlarityAPI.translate("path.to.translation", "{MOD('8', '4')}"))
    }

    @Test
    fun testRandom() {
        assertContains(KlarityAPI.translate("path.to.translation", "{RANDOM('asd', 'asd2')}"), "asd")
    }

    @Test
    fun testRegex() {
        val value = "{IF(MATCH('[a-z]+[0-9]', str), 'matches', 'no match')}"

        assertEquals("matches", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("str", "asd2"))))
        assertEquals("no match", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("str", "asd"))))
        assertEquals("no match", KlarityAPI.translate("path.to.translation", value, mapOf(Pair("str", "asd23"))))
    }
}