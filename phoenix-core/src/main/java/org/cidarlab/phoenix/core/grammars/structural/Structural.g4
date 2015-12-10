grammar Structural;

@lexer::header{
    package org.cidarlab.phoenix.core.grammars.structural;
}
@parser::header{
    package org.cidarlab.phoenix.core.grammars.structural;
}

root
    :    module
    ;

module
    : (tu)+
    | (wildcard)+
    | ((wildcard)+ (tu)+)+
    | ((tu)+ (wildcard)+)+
    | ((wildcard)+ (tu)+ (wildcard)+)+
    ;  
tu
    : ((FORWARD_PROMOTER) (wildcard)*)+ ((FORWARD_RBS FORWARD_CDS) (wildcard)*)+ (FORWARD_TERMINATOR (wildcard)*)+
    ;


wildcard
    : (REVERSE_PROMOTER|REVERSE_RBS|REVERSE_CDS|REVERSE_TERMINATOR)+
    ;

REVERSE_PROMOTER
    : '<p'+
    ;
FORWARD_PROMOTER
    : 'p'+
    ;
REVERSE_RBS
    : '<r'
    ;
FORWARD_RBS
    : 'r'
    ;
REVERSE_CDS
    : '<c'
    | '<f'
    ;
FORWARD_CDS
    : 'c'
    | 'f'
    ;
REVERSE_TERMINATOR
    : '<t'
    ;
FORWARD_TERMINATOR
    : 't'
    ;

WS : [ \t\r\n]+ -> skip ;