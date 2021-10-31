grammar Mx;

program : subProgram * EOF;

subProgram : varDef | funcDef | classDef;
varDef : varType subVar (',' subVar)* ';';
funcDef : returnType? Identifier '(' parameterList? ')' suite;
classDef : Class Identifier '{' (varDef | funcDef)* '}' ';';

suite : '{' statement* '}';
subVar : Identifier ('=' expression)?;
parameterList : varType Identifier (',' varType Identifier)*;

statement
    : suite                                                                          #block
    | varDef                                                                         #varDefStmt
    | If '(' expression ')' trueStmt=statement (Else falseStmt=statement)?           #ifStmt
    | While '(' expression ')' statement                                             #whileStmt
    | For '(' init=expression? ';' cond=expression? ';' incr=expression? ')' statement  #forStmt
    | Return expression? ';'                                                         #returnStmt
    | Break ';'                                                                      #breakStmt
    | Continue ';'                                                                   #continueStmt
    | expression ';'                                                                 #pureExprStmt
    | ';'                                                                            #emptyStmt
    ;

expression
    : primary #atomExpr
    | '[' '&' ']' ('(' parameterList ')')* '-' '>'  suite '(' expressionList? ')'    #lambdaExpr
    | <assoc=right> New creator                                                      #newExpr
    | expression '[' expression ']'                                                  #indexExpr //2
    | expression '(' expressionList? ')'                                             #functionExpr //2
    | expression '.' Identifier                                                      #memberExpr //2
    | expression op=('++' | '--')                                                    #suffixExpr //2
    | <assoc=right> op=('!' | '~' | '++' | '--' | '+' | '-') expression              #prefixExpr //3
    | expression op=('*' | '/' | '%') expression                                     #binaryExpr //5
    | expression op=('+' | '-') expression                                           #binaryExpr //6
    | expression op=('<<' | '>>') expression                                         #binaryExpr //7
    | expression op=('>' | '<' | '>=' | '<=') expression                             #binaryExpr //8
    | expression op=('==' | '!=') expression                                         #binaryExpr //9
    | expression op='&' expression                                                   #binaryExpr //10
    | expression op='^' expression                                                   #binaryExpr //11
    | expression op='|' expression                                                   #binaryExpr //12
    | expression op='&&' expression                                                  #binaryExpr //13
    | expression op='||' expression                                                  #binaryExpr //14
    | <assoc=right> expression '=' expression                                        #assignExpr //16
    ;

primary : '(' expression ')' | Identifier | literal | This;
literal : True | False | ConstInteger | ConstString | Null;
expressionList : expression (',' expression)*;
creator
    : basicType ('[' expression ']')+ ('[' ']')+ ('[' expression ']')+               #wrongCreator
    | basicType ('[' expression ']')+ ('[' ']')*                                     #arrayCreator
    | basicType ('(' ')')?                                                           #basicCreator
    ;

basicType : Bool | Int | String | Identifier;
returnType : basicType ('[' ']')* | Void;
varType : basicType ('['']')*;

Plus : '+';
Minus : '-';
Multiply : '*';
Divide : '/';
Modulo : '%';

Greater : '>';
Less : '<';
GreaterEqual : '>=';
LessEqual : '<=';
NotEqual : '!=';
Equal : '==';

AndAnd : '&&';
OrOr : '||';
Not :'!';

RightShift : '>>';
LeftShift : '<<';
And : '&';
Or : '|';
Caret : '^';
Tilde : '~';

Assign : '=';
SelfPlus : '++';
SelfMinus : '--';

Dot : '.';
Semi : ';';
Comma : ',';
Question : '?';
Colon : ':';
DbQuotation : '"';

LeftParen : '(';
RightParen : ')';
LeftBracket : '[';
RightBracket : ']';
LeftBrace : '{';
RightBrace : '}';

Int : 'int';
Bool : 'bool';
String : 'string';
Null : 'null';
Void : 'void';
True : 'true';
False : 'false';
If : 'if';
Else : 'else';
For : 'for';
While : 'while';
Break : 'break';
Continue : 'continue';
Return : 'return';
New : 'new';
Class : 'class';
This : 'this';

WhiteSpace : [ \t]+ -> skip;
NewLine : ('\r' '\n'? | '\n') -> skip;
LineComment : '//' ~[\r\n]* -> skip;
BlockComment : '/*' .*? '*/' -> skip;

Identifier : [a-zA-Z] [a-zA-Z_0-9]*;
ConstInteger : [1-9] [0-9]* | '0';
ConstString : '"' (~["\\\t\n\r] | '\\' ["\\tnr])* '"';
ConstBool : True | False;