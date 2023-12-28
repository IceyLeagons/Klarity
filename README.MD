# 🗺️ Klarity
![GitHub release (with filter)](https://img.shields.io/github/v/release/IceyLeagons/Klarity?style=flat-square)
[![](https://jitpack.io/v/net.iceyleagons/klarity.svg?style=flat-square)](https://jitpack.io/#net.iceyleagons/klarity.svg?style=flat-square)
![GitHub issues](https://img.shields.io/github/issues-raw/IceyLeagons/Klarity?style=flat-square)
![GitHub](https://img.shields.io/github/license/IceyLeagons/Klarity?style=flat-square)
![GitHub Repo stars](https://img.shields.io/github/stars/IceyLeagons/Klarity?style=flat-square)




Klarity is a lightweight internalization library for the Kotlin programming language, with support for JavaScript thanks to Kotlin Multiplatform.
It makes it super easy to create multi-lingual projects.

What it provides:
 - Script language to skyrocket the capabilities of translators
 - Simple yet powerful customization
 - Multiple built-in source support (properties, json)
 - Middlewares (for ex.: to add coloring in Minecraft server plugins)
 - Automatic file generation from default values
 - Basically no dependencies (except for some built-in file format supports)

So internalization becomes as simple and powerful as: 
```kotlin
Klarity.registerSource("en", PropertiesSource(Path.of("en.properties")))

println(
    Klarity.translate(
        "path.to.translation",
        "You have {IF(EQ(amount, '1'), IF(SW(item, 'a', 'e', 'i', 'o', 'u'), 'an', 'a'), amount)} {item}{IF(GT(amount, '1'), 's', '')}",
        mapOf(
            Pair("amount", "2"),
            Pair("item", "apple")
        )
    )
)

// Output:
// Amount = 2, Item = apple --> You have 2 apples
// Amount = 1, Item = apple --> You have an apple
// Amount = 1, Item = pear --y You have a pear
```

## 💻 Basic usage

To use Klarity, you will first have to install it via your build system of choice. I recommend Gradle.
```kts
repositories {
    maven("https://www.jitpack.io")
}

dependencies {
    implementation("net.iceyleagons:klarity:1.0.0")
}
```

After that's done, you will need to register one or more TranslationSources in the configuration DSL.
However if you don't want to do it just yet, you don't need to.
Klarity will just simply use the default values you've provided.

```kotlin
Klarity.configure {
    sourceManagement {
        registerSource("en", PropertiesSource(Path.of("en.properties")))
        registerSource("hu", PropertiesSource(Path.of("hu.properties")))

        // Or JSON
        registerSource("de", JsonSource(Path.of("de.json")))
    }
}
```

If that's done, you're ready to go, just use
```kotlin
val translation = Klarity.translate("path.to.translation", "Default value")

// Or with parameters
Klarity.translate("path.to.translation2", "Value is: {param}", mapOf(Pair("param", "something")))
```

However Klarity provides much more configuration to fine tune it to your use case!

```kotlin
Klarity.configure {
    defaultLanguage("hu") // (default: en)
    withScriptingEnabled(false) // (default: true)
    withGlobals {
        globalParameters {
            "appName" to "MyApp"
            "appVersion" to "1.0.0"
        }

        // Note that prefix and suffix are not parsed for the script!
        prefix("[MyApp] ")
        suffix(" [SUFFIX]")
    }
}
```

There are even more, take a look in Customizing for them :)

## 📜 Understanding the script
If you have ever used Excel's formulas, this script will feel very similar.

In essence everything inside {} will be parsed by the parser (although you can escape it with \ ).
Anything inside ' ' will become a string (you cannot use "!)
Anything outside of ' ' will be considered as a parameter passed from the underlying code implementation.

To empower translators we've included many useful functions, but these are not limited, as Klarity
provides an API to register your own functions.

<details>
    <summary><strong>Built in functions are the following:</strong></summary>

| Functions                     | Type       | Return              | Description                                                                                                                                              |
|-------------------------------|------------|---------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| **FALSE**()                   | Constants  | Bool                | Boolean false                                                                                                                                            |
| **TRUE**()                    | Constants  | Bool                | Boolean true                                                                                                                                             |
| **AND**(bool...)              | Logic      | Bool                | If all of the values inside the AND function are true, the function will return true.                                                                    |
| **IF**(condition, pass, else) | Logic      | String or Int       | If statement, the first argument is the condition, second will get executed if the condition returns true, otherwise the 3rd argument will get executed. |
| **NOT**(bool)                 | Logic      | Bool                | Inverts the boolean value inside the function.                                                                                                           |
| **OR**(bool...)               | Logic      | Bool                | If any of the values are true, the function will return true, false otherwise.                                                                           |
| **GTEQ**(int, int)            | Check      | Bool                | Checks whether the first argument is greater than, or equal to the second one. Returns true/false accordingly.                                           |
| **GT**(int, int)              | Check      | Bool                | Checks whether the first argument is greater than the second one. Returns true/false accordingly.                                                        |
| **LTEQ**(int, int)            | Check      | Bool                | Checks whether the first argument is less than, or equal to the second one. Returns true/false accordingly.                                              |
| **LT**(int, int)              | Check      | Bool                | Checks whether the first argument is less than the second one. Returns true/false accordingly.                                                           |
| **EW**(string, string...)     | Check      | Bool                | Checks whether the first argument ends with any of the following arguments. Returns true/false accordingly.                                              |
| **SW**(string, string...)     | Check      | Bool                | Checks whether the first argument starts with any of the following arguments. Returns true/false accordingly.                                            |
| **EQ**(any, any)              | Check      | Bool                | Checks whether the two parameters equal. Returns true if they are equal, false otherwise.                                                                |
| **NE**(any, any)              | Check      | Bool                | Checks whether the two parameters are not equal. Returns true if they are not equal, false otherwise.                                                    |
| **ISEMPTY**(string)           | Check      | Bool                | Returns true if the given string argument is empty (contains no characters)                                                                              |
| **MATCH**(string, string)     | Check      | Bool                | Checks whether the second argument matches the regular expression provided in the first argument. Returns true if they match                             |
| **ADD**(int, int)             | Arithmetic | Int                 | Adds the two integers together.                                                                                                                          |
| **SUB**(int, int)             | Arithmetic | Int                 | Subtracts the second integer from the first one.                                                                                                         |
| **MUL**(int, int)             | Arithmetic | Int                 | Multiplies the two integers.                                                                                                                             |
| **DIV**(int, int)             | Arithmetic | Int                 | Divides the first integer with the second one.                                                                                                           |
| **MOD**(int, int)             | Arithmetic | Int                 | Modulus operation. Calculates the remainder of truncating division of the first integer by the second one                                                |
| **CONCAT**(any...)            | Utility    | String              | Concatenates the arguments together.                                                                                                                     |
| **JOIN**(string, any...)      | Utility    | String              | Creates a string from all the elements separated using separator (first argument).                                                                       |
| **RANDOM**(any...)            | Utility    | String, Int or Bool | Picks a random argument and returns it.                                                                                                                  |

</details>


## 🖌️ Customizing
Currently - due to time limits - other than the KDoc I will not supply a documentation for customization.
However, feel free to look at the built in features as an example.

You can customize Klarity in two different ways:
 - Adding custom translation sources
 - Adding new script functions
 - Add middlewares

### 🗂️ Translation Sources

Translation sources are responsible for retrieving the strings from whatever source, and to write default values (if supported).

To add a custom translation source, just implement the TranslationSource interface and register it via
```kotlin
Klarity.configure {
    sourceManagement {
        registerSource("en", source)
    }
}
```

### 📡 Middlewares
Middlewares are codes that can modify the output of `Klarity.translate()` after the script has been parsed.
This could be useful to add custom parsing, for example adding color code support in Minecraft plugins. Additionally it can do filtering and verification as well. 

Please note, that whatever the middleware outputs will become the output of `Klarity.translate()`. There can also be multiple middlewares, they are
called in the order of registration. Every middleware will get the output of the previous one as an input.

To create a middleware, implement the KlarityMiddleware interface and register it via
```kotlin
Klarity.configure {
    pluginManagement {
        registerMiddleware(middleware)
    }
}
```

### 🔤 Script functions

Script functions are the functions you can use inside the little scripting language. We've covered most of the useful functions, but who knows
maybe you come up with something really cool, in that case you can submit it to us via a PR. Of course you can have it remain private, if that what
you like, but we strongly recommend publishing it, so other translators can have much more power.

To create a function, simply implement the KlarityFunction abstract class. Since this class requires the ScriptParser instance, we have created an interface
called FunctionProvider to do that when creating a new ScriptParser instance.

To register your own script functions, simply call:

```kotlin
Klarity.configure {
    pluginManagement {
        registerFunction(FunctionProvider { YourFunction(it) })
    }
}
```

# ➕ Contribution
If you have a great idea for the project, feel free to contribute in the way outlined in [CONTRIBUTING.md](https://github.com/IceyLeagons/Klarity/blob/master/CONTRIBUTING.md)

# ⚖️ License
Klarity is licensed under the terms of MIT License.
See the actual license [here](https://github.com/IceyLeagons/Klarity/blob/master/LICENSE)

# ❤️ Projects using Klarity
If you want to see your project here, feel free to open an pull request.

- Be the first one!
