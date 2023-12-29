import js.collections.MapLike
import js.core.Object
import net.iceyleagons.klarity.ConfigurationBuilder
import net.iceyleagons.klarity.KlarityAPI
import net.iceyleagons.klarity.api.KlarityMiddleware
import net.iceyleagons.klarity.api.TranslationSource
import kotlin.js.Json


@JsExport
@JsName("Klarity")
@OptIn(ExperimentalJsExport::class)
object KlarityJS {

    /**
     * Configures Klarity
     */
    fun configure(json: Json) {
        val builder = ConfigurationBuilder()

        json["defaultLanguage"]?.let {
            builder.defaultLanguage(it as String)
        }

        json["scriptingEnabled"]?.let {
            builder.withScriptingEnabled(it as Boolean)
        }

        json["globals"]?.let { globalsObject ->
            val globals = globalsObject as Json

            builder.withGlobals {
                globals["suffix"]?.let {
                    this.suffix(it as String)
                }

                globals["prefix"]?.let {
                    this.prefix(it as String)
                }

                globals["globalParameters"]?.let {
                    val params = it as MapLike<String, String>
                    this.globalParameters(toMap(params))
                }
            }
        }

        KlarityAPI.config = builder.build()
    }

    /**
     * Registers a translation source for a language code.
     *
     *
     * @param languageCode a string representing the language code to register.
     * @param translationSource a [TranslationSource] object representing the translation source to register.
     * @throws IllegalStateException if the language code is already registered in the map.
     */
    fun registerSource(languageCode: String, translationSource: TranslationSource) {
        if (KlarityAPI.config.sources.containsKey(languageCode)) {
            throw IllegalStateException("Language already registered!")
        }

        KlarityAPI.config.sources[languageCode] = translationSource
    }

    /**
     * Registers a middleware to the list of middlewares.
     *
     * @param middleware a [KlarityMiddleware] object representing the middleware to register.
     * @throws IllegalStateException if the middleware is already registered in the list.
     */
    fun registerMiddleware(middleware: KlarityMiddleware) {
        if (KlarityAPI.config.pluginConfig.middlewares.contains(middleware)) {
            throw IllegalStateException("Middleware already registered!")
        }

        KlarityAPI.config.pluginConfig.middlewares.add(middleware)
    }


    fun translate(key: String,
            defaultValue: String? = null,
            values: Any? = null,
            language: String = KlarityAPI.config.defaultLanguage): String {

        return KlarityAPI.translate(key, defaultValue, toMap(values), language)
    }

    private fun toMap(o: Any?): Map<String, String> {
        val result = mutableMapOf<String, String>()

        if (o != null && o !== undefined) {
            val entries = Object.entries(o).associate { Pair(it.component1(), it.component2()) }
            for (key in Object.keys(o)) {
                result[key] = entries[key].toString()
            }
        }
        return result
    }
}