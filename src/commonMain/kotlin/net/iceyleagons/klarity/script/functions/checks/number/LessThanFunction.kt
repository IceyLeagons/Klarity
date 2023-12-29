package net.iceyleagons.klarity.script.functions.checks.number

import net.iceyleagons.klarity.parsers.generated.StringCodeParser
import net.iceyleagons.klarity.script.KlarityASTVisitor
import net.iceyleagons.klarity.script.KlarityFunction

class LessThanFunction : KlarityFunction {

    override fun parse(ctx: StringCodeParser.FunctionContext, visitor: KlarityASTVisitor): String {
        val a = ctx.eval(0, visitor).assertNumber()
        val b = ctx.eval(1, visitor).assertNumber()

        return (a < b).toString()
    }
}