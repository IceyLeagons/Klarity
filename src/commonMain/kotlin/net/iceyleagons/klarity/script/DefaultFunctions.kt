package net.iceyleagons.klarity.script

import net.iceyleagons.klarity.script.functions.AndFunction
import net.iceyleagons.klarity.script.functions.IfFunction
import net.iceyleagons.klarity.script.functions.NotFunction
import net.iceyleagons.klarity.script.functions.OrFunction
import net.iceyleagons.klarity.script.functions.arithmetic.*
import net.iceyleagons.klarity.script.functions.checks.*
import net.iceyleagons.klarity.script.functions.checks.number.GreaterThanEqualFunction
import net.iceyleagons.klarity.script.functions.checks.number.GreaterThanFunction
import net.iceyleagons.klarity.script.functions.checks.number.LessThanEqualFunction
import net.iceyleagons.klarity.script.functions.checks.number.LessThanFunction
import net.iceyleagons.klarity.script.functions.constants.FalseFunction
import net.iceyleagons.klarity.script.functions.constants.TrueFunction
import net.iceyleagons.klarity.script.functions.manipulation.ConcatFunction
import net.iceyleagons.klarity.script.functions.manipulation.JoinFunction
import net.iceyleagons.klarity.script.functions.manipulation.RandomFunction

object DefaultFunctions {

    private val functions: Map<String, KlarityFunction> = mapOf(
        "FALSE" to FalseFunction(),
        "TRUE" to TrueFunction(),

        "IF" to IfFunction(),
        "NOT" to NotFunction(),

        "EQ" to EqualsFunction(),
        "NE" to NotEqualsFunction(),
        "AND" to AndFunction(),
        "OR" to OrFunction(),

        "GTEQ" to GreaterThanEqualFunction(),
        "GT" to GreaterThanFunction(),
        "LTEQ" to LessThanEqualFunction(),
        "LT" to LessThanFunction(),

        "EW" to EndsWithFunction(),
        "SW" to StartsWithFunction(),
        "ISEMPTY" to IsEmptyFunction(),
        "MATCH" to RegexMatchFunction(),

        "ADD" to AddFunction(),
        "SUB" to SubtractFunction(),
        "MUL" to MultiplyFunction(),
        "DIV" to DivideFunction(),
        "MOD" to ModulusFunction(),

        "CONCAT" to ConcatFunction(),
        "JOIN" to JoinFunction(),
        "RANDOM" to RandomFunction()
    )

    fun get(): Map<String, KlarityFunction> {
        return functions
    }
}