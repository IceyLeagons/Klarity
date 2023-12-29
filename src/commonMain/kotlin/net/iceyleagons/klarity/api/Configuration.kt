package net.iceyleagons.klarity.api

import net.iceyleagons.klarity.KlarityAPI
import net.iceyleagons.klarity.script.KlarityFunction

/**
 * Data class holding the configuration for Klarity.
 *
 * @property defaultLanguage the default language of Klarity. If no language is specified in [KlarityAPI.translate] it will use this instead
 * @property scriptingEnabled if set to true the script parser will be executed, otherwise the script will remain in plain text form
 * @property globalConfig configuration regarding globals
 * @property pluginConfig configuration regarding customization
 * @property sources the translation sources
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 25, 2023
 */
data class Configuration(
    val defaultLanguage: String,
    val scriptingEnabled: Boolean,
    val globalConfig: GlobalConfig,
    val pluginConfig: PluginConfig,
    val sources: MutableMap<String, TranslationSource>
)

/**
 * Data class holding the configuration for Klarity regarding globals.
 *
 * @property globalPrefix the global prefix, that is applied to every translation at runtime
 * @property globalSuffix the global suffix, that is applied to every translation at runtime
 * @property globalParameters the parameters that will be provided to every translation
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 25, 2023
 */
data class GlobalConfig(val globalPrefix: String, val globalSuffix: String, val globalParameters: MutableMap<String, String>)

/**
 * Data class holding the configuration for Klarity regarding its' customization.
 *
 * @property middlewares the registered middlewares
 * @property functions the registered functions to use when parsing the script
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 25, 2023
 */
data class PluginConfig(val middlewares: MutableList<KlarityMiddleware>, val functions: MutableMap<String, KlarityFunction>)

/**
 * @return a Configuration with default values
 */
fun defaultConfig(): Configuration {
    return Configuration("en", true, GlobalConfig("", "", mutableMapOf()), PluginConfig(mutableListOf(), mutableMapOf()), mutableMapOf())
}