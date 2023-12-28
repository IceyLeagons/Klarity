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

package net.iceyleagons.klarity.script.parsing

import net.iceyleagons.klarity.script.parsing.ScriptParser.Companion.CODE_PART_END
import net.iceyleagons.klarity.script.parsing.ScriptParser.Companion.CODE_PART_START
import net.iceyleagons.klarity.script.parsing.ScriptParser.Companion.FUNC_PART_END
import net.iceyleagons.klarity.script.parsing.ScriptParser.Companion.FUNC_PART_START
import net.iceyleagons.klarity.script.parsing.ScriptParser.Companion.STRING_PART_INDICATOR

/**
 * A class that parses the body of a function in a script.
 *
 * @property currentIndex the current index of the input string
 * @property stringPartIndex the index of the apostrophe (string part) in the input string
 * @property codeStartIndex the index of the code part start symbol (`{`) in the input string
 * @property codeEndIndex the index of the code part end symbol (`}`) in the input string
 * @property functionStartIndex the index of the function part start symbol (`(`) in the input string
 * @property functionEndIndex the index of the function part end symbol (`)`) in the input string
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 23, 2023
 */
class TraversingFunctionBodyParser {

    private var currentIndex = 0
    private var stringPartIndex = 0 // apostrophe (string part) index
    private var codeStartIndex = 0 //code part start index
    private var codeEndIndex = 0 //code part end index
    private var functionStartIndex = 0 //function part start index
    private var functionEndIndex = 0 //function part end index

    /**
     * Gets the body of a function from an input string, starting from a given index.
     *
     * @param input the input string to parse
     * @param start the index to start parsing from
     * @return the body of the function as a substring of the input string
     * @throws IllegalStateException if the input string is not valid or does not contain a complete function body
     */
    fun getBody(input: String, start: Int): String {
        currentIndex = start
        stringPartIndex = 0
        codeStartIndex = 0
        codeEndIndex = 0
        functionStartIndex = 0
        functionEndIndex = 0

        while (input.length > currentIndex) {
            val symbol = input[currentIndex]

            if (symbol == '\\') {
                handleEscaping(input)
                continue
            }

            when (symbol) {
                STRING_PART_INDICATOR -> stringPartIndex += 1
                CODE_PART_START -> codeStartIndex += 1
                CODE_PART_END -> codeEndIndex += 1
                FUNC_PART_START -> functionEndIndex += 1
                FUNC_PART_END -> functionStartIndex += 1


                ',' ->
                    if (shouldReturn())
                        return input.substring(start, currentIndex)


                else -> {
                    currentIndex++
                    continue
                }
            }

            if (shouldReturn()) {
                return input.substring(start, currentIndex + 1)
            }

            currentIndex += 1
        }

        if (!shouldReturn()) {
            throw IllegalStateException()
        }

        return input.substring(start)
    }

    /**
     * Handles escaping characters in the input string.
     *
     * @param input the input string to parse
     */
    private fun handleEscaping(input: String) {
        if (currentIndex + 1 == input.length) {
            throw IllegalStateException("Unexpected end of input at escaping.")
        }

        println(currentIndex)
        if (ParsingUtils.isSpecialChar(input[currentIndex + 1])) {
            currentIndex -= 1
        }

        println(currentIndex)
        currentIndex += 2
        println(currentIndex)
    }

    /**
     * Checks if the parsing should return based on the current indices.
     *
     * @return true if the parsing should return, false otherwise
     */
    private fun shouldReturn(): Boolean =
        stringPartIndex >= 0
                && codeStartIndex >= 0
                && functionStartIndex >= 0
                && stringPartIndex % 2 == 0
                && codeStartIndex == codeEndIndex
                && functionStartIndex == functionEndIndex

    companion object {

        /**
         * Gets the body of a function from an input string, starting from a given index.
         *
         * @param input the input string to parse
         * @param start the index to start parsing from
         * @return the body of the function as a substring of the input string
         */
        fun getBody(input: String, start: Int): String {
            return TraversingFunctionBodyParser().getBody(input, start)
        }
    }
}