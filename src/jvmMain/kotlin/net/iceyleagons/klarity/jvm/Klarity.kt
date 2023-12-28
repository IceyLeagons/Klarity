package net.iceyleagons.klarity.jvm

import net.iceyleagons.klarity.ConfigurationBuilder
import net.iceyleagons.klarity.KlarityAPI

object Klarity {

    /**
     * Configures Klarity
     */
    fun configure(initializer: ConfigurationBuilder.() -> Unit) {
        KlarityAPI.config = ConfigurationBuilder().apply(initializer).build()
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
    fun translate(key: String, defaultValue: String?, values: Map<String, String> = mapOf(), language: String = KlarityAPI.config.defaultLanguage): String {
        return KlarityAPI.translate(key, defaultValue, values, language)
    }
}