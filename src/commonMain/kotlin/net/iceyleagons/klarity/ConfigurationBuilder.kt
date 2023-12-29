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

import net.iceyleagons.klarity.api.*
import net.iceyleagons.klarity.script.DefaultFunctions
import net.iceyleagons.klarity.script.KlarityFunction

/**
 * A builder class for creating a [Configuration] object.
 *
 * This class provides methods for setting various properties of a configuration, such as the default language, the scripting enabled flag, the globals, the plugins, and the sources. It also provides methods for applying custom initializers to some of these properties. The [build] method returns a new [Configuration] object based on the current state of the builder.
 *
 * @property defaultLanguage the default language of the configuration. Defaults to "en".
 * @property scriptingEnabled whether scripting is enabled for the configuration. Defaults to true.
 * @property globals a [GlobalsBuilder] object for setting global variables and functions for the configuration.
 * @property plugins a [PluginManagementConfigurationBuilder] object for managing plugins for the configuration.
 * @property sources a [SourceManagement] object for managing sources for the configuration.
 * @constructor Creates a new [ConfigurationBuilder] with default values for all properties.
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 25, 2023
 */
class ConfigurationBuilder {

    private var defaultLanguage = "en"
    private var scriptingEnabled = true

    private var globals = GlobalsBuilder()
    private var plugins = PluginManagementConfigurationBuilder()
    private var sources = SourceManagement()

    /**
     * Sets the source management property of the configuration using a custom initializer.
     *
     * This function takes a lambda expression as a parameter and applies it to the [SourceManagement] object of the builder. The lambda expression can modify the state of the source management object, such as adding or removing sources, setting source options, etc. The function returns the modified source management object.
     *
     * @param initializer a lambda expression that modifies the source management object.
     * @return the modified source management object.
     */
    fun sourceManagement(initializer: SourceManagement.() -> Unit): SourceManagement {
        return sources.apply(initializer)
    }

    /**
     * Sets the globals property of the configuration using a custom initializer.
     *
     * This function takes a lambda expression as a parameter and applies it to the [GlobalsBuilder] object of the builder. The lambda expression can modify the state of the globals object, such as adding or removing global variables or functions, setting global options, etc. The function returns the modified globals object.
     *
     * @param initializer a lambda expression that modifies the globals object.
     * @return the modified globals object.
     */
    fun withGlobals(initializer: GlobalsBuilder.() -> Unit): GlobalsBuilder {
        return globals.apply(initializer)
    }

    /**
     * Sets the plugin management property of the configuration using a custom initializer.
     *
     * This function takes a lambda expression as a parameter and applies it to the [PluginManagementConfigurationBuilder] object of the builder. The lambda expression can modify the state of the plugin management object, such as adding or removing plugins, setting plugin options, etc. The function returns the modified plugin management object.
     *
     * @param initializer a lambda expression that modifies the plugin management object.
     * @return the modified plugin management object.
     */
    fun pluginManagement(initializer: PluginManagementConfigurationBuilder.() -> Unit): PluginManagementConfigurationBuilder {
        return plugins.apply(initializer)
    }

    /**
     * Sets the default language property of the configuration.
     *
     * This function takes a string as a parameter and assigns it to the default language property of the builder. The string should be a valid language code, such as "en", "fr", "zh", etc. The default language property determines the language used by the configuration when no other language is specified by a source or a plugin.
     *
     * @param language a string representing a valid language code.
     */
    fun defaultLanguage(language: String) {
        this.defaultLanguage = language
    }

    /**
     * Sets the scripting enabled flag of the configuration.
     *
     * This function takes a boolean as a parameter and assigns it to the scripting enabled flag of the builder. The scripting enabled flag determines whether scripting is allowed for the configuration. If scripting is enabled, then sources and plugins can execute scripts written in various languages, such as JavaScript, Python, Ruby, etc. If scripting is disabled, then scripts will be ignored or rejected by the configuration.
     *
     * @param enabled a boolean indicating whether scripting is enabled or not.
     */
    fun withScriptingEnabled(enabled: Boolean) {
        this.scriptingEnabled = enabled
    }

    /**
     * Builds and returns a new [Configuration] object based on the current state of the builder.
     *
     * This function creates a new [Configuration] object using the properties of the builder. It then returns this object to the caller. The builder can be reused after calling this function, but any changes made to it will not affect the previously created configuration objects.
     *
     * @return a new [Configuration] object based on the current state of the builder.
     */
    fun build(): Configuration {
        return Configuration(defaultLanguage, scriptingEnabled, globals.build(), plugins.build(), sources.build())
    }
}


/**
 * A builder class for creating a [GlobalConfig] object.
 *
 * This class provides methods for setting various properties of a global configuration, such as the global parameters, the global prefix, and the global suffix. It also provides methods for adding global parameters using different formats, such as a map, a pair, or a custom initializer. The [build] method returns a new [GlobalConfig] object based on the current state of the builder.
 *
 * @property globalParameters a mutable map of strings that stores the key-value pairs of global parameters.
 * @property globalPrefix a string that represents the prefix to be added to all global parameters.
 * @property globalSuffix a string that represents the suffix to be added to all global parameters.
 * @constructor Creates a new [GlobalsBuilder] with empty values for all properties.
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 25, 2023
 */
