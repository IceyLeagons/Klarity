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

package net.iceyleagons.klarity.script.functions.impl

import net.iceyleagons.klarity.script.functions.KlarityFunction
import net.iceyleagons.klarity.script.parsing.ListUtils
import net.iceyleagons.klarity.script.parsing.ParsingUtils
import net.iceyleagons.klarity.script.parsing.ParsingUtils.isFalse
import net.iceyleagons.klarity.script.parsing.ParsingUtils.isTrue
import net.iceyleagons.klarity.script.parsing.ScriptParser

class IfFunction(scriptParser: ScriptParser) : KlarityFunction("IF", scriptParser) {

    override fun parse(input: String): String {
        val functionList = ListUtils.parseFunctionList(ParsingUtils.getFunctionContent(input))
        if (functionList.size != 3) {
            throw IllegalStateException("Malformed function (IF). Expected 3 arguments, got ${functionList.size}")
        }

        val condition = scriptParser.parseFunction(functionList[0]!!)

        return if (condition.isTrue()) {
            scriptParser.parseFunction(functionList[1]!!)
        } else if (condition.isFalse()) {
            scriptParser.parseFunction(functionList[2]!!)
        } else {
            throw IllegalStateException("Got invalid return to condition value. Expected true/false, got $condition.")
        }
    }
}