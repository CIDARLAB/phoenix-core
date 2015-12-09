grammar TranscriptionalInterference;

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
    : (((FORWARD_PROMOTER)+ (wildcard_rbs_cds_term)+))+
    | (((wildcard_rbs_cds_term)+ (REVERSE_PROMOTER)+))+ 
    | (((wildcard_rbs_cds_term)+ (REVERSE_PROMOTER)+))+ (((FORWARD_PROMOTER)+ (wildcard_rbs_cds_term)+))+  
    | ((wildcard_rbs_cds_term)* ((transcriptional_interference)+ (wildcard_rbs_cds_term)*)+)
    ;

transcriptional_interference
    : ((FORWARD_PROMOTER)+ (wildcard_rbs_cds)* (REVERSE_PROMOTER)+)+
    | ((FORWARD_PROMOTER)+ (wildcard_rbs_cds_ft)* (REVERSE_PROMOTER)+)+
    | ((FORWARD_PROMOTER)+ (wildcard_rbs_cds_rt)* (REVERSE_PROMOTER)+)+
    //| ((REVERSE_PROMOTER)+ (wildcard)* (FORWARD_PROMOTER)+)+
    ;

wildcard_rbs_cds_term
    : (FORWARD_RBS|REVERSE_RBS|FORWARD_CDS|REVERSE_CDS|FORWARD_TERMINATOR|REVERSE_TERMINATOR)+
    ;
wildcard_rbs_cds
    : (FORWARD_RBS|REVERSE_RBS|FORWARD_CDS|REVERSE_CDS)+
    ;
wildcard_rbs_cds_ft
    : (FORWARD_RBS|REVERSE_RBS|FORWARD_CDS|REVERSE_CDS|FORWARD_TERMINATOR)+
    ; 
wildcard_rbs_cds_rt
    : (FORWARD_RBS|REVERSE_RBS|FORWARD_CDS|REVERSE_CDS|REVERSE_TERMINATOR)+
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