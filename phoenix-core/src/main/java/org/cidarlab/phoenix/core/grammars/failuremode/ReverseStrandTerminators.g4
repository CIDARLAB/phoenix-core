grammar ReverseStrandTerminators;

@lexer::header{
    package org.cidarlab.phoenix.core.grammars.failuremode;
}
@parser::header{
    package org.cidarlab.phoenix.core.grammars.failuremode;
}

root
    : module
    ;

module 
    : ((reverse_strand_terminators)+ (wildcard)*)+
    | ((wildcard)* (reverse_strand_terminators)+)+
    | ((wildcard)* (reverse_strand_terminators)+ (wildcard)*)+
    | (wildcard)+
    ;

reverse_strand_terminators
    : ((FORWARD_PROMOTER)+ (wildcard_type1)* (REVERSE_TERMINATOR))+
    | ((FORWARD_TERMINATOR) (wildcard_type2)* (REVERSE_PROMOTER)+)+
    ;

wildcard_type1
    : (FORWARD_RBS|FORWARD_CDS|REVERSE_CDS|REVERSE_RBS|REVERSE_PROMOTER)+
    ;

wildcard_type2
    : (REVERSE_CDS|REVERSE_RBS|FORWARD_CDS|FORWARD_RBS|FORWARD_PROMOTER)+
    ;

wildcard
    : (FORWARD_PROMOTER|FORWARD_RBS|FORWARD_CDS|FORWARD_TERMINATOR|REVERSE_PROMOTER|REVERSE_RBS|REVERSE_CDS|REVERSE_TERMINATOR)+
    ;

REVERSE_PROMOTER
    : '<p'
    ;
FORWARD_PROMOTER
    : 'p'
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