class GlobalsBuilder {
    private val globalParameters = mutableMapOf<String, String>()
    private var globalPrefix = ""
    private var globalSuffix = ""

    /**
     * Adds a single global parameters to the builder.
     * Global parameters will be provided to every translation's script parser at runtime.
     *
     * @param key the name of the parameter
     * @param value the value of the parameter
     */
    fun globalParameter(key: String, value: String) {
        this.globalParameters[key] = value
    }

    /**
     * Adds multiple global parameters to the builder.
     * Global parameters will be provided to every translation's script parser at runtime.
     *
     * @param map the parameters to add
     */
    fun globalParameters(map: Map<String, String>) {
        globalParameters.putAll(map)
    }

    /**
     * Adds multiple global parameters to the builder using a custom initializer, so you can use it via DSL.
     * Global parameters will be provided to every translation's script parser at runtime.
     *
     * @param initializer a lambda expression that creates pairs of strings and adds them to an [And] object.
     */
    fun globalParameters(initializer: GlobalParameter.() -> Unit) {
        globalParameters.putAll(And().apply(initializer).parameters)
    }

    /**
     * Sets the global prefix.
     * A global prefix will be applied to the start of every translation at runtime
     *
     * @param prefix the desired suffix
     */
    fun prefix(prefix: String) {
        this.globalPrefix = prefix
    }

    /**
     * Sets the global suffix.
     * A global suffix will be applied to the end of every translation at runtime
     *
     * @param suffix the desired suffix
     */
    fun suffix(suffix: String) {
        this.globalSuffix = suffix
    }

    /**
     * Builds and returns a new [GlobalConfig] object based on the current state of the builder.
     *
     * This function creates a new [GlobalConfig] object using the properties of the builder. It then returns this object to the caller. The builder can be reused after calling this function, but any changes made to it will not affect the previously created global configuration objects.
     *
     * @return a new [GlobalConfig] object based on the current state of the builder.
     */
    fun build(): GlobalConfig {
        return GlobalConfig(globalPrefix, globalSuffix, globalParameters)
    }
}

/**
 * An abstract class for creating pairs of strings that represent global parameters.
 *
 * This class provides an infix function [to] that takes two strings as parameters and creates a pair of strings.
 * The pair can then be added to a list or a map of global parameters. The class also defines an abstract method [addParameter] that takes a pair of strings as a parameter and adds it to a specific collection.
 * The subclasses of this class should implement this method according to their own logic.
 *
 * @constructor Creates a new [GlobalParameter] object.
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 25, 2023
 */
abstract class GlobalParameter {

    /**
     * Creates a pair of strings that represent a global parameter.
     *
     * This function takes two strings as parameters and returns a pair of strings. The first string is the key and the second string is the value of the global parameter. The strings should not be null or empty. The function can be used in an infix notation, such as "key" to "value".
     *
     * @param value a string representing the value of the global parameter.
     * @return a pair of strings that represent a global parameter.
     */
    infix fun String.to(value: String) {
        addParameter(Pair(this, value))
    }

    /**
     * Adds a pair of strings to a specific collection.
     *
     * This function takes a pair of strings as a parameter and adds it to a specific collection. The collection can be a list, a map, or any other data structure that can store pairs of strings. The function does not return anything. The subclasses of this class should implement this method according to their own logic.
     *
     * @param pair a pair of strings that represent a global parameter.
     */
    abstract fun addParameter(pair: Pair<String, String>)
}

/**
 * A subclass of [GlobalParameter] that adds pairs of strings to a mutable list.
 *
 * This class inherits from the [GlobalParameter] class and overrides the [addParameter] method. It uses a mutable list of pairs of strings as its internal data structure. It adds pairs of strings to this list using the [add] method. The list can then be accessed by other classes using the [parameters] property.
 *
 * @property parameters a mutable list of pairs of strings that stores the global parameters.
 * @constructor Creates a new [And] object with an empty list of parameters.
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 25, 2023
 */
class And : GlobalParameter() {
    val parameters = mutableListOf<Pair<String, String>>()

    /**
     * Adds a pair of strings to the [parameters] list.
     *
     * This function takes a pair of strings as a parameter and adds it to the [parameters] list using the [add] method. The pair should not be null or empty. The function does not return anything.
     *
     * @param pair a pair of strings that represent a global parameter.
     */
    override fun addParameter(pair: Pair<String, String>) {
        parameters.add(pair)
    }
}

