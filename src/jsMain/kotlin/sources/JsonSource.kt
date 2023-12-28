package sources

import net.iceyleagons.klarity.api.TranslationSource
import kotlin.js.Json

@JsExport
@JsName("JsonSource")
@OptIn(ExperimentalJsExport::class)
class JsonSource(val root: Json) : TranslationSource {

    /**
     * Gets the raw string value for the given key from the JSON object.
     * If the key does not exist, it returns the default value.
     *
     * @param key the key of the translation
     * @param defaultValue the default value to use if the key does not exist
     * @return the string value for the key or null if both the key and the default value are null
     */
    override fun getRawString(key: String, defaultValue: String?): String? {
        return get(key, this.root) ?: defaultValue
    }

    /**
     * Gets the string value for the given key from the JSON object.
     * The key can be a dot-separated path to a nested object.
     *
     * @param key the key of the translation
     * @param jsonObject the JSON object to search in
     * @return the string value for the key or null if it does not exist
     */
    private fun get(key: String, jsonObject: Json): String? {
        val path = key.split(".")
        val value: Any = jsonObject[path[0]] ?: return null
        if (value is String || value is Number || value is Boolean) {
            return value.toString()
        }

        var currentObj: Json? = value as Json
        for (i in 1..<path.size - 1) {
            if (currentObj == null) {
                return null
            }

            currentObj = currentObj[path[i]] as Json? ?: return null
        }

        if (currentObj == null) return null
        return currentObj[path[path.size - 1]]?.toString() ?: return null
    }
}