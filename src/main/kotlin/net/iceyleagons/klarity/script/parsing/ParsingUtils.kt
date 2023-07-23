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
 * A utility object that provides various functions for parsing and manipulating strings in a script.
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 23, 2023
 */
object ParsingUtils {

    /**
     * Converts a Boolean value to its script representation as a string.
     *
     * @param this the Boolean value to convert.
     * @return "true" if the value is true, "false" otherwise.
     */
    fun Boolean.toScriptRepresentation(): String {
        return if (this) "true" else "false"
    }

    /**
     * Checks if a string can be parsed as an integer.
     *
     * @param this the string to check.
     * @return true if the string is a valid integer, false otherwise.
     */
    fun String.isInt(): Boolean {
        return try {
            Integer.parseInt(this)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Checks if a string is equal to "true".
     *
     * @param this the string to check.
     * @return true if the string is "true", false otherwise.
     */
    fun String.isTrue(): Boolean {
        return this == "true"
    }

    /**
     * Checks if a string is equal to "false".
     *
     * @param this the string to check.
     * @return true if the string is "false", false otherwise.
     */
    fun String.isFalse(): Boolean {
        return this == "false"
    }

    /**
     * Checks if a string is enclosed by the STRING_PART_INDICATOR character.
     *
     * @param input the string to check.
     * @return true if the string has a valid format, false otherwise.
     */
    fun isString(input: String): Boolean {
        val a = input.indexOf(STRING_PART_INDICATOR)
        val b = input.lastIndexOf(STRING_PART_INDICATOR)

        return a != -1 && b != -1 && a < b
    }

    /**
     * Extracts the content inside two indicator characters from a string.
     *
     * @param input the string to extract from.
     * @param start the character that marks the start of the content.
     * @param end the character that marks the end of the content.
     * @return the content between the indicators, or the original input if not found.
     */
    fun getContentInsideIndicators(input: String, start: Char, end: Char): String {
        val a = input.indexOf(start)
        val b = input.lastIndexOf(end)

        return if (a == -1 || b == -1) input else input.substring(a + 1, b)
    }

    /**
     * Extracts the content inside two STRING_PART_INDICATOR characters from a string.
     *
     * @param input the string to extract from.
     * @return the content between the indicators, or the original input if not found.
     */
    fun getStringContent(input: String): String {
        return getContentInsideIndicators(input, STRING_PART_INDICATOR, STRING_PART_INDICATOR)
    }

    /**
     * Extracts the content inside two FUNC_PART_START and FUNC_PART_END characters from a string.
     *
     * @param input the string to extract from.
     * @return the content between the indicators, or the original input if not found.
     */
    fun getFunctionContent(input: String): String {
        return getContentInsideIndicators(input, FUNC_PART_START, FUNC_PART_END)
    }

    /**
     * Extracts the name of a function from a string that starts with it and ends with FUNC_PART_START character.
     *
     * @param input the string to extract from.
     * @return the name of the function in lowercase, or an empty string if not found.
     */
    fun getFunctionName(input: String): String {
        val a = input.indexOf(FUNC_PART_START)
        return if (a == -1) "" else input.substring(0, a).trim().lowercase()
    }

    /**
     * Checks if a string is a valid function call with one of the given function names and proper indicators.
     *
     * @param input the string to check.
     * @param functions the set of valid function names in lowercase.
     * @return true if the string is a valid function call, false otherwise.
     */
    fun isFunction(input: String, functions: Set<String>): Boolean {
        val a = input.indexOf(FUNC_PART_START)
        val b = input.indexOf(FUNC_PART_END)
        return a != -1 && b != -1 && a < b
                && functions.contains(input.substring(0, a).trim().lowercase())
    }

    /**
     * Checks if a string contains parsable code between two CODE_PART_START and CODE_PART_END characters.
     *
     * @param input the string to check.
     * @return true if the string has parsable code, false otherwise.
     */
    fun hasParsableCode(input: String): Boolean {
        return hasParsableCode(input, 0)
    }

    /**
     * Checks if a string contains parsable code between two CODE_PART_START and CODE_PART_END characters, starting from a given index.
     *
     * @param input the string to check.
     * @param start the index to start from.
     * @return true if the string has parsable code, false otherwise.
     */
    fun hasParsableCode(input: String, start: Int): Boolean {
        val a = input.indexOf(CODE_PART_START, start)
        val b = input.lastIndexOf(CODE_PART_END)

        return start >= 0 && a != -1 && b != -1 && a < b
    }

    /**
     * Checks if a character is one of the special characters used for parsing scripts.
     *
     * @param symbol the character to check.
     * @return true if the character is special, false otherwise.
     */
    fun isSpecialChar(symbol: Char): Boolean =
        symbol == '\''
                || symbol == FUNC_PART_START
                || symbol == FUNC_PART_END
                || symbol == CODE_PART_START
                || symbol == CODE_PART_END

}