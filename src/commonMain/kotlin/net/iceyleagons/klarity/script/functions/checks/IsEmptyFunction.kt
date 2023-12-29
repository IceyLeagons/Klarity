package net.iceyleagons.klarity.script.functions.checks

import net.iceyleagons.klarity.parsers.generated.StringCodeParser
import net.iceyleagons.klarity.script.KlarityASTVisitor
import net.iceyleagons.klarity.script.KlarityFunction

class IsEmptyFunction : KlarityFunction {

    override fun parse(ctx: StringCodeParser.FunctionContext, visitor: KlarityASTVisitor): String {
        return (ctx.eval(0, visitor).isEmpty()).toStringRepresentation()
    }
}