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

import net.iceyleagons.klarity.api.Configuration
import net.iceyleagons.klarity.api.defaultConfig
import net.iceyleagons.klarity.script.KlarityFunction
import net.iceyleagons.klarity.script.ScriptParser

/**
 * Main singleton of Klarity. This is the instance you will interact with.
 *
 * @version 1.1.0
 * @author TOTHTOMI
 * @since Jul. 23, 2023
 */
object KlarityAPI {

   var config: Configuration = defaultConfig()

    /**
     * Configures Klarity
     */
    fun configure(initializer: ConfigurationBuilder.() -> Unit) {
        config = ConfigurationBuilder().apply(initializer).build()
    }

    /**
     * Translates a key to a string using the specified language, values and default value.
     * If scripting is enabled, parses the string as a script and applies the middlewares.
     *
     * @param key The key to look up in the translation source
     * @param defaultValue The default value to use if the key is not found
     * @param values The values to pass to the script parser
     * @param language The language code to use for translation
     * @return The translated, parsed and modified string
     */
    fun translate(key: String, defaultValue: String? = null, values: Map<String, String> = emptyMap(), language: String = config.defaultLanguage): String {
        val rawString = getRawString(key, defaultValue, language)
        val parsed = parseScript(rawString, values)
        val modified = applyMiddlewares(parsed)

        return config.globalConfig.globalPrefix + modified + config.globalConfig.globalSuffix
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
        if (!config.scriptingEnabled) {
            return input
        }

        val scriptParser = ScriptParser(config.pluginConfig.functions, concat(config.globalConfig.globalParameters, values))
        return scriptParser.parse(input)
    }

    /**
     * Concatenates the given maps into one
     *
     * @return the resulting map
     */
    private fun concat(vararg map: Map<String, String>): Map<String, String> {
        val result: MutableMap<String, String> = HashMap()
        map.forEach(result::putAll)
        return result
    }

    /**
     * Applies the modification of the registered middlewares to the input.
     * If there are no middlewares registered, it will return the input.
     *
     * @param input the input
     * @return the resulting output
     */
    private fun applyMiddlewares(input: String): String {
        val middlewares = config.pluginConfig.middlewares
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
        if (!config.sources.containsKey(language.lowercase())) {
            return defaultValue ?: ""
        }

        return config.sources[language.lowercase()]!!.getRawString(key, defaultValue) ?: defaultValue ?: ""
    }
}