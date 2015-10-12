grammar SuperCoiling;

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
    : ((wildcard)* (super_coiling) (wildcard)*)
    ;

super_coiling
    : (FORWARD_PROMOTER) (REVERSE_PROMOTER)
    | (REVERSE_PROMOTER) (FORWARD_PROMOTER)
    ;

wildcard
    : (FORWARD_RBS|REVERSE_RBS|FORWARD_CDS|REVERSE_CDS|FORWARD_TERMINATOR|REVERSE_TERMINATOR)+
    | ((FORWARD_RBS|REVERSE_RBS|FORWARD_CDS|REVERSE_CDS|FORWARD_TERMINATOR|REVERSE_TERMINATOR)+ (FORWARD_PROMOTER+))+
    | ((FORWARD_PROMOTER+) (FORWARD_RBS|REVERSE_RBS|FORWARD_CDS|REVERSE_CDS|FORWARD_TERMINATOR|REVERSE_TERMINATOR)+)+
    | ((FORWARD_PROMOTER+) (FORWARD_RBS|REVERSE_RBS|FORWARD_CDS|REVERSE_CDS|FORWARD_TERMINATOR|REVERSE_TERMINATOR)+ (FORWARD_PROMOTER+))+
    | ((FORWARD_RBS|REVERSE_RBS|FORWARD_CDS|REVERSE_CDS|FORWARD_TERMINATOR|REVERSE_TERMINATOR)+ (REVERSE_PROMOTER+))+
    | ((REVERSE_PROMOTER+) (FORWARD_RBS|REVERSE_RBS|FORWARD_CDS|REVERSE_CDS|FORWARD_TERMINATOR|REVERSE_TERMINATOR)+)+
    | ((REVERSE_PROMOTER+) (FORWARD_RBS|REVERSE_RBS|FORWARD_CDS|REVERSE_CDS|FORWARD_TERMINATOR|REVERSE_TERMINATOR)+ (REVERSE_PROMOTER+))+
    ;

/*
transcriptionl_readthrough
    :
    ;

transcriptional_interference
    :
    ;

reverse_strand_terminator
    :
    ;
*/

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