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

package net.iceyleagons.klarity.source

import net.iceyleagons.klarity.api.TranslationSource
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.nio.file.Files
import java.nio.file.Path

/**
 * A [TranslationSource] that uses a JSON file to store and retrieve translations.
 *
 * @param path the path of the JSON file
 * @constructor Initialize the json object from the file or create a new one if the file does not exist
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 23, 2023
 */
class JsonSource(private val path: Path) : TranslationSource {

    private val json: JSONObject

    init {
        if (!Files.exists(path)) {
            json = JSONObject()
            Files.createFile(path)
        } else {
            Files.newInputStream(path).use {
                json = JSONObject(it)
            }
        }
    }

    /**
     * Gets the raw string value for the given key from the JSON object.
     * If the key does not exist, it writes the default value to the JSON object and returns it.
     *
     * @param key the key of the translation
     * @param defaultValue the default value to use if the key does not exist
     * @return the string value for the key or null if both the key and the default value are null
     */
    override fun getRawString(key: String, defaultValue: String?): String? {
        if (get(key, json) == null) {
            if (defaultValue == null) {
                // We don't want to write null to the file
                return null
            }

            write(key, defaultValue, json)
            updateJson()
            return defaultValue
        }

        return get(key, json)
    }

    /**
     * Updates the JSON file with the current state of the JSON object.
     */
    private fun updateJson() {
        Files.newOutputStream(path).use {
            OutputStreamWriter(it).use { writer ->
                json.write(writer, 2, 0)
            }
        }
    }

    /**
     * Gets the string value for the given key from the JSON object.
     * The key can be a dot-separated path to a nested object.
     *
     * @param key the key of the translation
     * @param jsonObject the JSON object to search in
     * @return the string value for the key or null if it does not exist
     */
    private fun get(key: String, jsonObject: JSONObject): String? {
        val path = key.split(".")
        if (!jsonObject.has(path[0])) return null

        var currentObj: JSONObject? = jsonObject.getJSONObject(path[0])
        for (i in 1 until path.size - 1) {
            if (currentObj == null || !currentObj.has(path[i])) return null
            currentObj = currentObj.getJSONObject(path[i])
        }

        if (currentObj == null || !currentObj.has(path[path.size - 1])) return null
        return currentObj.getString(path[path.size - 1])
    }

    /**
     * Writes the given value for the given key to the JSON object.
     * The key can be a dot-separated path to a nested object.
     * If any intermediate object does not exist, it creates it.
     *
     * @param key the key of the translation
     * @param value the value to write
     * @param jsonObject the JSON object to write to
     */
    private fun write(key: String, value: String, jsonObject: JSONObject) {
        if (!key.contains(".")) {
            jsonObject.put(key, value)
            return
        }

        val path = key.split(".")

        var currentObj: JSONObject = getOrCreate(path[0], jsonObject)
        for (i in 1 until path.size - 1) {
            currentObj = getOrCreate(path[i], currentObj)
        }

        currentObj.put(path[path.size - 1], value)
    }

    /**
     * Gets or creates a JSON object with the given name as a child of the parent object.
     * If the parent object already has a child with that name, it returns it.
     * Otherwise, it creates a new JSON object and adds it as a child with that name.
     *
     * @param name the name of the child object
     * @param parent the parent object
     * @return the child object
     */
    private fun getOrCreate(name: String, parent: JSONObject): JSONObject {
        if (parent.has(name)) {
            return parent.getJSONObject(name)
        }

        val jsonObject = JSONObject()
        parent.put(name, jsonObject)
        return jsonObject
    }
}