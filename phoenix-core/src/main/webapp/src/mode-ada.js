define("ace/mode/ada_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var TextHighlightRules = require("./text_highlight_rules").TextHighlightRules;

var AdaHighlightRules = function() {
var keywords = "boolean|num|txt|PartType|Property|Rule|Device|include|" +
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

    var builtinConstants = (
        "true|false|null"
    );

    var builtinFunctions = (
        "count|min|max|avg|sum|rank|now|coalesce|main"
    );

    var keywordMapper = this.createKeywordMapper({
        "support.function": builtinFunctions,
        "keyword": keywords,
        "constant.language": builtinConstants
    }, "identifier", true);

    this.$rules = {
        "start" : [ {
            token : "comment",
            regex : "--.*$"
        }, {
            token : "string",           // " string
            regex : '".*?"'
        }, {
            token : "string",           // ' string
            regex : "'.*?'"
        }, {
            token : "constant.numeric", // float
            regex : "[+-]?\\d+(?:(?:\\.\\d*)?(?:[eE][+-]?\\d+)?)?\\b"
        }, {
            token : keywordMapper,
            regex : "[a-zA-Z_$][a-zA-Z0-9_$]*\\b"
        }, {
            token : "keyword.operator",
            regex : "\\+|\\-|\\/|\\/\\/|%|<@>|@>|<@|&|\\^|~|<|>|<=|=>|==|!=|<>|="
        }, {
            token : "paren.lparen",
            regex : "[\\(]"
        }, {
            token : "paren.rparen",
            regex : "[\\)]"
        }, {
            token : "text",
            regex : "\\s+"
        } ]
    };
};

oop.inherits(AdaHighlightRules, TextHighlightRules);

exports.AdaHighlightRules = AdaHighlightRules;
});

define("ace/mode/ada",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/ada_highlight_rules"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var TextMode = require("./text").Mode;
var AdaHighlightRules = require("./ada_highlight_rules").AdaHighlightRules;

var Mode = function() {
    this.HighlightRules = AdaHighlightRules;
    this.$behaviour = this.$defaultBehaviour;
};
oop.inherits(Mode, TextMode);

(function() {

    this.lineCommentStart = "--";

    this.$id = "ace/mode/ada";
}).call(Mode.prototype);

exports.Mode = Mode;

});
