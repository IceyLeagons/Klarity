package net.iceyleagons.klarity

import net.iceyleagons.klarity.api.TranslationSource

class DefaultValueSource : TranslationSource {

    override fun getRawString(key: String, defaultValue: String?): String? {
        return defaultValue
    }
}