/**
 * A builder class for providing sources list for the [Configuration] object.
 *
 * This class provides methods for registering and retrieving translation sources for various language codes. A translation source is an object that implements the [TranslationSource] interface and provides methods for translating texts from one language to another. The class uses a mutable map of strings and [TranslationSource] objects as its internal data structure. The map stores the language codes and the corresponding translation sources that are registered by the class.
 *
 * @property sources a mutable map of strings and [TranslationSource] objects that stores the registered translation sources.
 * @constructor Creates a new [SourceManagement] object with an empty map of sources.
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 25, 2023
 */
class SourceManagement {

    private val sources = mutableMapOf<String, TranslationSource>()

    /**
     * Registers a translation source for a language code.
     *
     * This method takes a string and a [TranslationSource] object as parameters and adds them as a key-value pair to the [sources] map. The string should be a valid language code, such as "en", "fr", "zh", etc. The [TranslationSource] object should be an instance of a class that implements the [TranslationSource] interface and provides methods for translating texts from one language to another. The method throws an [IllegalStateException] if the language code is already registered in the map. The method does not return anything.
     *
     * @param languageCode a string representing the language code to register.
     * @param translationSource a [TranslationSource] object representing the translation source to register.
     * @throws IllegalStateException if the language code is already registered in the map.
     */
    fun registerSource(languageCode: String, translationSource: TranslationSource) {
        if (sources.containsKey(languageCode.lowercase())) {
            throw IllegalStateException("Language already registered!")
        }

        sources[languageCode.lowercase()] = translationSource
    }

    /**
     * Builds and returns a new immutable map of strings and [TranslationSource] objects based on the current state of the [sources] map.
     *
     * This method creates a new immutable map of strings and [TranslationSource] objects using the entries of the [sources] map. It then returns this map to the caller. The method can be used to retrieve the registered translation sources for different languages. The method does not modify the [sources] map or affect its state. The method does not throw any exceptions.
     *
     * @return a new immutable map of strings and [TranslationSource] objects based on the current state of the [sources] map.
     */
    fun build(): MutableMap<String, TranslationSource> = sources
}

/**
 * A builder class for creating a [PluginConfig] object.
 *
 * This class provides methods for registering and retrieving plugins for the configuration. A plugin is an object that implements either the [KlarityMiddleware] or the [FunctionProvider] interface and provides methods for modifying or enhancing the translation process. The class uses a mutable list of [KlarityMiddleware] objects and a mutable set of [FunctionProvider] objects as its internal data structures. The list stores the middlewares that are registered by the class. The set stores the function providers that are registered by the class. The class also adds some default functions to the set using the [DefaultFunctions] object.
 *
 * @property middlewares a mutable list of [KlarityMiddleware] objects that stores the registered middlewares.
 * @property functions a mutable set of [FunctionProvider] objects that stores the registered function providers.
 * @constructor Creates a new [PluginManagementConfigurationBuilder] object with an empty list of middlewares and a set of default functions.
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 25, 2023
 */
class PluginManagementConfigurationBuilder {

    private val middlewares = mutableListOf<KlarityMiddleware>()
    private val functions = mutableMapOf<String, KlarityFunction>()

    init {
        functions.putAll(DefaultFunctions.get())
    }

    /**
     * Registers a middleware to the list of middlewares.
     *
     * This method takes a [KlarityMiddleware] object as a parameter and adds it to the [middlewares] list. The [KlarityMiddleware] object should be an instance of a class that implements the [KlarityMiddleware] interface and provides methods for modifying the translation process, such as validating, filtering, transforming, or enriching the texts. The method throws an [IllegalStateException] if the middleware is already registered in the list. The method does not return anything.
     *
     * @param middleware a [KlarityMiddleware] object representing the middleware to register.
     * @throws IllegalStateException if the middleware is already registered in the list.
     */
    fun registerMiddleware(middleware: KlarityMiddleware) {
        if (this.middlewares.contains(middleware)) {
            throw IllegalStateException("Middleware already registered!")
        }

        this.middlewares.add(middleware)
    }

    /**
     * Registers a function provider to the set of function providers.
     *
     * This method takes a [FunctionProvider] object as a parameter and adds it to the [functions] set. The [FunctionProvider] object should be an instance of a class that implements the [FunctionProvider] interface and provides parsing for the function defined.
     *
     * @param functionProvider a [FunctionProvider] object representing the function provider to register.
     * @throws IllegalStateException if the function provider is already registered in the set.
     */
    fun registerFunction(name: String, function: KlarityFunction) {
        if (this.functions.containsKey(name)) {
            throw IllegalStateException("Function already registered!")
        }

        this.functions[name] = function
    }

    /**
     * Builds and returns a new [PluginConfig] object based on the current state of the builder.
     *
     * This method creates a new [PluginConfig] object using the properties of the builder. It then returns this object to the caller. The builder can be reused after calling this function, but any changes made to it will not affect the previously created plugin configuration objects.
     *
     * @return a new [PluginConfig] object based on the current state of the builder.
     */
    fun build(): PluginConfig {
        return PluginConfig(middlewares, functions)
    }
}