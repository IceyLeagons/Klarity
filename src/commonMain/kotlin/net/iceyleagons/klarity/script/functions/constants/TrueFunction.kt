package net.iceyleagons.klarity.script.functions.constants

import net.iceyleagons.klarity.parsers.generated.StringCodeParser
import net.iceyleagons.klarity.script.KlarityASTVisitor
import net.iceyleagons.klarity.script.KlarityFunction

class TrueFunction : KlarityFunction {
    override fun parse(ctx: StringCodeParser.FunctionContext, visitor: KlarityASTVisitor): String {
        return true.toStringRepresentation()
    }
}