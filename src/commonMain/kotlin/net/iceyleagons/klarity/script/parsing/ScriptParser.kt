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

import net.iceyleagons.klarity.KlarityAPI
import net.iceyleagons.klarity.script.functions.KlarityFunction

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
class ScriptParser {

    // "This is definetly not a standard script parser implementation, but I did not want to write a tokenizer and such" - TOTH

    /**
     * @property CODE_PART_START defines the boundary of script parts
     * @property CODE_PART_END defines the boundary of script parts
     * @property FUNC_PART_START defines the boundary of functions
     * @property FUNC_PART_END defines the boundary of functions
     * @property STRING_PART_INDICATOR defines the boundary of strings
     */
    companion object {
        const val CODE_PART_START = '{'
        const val CODE_PART_END = '}'
        const val FUNC_PART_START = '('
        const val FUNC_PART_END = ')'
        const val STRING_PART_INDICATOR = '\''
    }


    val values: MutableMap<String, String> = HashMap()
    private val functions: Map<String, KlarityFunction> = KlarityAPI.getFunctions(this)

    /**
     * Adds a new value to the values.
     * Values will be passed to the script functions.
     *
     * @param key the name of the value
     * @param value the value of the value
     */
    fun addValue(key: String, value: String) {
        this.values[key] = value
    }

    /**
     * Adds a new values to the values.
     * Values will be passed to the script functions.
     *
     * @param values the values to add
     */
    fun addValues(values: Map<String, String>) {
        this.values.putAll(values)
    }

    /**
     * Parses all the script parts inside the input
     *
     * @param input the input
     * @return the parsed output
     */
    fun parseScript(input: String): String {
        if (!ParsingUtils.hasParsableCode(input)) return input

        val result = StringBuilder()
        var currentIndex = input.indexOf(CODE_PART_START)
        if (currentIndex > 0) result.append(input, 0, currentIndex)

        while (ParsingUtils.hasParsableCode(input, currentIndex)) {
            val code: String = TraversingFunctionBodyParser.getBody(input, currentIndex)
            val rawFunction: String = ParsingUtils.getContentInsideIndicators(code, CODE_PART_START, CODE_PART_END)
            val parsed = parseFunction(rawFunction)

            result.append(parsed)
            currentIndex += code.length

            val openIndex = input.indexOf(CODE_PART_START, currentIndex)
            if (openIndex == -1)
                break

            result.append(input, currentIndex, openIndex)
            currentIndex = openIndex
        }

        if (currentIndex >= 0 && currentIndex < input.length)
            result.append(input.substring(currentIndex))

        println()
        return result.toString()
    }

    /**
     * Parses a function in the give input.
     * This is reserved for internal use, you probably want to execute parseScript.
     *
     * @param input the input containing the function
     * @return the parsed string
     */
    fun parseFunction(input: String): String {
        if (!ParsingUtils.isFunction(input, functions.keys)) {
            if (values.containsKey(input)) return values[input]!!

            var currentInput = input
            if (ParsingUtils.hasParsableCode(input)) {
                currentInput = parseScript(input)
            }

            return if (ParsingUtils.isString(currentInput))
                ParsingUtils.getStringContent(currentInput)
            else
                currentInput
        }

        val name = ParsingUtils.getFunctionName(input)
        if (!functions.containsKey(name))
            throw IllegalStateException("Syntax error. Illegal function $name")

        return functions[name]!!.parse(input)
    }
}