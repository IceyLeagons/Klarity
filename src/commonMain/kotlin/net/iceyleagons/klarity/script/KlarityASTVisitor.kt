package net.iceyleagons.klarity.script

import net.iceyleagons.klarity.parsers.generated.StringCodeBaseVisitor
import net.iceyleagons.klarity.parsers.generated.StringCodeParser

class KlarityASTVisitor(private val params: Map<String, String>, private val functions: Map<String, KlarityFunction>) : StringCodeBaseVisitor<String>() {

    override fun visitFunction(ctx: StringCodeParser.FunctionContext): String {
        val functionName = ctx.FUNCTION_NAME().toString()
        if (!functions.containsKey(functionName)) {
            error("Unexpected function name!")
        }

        return functions[functionName]!!.parse(ctx, this)
    }

    override fun visitString(ctx: StringCodeParser.StringContext): String {
        val raw = ctx.STRING_LITERAL().toString()
        return raw.substring(1, raw.length - 1)
    }

    override fun visitNumber(ctx: StringCodeParser.NumberContext): String {
        return ctx.NUMBER().toString()
    }

    override fun visitParameter(ctx: StringCodeParser.ParameterContext): String {
        val parameterName = ctx.PARAMETER().toString()
        return params[parameterName] ?: error("Invalid parameter given!")
    }
}