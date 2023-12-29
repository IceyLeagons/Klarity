package net.iceyleagons.klarity.script.functions.checks

import net.iceyleagons.klarity.parsers.generated.StringCodeParser
import net.iceyleagons.klarity.script.KlarityASTVisitor
import net.iceyleagons.klarity.script.KlarityFunction

class NotEqualsFunction : KlarityFunction {

    override fun parse(ctx: StringCodeParser.FunctionContext, visitor: KlarityASTVisitor): String {
        return (ctx.eval(0, visitor) != ctx.eval(1, visitor)).toStringRepresentation()
    }
}