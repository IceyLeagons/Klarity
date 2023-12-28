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

/**
 * A utility object that provides methods for manipulating lists of strings.
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 23, 2023
 */
object ListUtils {

    /**
     * Transforms a comma-separated string into a list of trimmed strings.
     *
     * @param input The input string to be transformed.
     * @return A list of trimmed strings obtained by splitting the input by commas.
     */
    fun transformCommaSeparatedList(input: String): List<String> {
        return input.split(",").map { it.trim() }.toList()
    }

    /**
     * Parses a string containing a list of functions and returns a list of function bodies.
     *
     * @param input The input string to be parsed.
     * @return A list of strings representing the function bodies extracted from the input.
     */
    fun parseFunctionList(input: String): List<String?> {
        var start = 0
        val list: MutableList<String?> = ArrayList()
        while (input.indexOf(",", start) != -1) {
            val body = TraversingFunctionBodyParser.getBody(input, start)

            val index = input.indexOf(body, start)
            val comma = input.indexOf(",", index + body.length)
            if (comma == -1) break

            start = comma + 1
            list.add(body.trim())
        }

        list.add(TraversingFunctionBodyParser.getBody(input, start).trim())
        return list
    }
}