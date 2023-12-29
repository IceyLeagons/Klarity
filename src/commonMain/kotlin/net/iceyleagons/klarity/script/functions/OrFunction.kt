package net.iceyleagons.klarity.script.functions

import net.iceyleagons.klarity.parsers.generated.StringCodeParser
import net.iceyleagons.klarity.script.KlarityASTVisitor
import net.iceyleagons.klarity.script.KlarityFunction

class OrFunction : KlarityFunction {

    override fun parse(ctx: StringCodeParser.FunctionContext, visitor: KlarityASTVisitor): String {
        val list = ctx.evalAll(visitor)
        for (s in list) {
            if (s.toBoolean()) {
                return true.toStringRepresentation()
            }
        }

        return false.toStringRepresentation()
    }
}