grammar TranscriptionalReadThrough;

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
    : (((FORWARD_PROMOTER)+ (wildcard)+))+
    | (((wildcard)+ (REVERSE_PROMOTER)+))+ 
    | (((wildcard)+ (REVERSE_PROMOTER)+))+ (((FORWARD_PROMOTER)+ (wildcard)+))+  
    | ((wildcard)* ((transcriptional_readthrough)+ (wildcard)*)+)
    ;

transcriptional_readthrough
    : ((FORWARD_PROMOTER)+ (wildcard)* (FORWARD_PROMOTER)+)+
    | ((REVERSE_PROMOTER)+ (wildcard)* (REVERSE_PROMOTER)+)+
    | ((FORWARD_PROMOTER)+ (wildcard)* (REVERSE_TERMINATOR)+)+
    | ((FORWARD_TERMINATOR)+ (wildcard)* (REVERSE_PROMOTER)+)+
    //| ((REVERSE_PROMOTER)+ (wildcard)* (FORWARD_PROMOTER)+)+
    ;

wildcard
    : (FORWARD_RBS|REVERSE_RBS|FORWARD_CDS|REVERSE_CDS|FORWARD_TERMINATOR|REVERSE_TERMINATOR)+
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