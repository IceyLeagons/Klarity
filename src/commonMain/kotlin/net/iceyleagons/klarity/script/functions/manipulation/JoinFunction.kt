package net.iceyleagons.klarity.script.functions.manipulation

import net.iceyleagons.klarity.parsers.generated.StringCodeParser
import net.iceyleagons.klarity.script.KlarityASTVisitor
import net.iceyleagons.klarity.script.KlarityFunction

class JoinFunction : KlarityFunction {

    override fun parse(ctx: StringCodeParser.FunctionContext, visitor: KlarityASTVisitor): String {
        val separator = ctx.eval(0, visitor)
        val list = mutableListOf<String>()

        for (i in 1..< ctx.findFunctionValue().size) {
            list.add(ctx.eval(i, visitor))
        }

        return list.joinToString(separator=separator)
    }
}