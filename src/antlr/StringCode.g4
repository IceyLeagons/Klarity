grammar StringCode;

program         : statement+;

string          : STRING_LITERAL;

parameter       : PARAMETER;

number          : NUMBER;

statement       : function | parameter | string;

function        : FUNCTION_NAME '(' ((functionValue | ',')+)? ')';

functionValue    : parameter | function | string | number;

STRING_LITERAL  : '\'' ~('\'' | '\r' | '\n')* '\'';

NUMBER          : [0-9]+;

PARAMETER       : [a-z0-9]+;

FUNCTION_NAME   : [A-Z]+;

WS              : [ \t\r\n]+ -> skip;