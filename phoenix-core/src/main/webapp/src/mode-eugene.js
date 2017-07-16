define("ace/mode/doc_comment_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var DocCommentHighlightRules = require("./doc_comment_highlight_rules").DocCommentHighlightRules;
var TextHighlightRules = require("./text_highlight_rules").TextHighlightRules;

var eugeneHighlightRules = function() {
    var keywordControls = "boolean|num|txt|PartType|Property|Rule|Device|include|" +
                "else|for|if|return|while|OR|or|"+
                "CONTAINS|NOTCONTAINS|AFTER|ALL_AFTER|SOME_AFTER|BEFORE|ALL_BEFORE|" + 
                "SOME_BEFORE|STARTSWITH|ENDSWITH|WITH|NOTWITH|THEN|NEXTTO|ALL_NEXTTO|" +
                "SOME_NEXTTO|MORETHAN|NOTMORETHAN|EXACTLY|NOTEXACTLY|REPRESSES|INDUCES|" +
                "BINDS|DRIVES|ALL_REVERSE|REVERSE|SOME_REVERSE|ALL_FORWARD|FORWARD|" +
                "SOME_FORWARD|SAME_ORIENTATION|ALL_SAME_ORIENTATION|SAME_COUNT|" + 
                "ALTERNATE_ORIENTATION|TEMPLATE|SEQUENCE|" +
                "contains|notcontains|after|all_after|some_after|before|all_before|" +
                "some_before|startswith|endswith|with|notwith|then|nextto|all_nextto|" +
                "some_nextto|morethan|notmorethan|exactly|notexactly|represses|induces|" +
                "binds|drives|all_reverse|reverse|some_reverse|all_forward|forward|" +
                "some_forward|same_orientation|all_same_orientation|same_count|" +
                "alternate_orientation|template|sequence";

    var keywordOperators = (
        "and|and_eq|bitand|bitor|compl|not|not_eq|or|or_eq|typeid|xor|xor_eq" +
        "const_cast|dynamic_cast|reinterpret_cast|static_cast|sizeof|namespace"
    );

    var builtinConstants = (
        "NULL|true|false|TRUE|FALSE|nullptr"
    );

    var keywordMapper = this.$keywords = this.createKeywordMapper({
        "keyword.control" : keywordControls,
        "keyword.operator" : keywordOperators,
        "variable.language": "this",
        "constant.language": builtinConstants
    }, "identifier");

    var identifierRe = "[a-zA-Z\\$_\u00a1-\uffff][a-zA-Z\\d\\$_\u00a1-\uffff]*\\b";
    var escapeRe = /\\(?:['"?\\abfnrtv]|[0-7]{1,3}|x[a-fA-F\d]{2}|u[a-fA-F\d]{4}U[a-fA-F\d]{8}|.)/.source;

    // regexp must not have capturing parentheses. Use (?:) instead.
    // regexps are ordered -> the first match is used

    this.$rules = { 
        "start" : [
            {
                token : "comment",
                regex : "//$",
                next : "start"
            }, {
                token : "comment",
                regex : "//",
                next : "singleLineComment"
            },
            DocCommentHighlightRules.getStartRule("doc-start"),
            {
                token : "comment", // multi line comment
                regex : "\\/\\*",
                next : "comment"
            }, {
                token : "string", // character
                regex : "'(?:" + escapeRe + "|.)?'"
            }, {
                token : "string.start",
                regex : '"', 
                stateName: "qqstring",
                next: [
                    { token: "string", regex: /\\\s*$/, next: "qqstring" },
                    { token: "constant.language.escape", regex: escapeRe },
                    { token: "constant.language.escape", regex: /%[^'"\\]/ },
                    { token: "string.end", regex: '"|$', next: "start" },
                    { defaultToken: "string"}
                ]
            }, {
                token : "string.start",
                regex : 'R"\\(', 
                stateName: "rawString",
                next: [
                    { token: "string.end", regex: '\\)"', next: "start" },
                    { defaultToken: "string"}
                ]
            }, {
                token : "constant.numeric", // hex
                regex : "0[xX][0-9a-fA-F]+(L|l|UL|ul|u|U|F|f|ll|LL|ull|ULL)?\\b"
            }, {
                token : "constant.numeric", // float
                regex : "[+-]?\\d+(?:(?:\\.\\d*)?(?:[eE][+-]?\\d+)?)?(L|l|UL|ul|u|U|F|f|ll|LL|ull|ULL)?\\b"
            }, {
                token : "keyword", // pre-compiler directives
                regex : "#\\s*(?:include|import|pragma|line|define|undef)\\b",
                next  : "directive"
            }, {
                token : "keyword", // special case pre-compiler directive
                regex : "#\\s*(?:endif|if|ifdef|else|elif|ifndef)\\b"
            }, {
                token : "support.function.C99.c",
                regex : cFunctions
            }, {
                token : keywordMapper,
                regex : "[a-zA-Z_$][a-zA-Z0-9_$]*"
            }, {
                token : "keyword.operator",
                regex : /--|\+\+|<<=|>>=|>>>=|<>|&&|\|\||\?:|[*%\/+\-&\^|~!<>=]=?/
            }, {
              token : "punctuation.operator",
              regex : "\\?|\\:|\\,|\\;|\\."
            }, {
                token : "paren.lparen",
                regex : "[[({]"
            }, {
                token : "paren.rparen",
                regex : "[\\])}]"
            }, {
                token : "text",
                regex : "\\s+"
            }
        ],
        "comment" : [
            {
                token : "comment", // closing comment
                regex : ".*?\\*\\/",
                next : "start"
            }, {
                token : "comment", // comment spanning whole line
                regex : ".+"
            }
        ],
        "singleLineComment" : [
            {
                token : "comment",
                regex : /\\$/,
                next : "singleLineComment"
            }, {
                token : "comment",
                regex : /$/,
                next : "start"
            }, {
                defaultToken: "comment"
            }
        ],
        "directive" : [
            {
                token : "constant.other.multiline",
                regex : /\\/
            },
            {
                token : "constant.other.multiline",
                regex : /.*\\/
            },
            {
                token : "constant.other",
                regex : "\\s*<.+?>",
                next : "start"
            },
            {
                token : "constant.other", // single line
                regex : '\\s*["](?:(?:\\\\.)|(?:[^"\\\\]))*?["]',
                next : "start"
            }, 
            {
                token : "constant.other", // single line
                regex : "\\s*['](?:(?:\\\\.)|(?:[^'\\\\]))*?[']",
                next : "start"
            },
            // "\" implies multiline, while "/" implies comment
            {
                token : "constant.other",
                regex : /[^\\\/]+/,
                next : "start"
            }
        ]
    };

    this.embedRules(DocCommentHighlightRules, "doc-",
        [ DocCommentHighlightRules.getEndRule("start") ]);
    this.normalizeRules();
};

oop.inherits(eugeneHighlightRules, TextHighlightRules);

exports.eugeneHighlightRules = eugeneHighlightRules;
});

define("ace/mode/eugene",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/eugene_highlight_rules"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var TextMode = require("./text").Mode;
var eugeneHighlightRules = require("./eugene_highlight_rules").eugeneHighlightRules;

var Mode = function() {
    this.HighlightRules = eugeneHighlightRules;
    this.$behaviour = this.$defaultBehaviour;
};
oop.inherits(Mode, TextMode);

(function() {

    this.lineCommentStart = "--";

    this.$id = "ace/mode/eguene";
}).call(Mode.prototype);

exports.Mode = Mode;

});