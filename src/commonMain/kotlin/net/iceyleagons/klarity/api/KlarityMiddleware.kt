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

package net.iceyleagons.klarity.api

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * Interface for middlewares.
 * Middlewares are codes that can modify the output of Klarity.translate() after the script has been parsed.
 * This could be useful to add custom parsing, for example adding color code support in Minecraft plugins.
 *
 * Please note, that whatever the middleware outputs will become the output of `Klarity.translate()`. There can also be multiple middlewares, they are
 * called in the order of registration. Every middleware will get the output of the previous one as an input.
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 23, 2023
 */
@JsExport
@OptIn(ExperimentalJsExport::class)
interface KlarityMiddleware {

    /**
     * Middleware function
     *
     * @param input the output of ScriptParser or the previous middleware
     * @return the processed input
     */
    fun modify(input: String): String = input

}