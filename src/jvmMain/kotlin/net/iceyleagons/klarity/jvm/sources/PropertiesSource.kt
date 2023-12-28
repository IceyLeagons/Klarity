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

package net.iceyleagons.klarity.jvm.sources

import net.iceyleagons.klarity.api.TranslationSource
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

/**
 * A [TranslationSource] that reads and writes key-value pairs from a properties file.
 *
 * @param path the path to the properties file. If the file does not exist, it will be created.
 * @constructor Initialize the properties object from the file or create a new one if the file does not exist
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 23, 2023
 */
class PropertiesSource(private val path: Path) : TranslationSource {

    private val properties: Properties

    init {
        if (!Files.exists(path)) {
            Files.createFile(path)
        }

        Files.newInputStream(path).use {
            properties = Properties()
            properties.load(it)
        }
    }

    /**
     * Saves the properties to the file.
     */
    private fun saveProperties() {
        Files.newOutputStream(path).use {
            properties.store(it, null)
        }
    }

    /**
     * Returns the value associated with the given key, or the default value if the key is not found.
     * If the key is not found and the default value is not null, the key-value pair will be added to the properties and saved to the file.
     *
     * @param key the key to look up
     * @param defaultValue the default value to return if the key is not found
     * @return the value associated with the key, or the default value if not found
     */
    override fun getRawString(key: String, defaultValue: String?): String? {
        if (!properties.containsKey(key)) {
            if (defaultValue == null) {
                // We don't want to write null to the file
                return null
            }

            properties[key] = defaultValue
            saveProperties()
            return defaultValue
        }

        return properties[key].toString()
    }
}