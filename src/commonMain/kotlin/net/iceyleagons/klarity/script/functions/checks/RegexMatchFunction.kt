package net.iceyleagons.klarity.script.functions.checks

import net.iceyleagons.klarity.parsers.generated.StringCodeParser
import net.iceyleagons.klarity.script.KlarityASTVisitor
import net.iceyleagons.klarity.script.KlarityFunction

class RegexMatchFunction : KlarityFunction {

    override fun parse(ctx: StringCodeParser.FunctionContext, visitor: KlarityASTVisitor): String {
        val regex = Regex(ctx.eval(0, visitor))
        val value = ctx.eval(1, visitor)

        return regex.matches(value).toStringRepresentation()
    }
}