package net.iceyleagons.klarity.script

import net.iceyleagons.klarity.parsers.generated.StringCodeParser

interface KlarityFunction {

    fun parse(ctx: StringCodeParser.FunctionContext, visitor: KlarityASTVisitor): String

    fun StringCodeParser.FunctionContext.evalAll(visitor: KlarityASTVisitor): List<String> {
        return this.findFunctionValue().map { eval(it, visitor) }
    }

    fun StringCodeParser.FunctionContext.eval(i: Int, visitor: KlarityASTVisitor): String {
        return eval(this.findFunctionValue(i)!!, visitor)
    }

    private fun eval(functionValueContext: StringCodeParser.FunctionValueContext, visitor: KlarityASTVisitor): String {
        val function = functionValueContext.findFunction()
        if (function == null) {
            val number = functionValueContext.findNumber()
            if (number == null) {
                val str =
                    functionValueContext.findString() ?: return visitor.visit(functionValueContext.findParameter()!!)
                        .toString()
                return visitor.visit(str).toString()
            }
            return visitor.visit(number).toString()
        }

        return visitor.visit(function).toString()
    }

    fun String.assertNumber(): Int  {
        if (!this.isInt()) {
            error("Number expected!")
        }
        return this.toInt()
    }

    fun String.isInt(): Boolean {
        return try {
            this.toInt()
            true
        } catch (_: NumberFormatException) {
            false
        }
    }

    fun String.toBoolean(): Boolean {
        if (this != "true" && this != "false") {
            error("Invalid boolean! Use the toStringRepresentation() function!")
        }

        return this == "true"
    }

    fun Boolean.toStringRepresentation(): String {
        return if (this) "true" else "false"
    }
}