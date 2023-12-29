package net.iceyleagons.klarity.script.functions.checks

import net.iceyleagons.klarity.parsers.generated.StringCodeParser
import net.iceyleagons.klarity.script.KlarityASTVisitor
import net.iceyleagons.klarity.script.KlarityFunction

class EndsWithFunction : KlarityFunction {

    override fun parse(ctx: StringCodeParser.FunctionContext, visitor: KlarityASTVisitor): String {
        val value = ctx.eval(0, visitor)

        for (i in 1..< ctx.findFunctionValue().size) {
            val param = ctx.eval(i, visitor)
            if (value.endsWith(param)) {
                return true.toStringRepresentation()
            }
        }

        return false.toStringRepresentation()
    }
}