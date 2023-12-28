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

import net.iceyleagons.klarity.script.functions.KlarityFunction
import net.iceyleagons.klarity.script.parsing.ScriptParser

/**
 * FunctionProvider is a utility interface to give us the option to pass the ScriptParser instance to the functions at runtime,
 * so the registrar will not have to deal with it.
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 23, 2023
 */
fun interface FunctionProvider {

    /**
     * Utility function to register a script function
     *
     * @param scriptParser the ScriptParser instance for the function to use
     * @return the defined KlarityFunction
     */
    fun getFunction(scriptParser: ScriptParser): KlarityFunction
}