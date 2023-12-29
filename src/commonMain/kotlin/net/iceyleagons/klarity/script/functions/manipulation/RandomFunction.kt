package net.iceyleagons.klarity.script.functions.manipulation

import net.iceyleagons.klarity.parsers.generated.StringCodeParser
import net.iceyleagons.klarity.script.KlarityASTVisitor
import net.iceyleagons.klarity.script.KlarityFunction

class RandomFunction : KlarityFunction {

    override fun parse(ctx: StringCodeParser.FunctionContext, visitor: KlarityASTVisitor): String {
        val list = ctx.evalAll(visitor)
        return list.random()
    }
}