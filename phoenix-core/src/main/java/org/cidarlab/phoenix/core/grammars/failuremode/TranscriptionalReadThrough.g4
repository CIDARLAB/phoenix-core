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
    : ((transcriptional_readthrough)+ (wildcard)+)+
    | ((wildcard)+ (transcriptional_readthrough)+)+
    | ((wildcard)+ (transcriptional_readthrough)+ (wildcard)+)+
    | (((reverse_strand)* (forward_strand)+))+
    | (((reverse_strand)+ (forward_strand)*))+
    | (wildcard+)
    ;

transcriptional_readthrough
    : ((FORWARD_PROMOTER)+ (wildcard_type1)+ (FORWARD_PROMOTER)+)
    | ((REVERSE_PROMOTER)+ (wildcard_type1)+ (REVERSE_PROMOTER)+)
    //| ((FORWARD_PROMOTER)+ (wildcard_type2)* (REVERSE_TERMINATOR)+)
    //| ((FORWARD_TERMINATOR)+ (wildcard_type3)* (REVERSE_PROMOTER)+)
    ;

reverse_strand
    : (REVERSE_PROMOTER|REVERSE_RBS|REVERSE_CDS|REVERSE_TERMINATOR)+
    ;
forward_strand
    : (FORWARD_PROMOTER|FORWARD_RBS|FORWARD_CDS|FORWARD_TERMINATOR)+
    ;
wildcard
    : (REVERSE_PROMOTER|REVERSE_RBS|REVERSE_CDS|REVERSE_TERMINATOR|FORWARD_PROMOTER|FORWARD_RBS|FORWARD_CDS|FORWARD_TERMINATOR)+  
    ;
wildcard_type1
    : (FORWARD_RBS|REVERSE_RBS|FORWARD_CDS|REVERSE_CDS|FORWARD_TERMINATOR|REVERSE_TERMINATOR)+
    ;
wildcard_type2
    : (FORWARD_RBS|REVERSE_RBS|FORWARD_CDS|REVERSE_CDS|FORWARD_TERMINATOR|REVERSE_PROMOTER)+
    ;
wildcard_type3
    : (FORWARD_RBS|REVERSE_RBS|FORWARD_CDS|REVERSE_CDS|REVERSE_TERMINATOR|FORWARD_PROMOTER)+
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