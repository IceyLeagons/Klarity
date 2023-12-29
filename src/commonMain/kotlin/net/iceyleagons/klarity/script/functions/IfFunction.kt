package net.iceyleagons.klarity.script.functions

import net.iceyleagons.klarity.parsers.generated.StringCodeParser
import net.iceyleagons.klarity.script.KlarityASTVisitor
import net.iceyleagons.klarity.script.KlarityFunction

class IfFunction : KlarityFunction {
    override fun parse(ctx: StringCodeParser.FunctionContext, visitor: KlarityASTVisitor): String {
        val condition = ctx.eval(0, visitor).toBoolean()
        return if (condition) ctx.eval(1, visitor) else ctx.eval(2, visitor)
    }
}