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

package net.iceyleagons.klarity.script.functions

import net.iceyleagons.klarity.script.ReturnValueSupplier
import net.iceyleagons.klarity.script.parsing.ListUtils
import net.iceyleagons.klarity.script.parsing.ParsingUtils
import net.iceyleagons.klarity.script.parsing.ScriptParser

/**
 * An abstract class that represents a custom function in Klarity scripting language.
 *
 * @property functionName the name of the function
 * @property scriptParser the parser that handles the script execution
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 23, 2023
 */
abstract class KlarityFunction(val functionName: String, val scriptParser: ScriptParser) {

    /**
     * Parses the input string and returns the result of the function evaluation.
     *
     * @param input the input string that contains the function call
     * @return the result of the function evaluation as a string
     */
    abstract fun parse(input: String): String

    /**
     * Handles the case when the function expects two parameters and returns a value based on a supplier function.
     *
     * @param input the input string that contains the function call
     * @param returnValueSupplier the function that takes two parameters and returns a value
     * @return the result of the supplier function as a string
     * @throws IllegalStateException if the input does not contain exactly two parameters
     */
    protected fun handleTwoParameterList(input: String, returnValueSupplier: ReturnValueSupplier): String {
        val list = ListUtils.transformCommaSeparatedList(ParsingUtils.getFunctionContent(input))
        if (list.size != 2) {
            throw IllegalStateException("Expected 2 parameters got ${list.size}")
        }

        return returnValueSupplier.get(getValueIfExists(list[0]), getValueIfExists(list[1]))
    }

    /**
     * Returns the value of a string or a variable if it exists in the script parser.
     *
     * @param value the string or variable name to get the value of
     * @return the value of the string or variable, or the original value if not found
     */
    private fun getValueIfExists(value: String): String {
        if (ParsingUtils.isString(value)) {
            return ParsingUtils.getStringContent(value)
        }

        return scriptParser.values.getOrDefault(value, value)
    }
}