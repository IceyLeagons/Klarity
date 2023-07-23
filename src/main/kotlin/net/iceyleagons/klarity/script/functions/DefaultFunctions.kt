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

package net.iceyleagons.klarity.script.functions

import net.iceyleagons.klarity.api.FunctionProvider
import net.iceyleagons.klarity.script.functions.impl.AndFunction
import net.iceyleagons.klarity.script.functions.impl.IfFunction
import net.iceyleagons.klarity.script.functions.impl.NotFunction
import net.iceyleagons.klarity.script.functions.impl.OrFunction
import net.iceyleagons.klarity.script.functions.impl.checks.*
import net.iceyleagons.klarity.script.functions.impl.checks.number.GreaterThanEqualFunction
import net.iceyleagons.klarity.script.functions.impl.checks.number.GreaterThanFunction
import net.iceyleagons.klarity.script.functions.impl.checks.number.LessThanEqualFunction
import net.iceyleagons.klarity.script.functions.impl.checks.number.LessThanFunction
import net.iceyleagons.klarity.script.functions.impl.constants.FalseConstant
import net.iceyleagons.klarity.script.functions.impl.constants.TrueConstant
import net.iceyleagons.klarity.script.functions.impl.manipulation.ConcatFunction
import net.iceyleagons.klarity.script.functions.impl.manipulation.JoinFunction
import net.iceyleagons.klarity.script.functions.impl.manipulation.RandomFunction
import net.iceyleagons.klarity.script.functions.impl.manipulation.number.*

/**
 * This singleton defines the built in script functions.
 *
 * @property functions set containing the default functions' providers.
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Jul. 23, 2023
 */
object DefaultFunctions {

    val functions: Set<FunctionProvider> = setOf(
        FunctionProvider { TrueConstant(it) },
        FunctionProvider { FalseConstant(it) },

        FunctionProvider { GreaterThanEqualFunction(it) },
        FunctionProvider { GreaterThanFunction(it) },
        FunctionProvider { LessThanEqualFunction(it) },
        FunctionProvider { LessThanFunction(it) },

        FunctionProvider { EndsWithFunction(it) },
        FunctionProvider { EqualsFunction(it) },
        FunctionProvider { IsEmptyFunction(it) },
        FunctionProvider { NotEqualsFunction(it) },
        FunctionProvider { StartsWithFunction(it) },

        FunctionProvider { AddFunction(it) },
        FunctionProvider { DivideFunction(it) },
        FunctionProvider { ModulusFunction(it) },
        FunctionProvider { MultiplyFunction(it) },
        FunctionProvider { SubtractFunction(it) },

        FunctionProvider { ConcatFunction(it) },
        FunctionProvider { JoinFunction(it) },
        FunctionProvider { RandomFunction(it) },

        FunctionProvider { AndFunction(it) },
        FunctionProvider { IfFunction(it) },
        FunctionProvider { NotFunction(it) },
        FunctionProvider { OrFunction(it) }
    )
}