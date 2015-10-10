grammar Structural;

options {
    langauge = Java;
    output = AST;
}

@header{
    org.cidarlab.phoenix.core.grammars;
}

@lexer::header{
    org.cidarlab.phoenix.core.grammars;
}
@parser::header{
    org.cidarlab.phoenix.core.grammars;
}


tokens {
    reverse_promoter = '<p';
    forward_promoter = 'p';
    reverse_rbs = '<r';
    forward_rbs = 'r';
    reverse_cds = '<c';
    forward_cds = 'c';
    reverse_fusion_cds = '<f';
    forward_fusion_cds = 'f';
    reverse_terminator = '<t';
    forward_terminator = 't';
}

eval
    :    root
    ;


    
    