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

package net.iceyleagons.klarity

import net.iceyleagons.klarity.api.FunctionProvider
import net.iceyleagons.klarity.api.KlarityMiddleware
import net.iceyleagons.klarity.api.TranslationSource
import net.iceyleagons.klarity.script.functions.DefaultFunctions
import net.iceyleagons.klarity.script.functions.KlarityFunction
import net.iceyleagons.klarity.script.parsing.ScriptParser
import java.util.stream.Collectors

/**
 * Main singleton of Klarity. This is the instance you will interact with.
 *
 * @property sources A mutable map of language codes to translation sources.
 * @property functions A mutable set of function providers that can be used in scripts
 * @property middlewares A mutable list of middlewares that can modify the output of scripts
 * @property defaultLanguage The default language code to use for translations
 * @property scriptingEnabled A flag to enable or disable script parsing
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 23, 2023
 */
object Klarity {

    private val sources: MutableMap<String, TranslationSource> = HashMap()
    private val functions: MutableSet<FunctionProvider> = HashSet(25)
    private val middlewares: MutableList<KlarityMiddleware> = ArrayList(4)

    var defaultLanguage = "en"
    var scriptingEnabled = true

    init {
        functions.addAll(DefaultFunctions.functions)
    }

    /**
     * Translates a key to a string using the specified language, values and default value.
     * If scripting is enabled, parses the string as a script and applies the middlewares.
     *
     * @param key The key to look up in the translation source
     * @param defaultValue The default value to use if the key is not found
     * @param values The values to pass to the script parser
     * @param langauge The language code to use for translation
     * @return The translated, parsed and modified string
     */
    fun translate(key: String, defaultValue: String? = null, values: Map<String, String> = mapOf(), langauge: String = defaultLanguage): String {
        val rawString = getRawString(key, defaultValue, langauge)
        val parsed = parseScript(rawString, values)
        val modified = applyMiddlewares(parsed)

        return modified
    }

    /**
     * Registers a middleware to the list of middlewares.
     *
     * @param middleware The middleware to register
     * @throws IllegalStateException If the middleware is already registered
     */
    fun registerMiddleware(middleware: KlarityMiddleware) {
        if (middlewares.contains(middleware)) {
            throw IllegalStateException("Middleware already registered!")
        }

        middlewares.add(middleware)
    }

    /**
     * Registers a translation source for a language code.
     *
     * @param language The language code to register
     * @param source The translation source to register
     * @throws IllegalStateException If the language code is already registered
     */
    fun registerSource(language: String, source: TranslationSource) {
        if (sources.containsKey(language.lowercase())) {
            throw IllegalStateException("Language already registered!")
        }
        sources[language.lowercase()] = source
    }

    /**
     * Registers a function provider to the set of function providers.
     *
     * @param functionProvider The function provider to register
     */
    fun registerFunction(functionProvider: FunctionProvider) {
        functions.add(functionProvider)
    }

    /**
     * Returns a map of function names to klarity functions from the function providers.
     * Internal use only, used by ScriptParser instances!
     *
     * @param scriptParser The script parser to pass to the function providers
     * @return A map of function names to klarity functions
     */
    fun getFunctions(scriptParser: ScriptParser): Map<String, KlarityFunction> {
        val functions = functions.map { it.getFunction(scriptParser) }
        return functions.stream().collect(Collectors.toMap({ it.functionName.lowercase() }, { it } ))
    }

    /**
     * Parses a string as a script using a script parser with the given values.
     * If scripting is not enabled it will just return the input.
     *
     * @param input the string containing the script parts
     * @param values the values to pass to the ScriptParser
     * @return the resulting output
     */
    private fun parseScript(input: String, values: Map<String, String>): String {
        if (!scriptingEnabled) {
            return input
        }

        val scriptParser = ScriptParser()
        scriptParser.addValues(values)

        return scriptParser.parseScript(input)
    }

    /**
     * Applies the modification of the registered middlewares to the input.
     * If there are no middlewares registered, it will return the input.
     *
     * @param inpit the input
     * @return the resulting output
     */
    private fun applyMiddlewares(input: String): String {
        if (middlewares.isEmpty())
            return input

        var value = input
        middlewares.forEach {
            value = it.modify(value)
        }
        return value
    }

    /**
     * Returns the raw string for a key from the translation source of a language code, or the default value if not found
     *
     * @param key the key to the translation
     * @param defaultValue the default value
     * @param language the language code
     */
    private fun getRawString(key: String, defaultValue: String?, language: String): String {
        if (!sources.containsKey(language.lowercase())) {
            return defaultValue ?: ""
        }

        return sources[language.lowercase()]!!.getRawString(key, defaultValue) ?: defaultValue ?: ""
    }
}