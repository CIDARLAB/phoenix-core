grammar ReverseStructural;

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
    : ((module_wildcard_start)* (reverse_tu)+ (module_wildcard_end)*)*
    ;  
reverse_tu
    : (FORWARD_PROMOTER)+ (wildcard_fp)* (FORWARD_RBS FORWARD_CDS)+ (wildcard_fc)* FORWARD_TERMINATOR
    ;

//reverse_tu
//    : REVERSE_TERMINATOR (wildcard_rc)* (REVERSE_CDS REVERSE_RBS)+ (wildcard_rp)* (REVERSE_PROMOTER)+ 
//    ;  
module_wildcard_start
    : (REVERSE_PROMOTER|REVERSE_RBS|REVERSE_CDS|REVERSE_TERMINATOR|FORWARD_RBS|FORWARD_CDS|FORWARD_TERMINATOR)+
    ;
module_wildcard_end
    : (REVERSE_PROMOTER|REVERSE_RBS|REVERSE_CDS|REVERSE_TERMINATOR|FORWARD_PROMOTER|FORWARD_RBS|FORWARD_CDS|FORWARD_TERMINATOR)+
    ;
wildcard_fp
    : (REVERSE_PROMOTER|REVERSE_RBS|REVERSE_CDS|REVERSE_TERMINATOR|FORWARD_CDS)+
    ;
wildcard_fc
    : (REVERSE_PROMOTER|REVERSE_RBS|REVERSE_CDS|REVERSE_TERMINATOR|FORWARD_PROMOTER)+
    ;
wildcard_rp
    : (FORWARD_PROMOTER|FORWARD_RBS|FORWARD_CDS|FORWARD_TERMINATOR|REVERSE_CDS)+
    ;
wildcard_rc
    : (FORWARD_PROMOTER|FORWARD_RBS|FORWARD_CDS|FORWARD_TERMINATOR|REVERSE_PROMOTER)+
    ;

REVERSE_PROMOTER
    : 'p>'+
    ;
FORWARD_PROMOTER
    : 'p'+
    ;
REVERSE_RBS
    : 'r>'
    ;
FORWARD_RBS
    : 'r'
    ;
REVERSE_CDS
    : 'c>'+
    | 'f>'+
    ;
FORWARD_CDS
    : 'c'+
    | 'f'+
    ;
REVERSE_TERMINATOR
    : 't>'
    ;
FORWARD_TERMINATOR
    : 't'
    ;

WS : [ \t\r\n]+ -> skip ;