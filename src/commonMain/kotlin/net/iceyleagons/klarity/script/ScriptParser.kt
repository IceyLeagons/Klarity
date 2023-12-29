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

package net.iceyleagons.klarity.script

import net.iceyleagons.klarity.parsers.generated.StringCodeLexer
import net.iceyleagons.klarity.parsers.generated.StringCodeParser
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream

/**
 * This class is responsible for parsing any script inside the given input.
 * Instances should be created every time on new inputs.
 *
 * @property values the values that should be passed to the script functions
 * @property functions the dictionary of functions to use
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 23, 2023
 */
class ScriptParser(private val functions: Map<String, KlarityFunction>, private val values: Map<String, String>) {

    /**
     * @property CODE_PART_START defines the boundary of script parts
     * @property CODE_PART_END defines the boundary of script parts
     */
    companion object {
        private const val CODE_PART_START = '{'
        private const val CODE_PART_END = '}'
    }

    fun parse(input: String): String {
        if (!input.contains("{") || !input.contains("}")) {
            // saving computation
            return input
        }

        var index = 0
        var lastEnd = 0
        var csIndex = -1
        val result = StringBuilder()

        while (input.length > index) {
            val symbol = input[index]

            if (symbol == '{') {
                result.append(input.substring(lastEnd, index)) // adding everything that is not code
                csIndex = index + 1
            }

            if (symbol == '}') {
                val codePart = input.substring(csIndex, index)
                val parsed = parseCodePart(codePart)
                result.append(parsed)
                lastEnd = index + 1
                csIndex = 0
            }

            index += 1
        }

        if (csIndex == -1) {
            // we have never parsed a code
            return input
        }

        return result.toString()
    }

    private fun parseCodePart(codePart: String): String {
        return try {
            val lexer = StringCodeLexer(CharStreams.fromString(codePart))
            val tokens = CommonTokenStream(lexer)
            val parser = StringCodeParser(tokens)
            val ast = parser.program()
            val visitor = KlarityASTVisitor(values, functions)

            visitor.visit(ast).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            "ERROR"
        }
    }
}