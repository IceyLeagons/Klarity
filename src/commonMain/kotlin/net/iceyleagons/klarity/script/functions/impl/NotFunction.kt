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
import net.iceyleagons.klarity.script.parsing.ParsingUtils
import net.iceyleagons.klarity.script.parsing.ParsingUtils.toScriptRepresentation
import net.iceyleagons.klarity.script.parsing.ScriptParser

class NotFunction(scriptParser: ScriptParser) : KlarityFunction("NOT", scriptParser) {

    override fun parse(input: String): String {
        val content = ParsingUtils.getFunctionContent(input)
        if (content == "true" || content == "false") throw IllegalStateException("Expected function. Use TRUE() or FALSE() instead!")

        val parsed = scriptParser.parseFunction(content)
        if (!(parsed == "true" || parsed == "false")) {
            throw IllegalStateException("Illegal return type given to function. Expected boolean!")
        }

        return (!parsed.toBoolean()).toScriptRepresentation()
    }
}