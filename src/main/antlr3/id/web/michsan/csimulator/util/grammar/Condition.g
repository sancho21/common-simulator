// @author	Muhammad Ichsan <ichsan@gmail.com>
// @date	Nov 15th 2010

grammar Condition;

tokens {
	EQ='==';
	NEQ='!=';
	REQ='~~';
	RNEQ='!~';
	LEQ='%%';
	LNEQ='!%';
}

/* Not important. But it defines namespace for the parser */
@header {
    package id.web.michsan.csimulator.util.grammar;
    
    import java.util.HashMap;
	import java.util.Map;
}

/* Not important. But it defines namespace for the lexer */
@lexer::header {
	package id.web.michsan.csimulator.util.grammar;
}

/* Methods the parser may has */
@members {
	private Map<String, String> fields = new HashMap<String, String>();
	
	private String unwrap(String str) {
		return str.substring(1, str.length() - 1); 
	}
	
	// ANTLR will call this overridden method
	@Override
	public void emitErrorMessage(String msg) {
		// By default is printing to System.err.println();
       	ErrorReporter.report(msg);
    }
    
    /* Accessors ******************************************** */
    public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}
}

/* This will be the entry point of our parser. */
eval returns [boolean value]
	:	exp=booleanExp {$value = $exp.value;}
	;

booleanExp returns [boolean value]
	:	a=atomExp			{$value = $a.value;}		
		( '&&' a=atomExp	{$value = $value && $a.value;}
		| '||' a=atomExp	{$value = $value || $a.value;} 
		)*
	;

atomExp returns [boolean value]
	:	f=FIELD NEQ v=VALUE			{$value = !unwrap($v.text).equals(fields.get($f.text));}
		|	f=FIELD EQ v=VALUE		{$value = unwrap($v.text).equals(fields.get($f.text));}
		
		|	f=FIELD RNEQ v=VALUE	{$value = fields.get($f.text) != null ? !fields.get($f.text).matches(unwrap($v.text)) : true;}
		|	f=FIELD REQ v=VALUE		{$value = fields.get($f.text) != null ? fields.get($f.text).matches(unwrap($v.text)) : false;}

		|	f=FIELD LNEQ v=VALUE	{$value = fields.get($f.text) != null ? fields.get($f.text).indexOf(unwrap($v.text)) == -1 : true;}
		|	f=FIELD LEQ v=VALUE		{$value = fields.get($f.text) != null ? fields.get($f.text).indexOf(unwrap($v.text)) != -1 : false;}
		
		|	'(' exp=booleanExp ')'	{$value = $exp.value;}
	;

VALUE
	:'"' (.)* '"'
	;

FIELD
	: (~(SPACE | ENDLINE | '(' | ')' | '!' | '=' | '~' | '%'))+
	;

fragment SPACE:	' ' | '\t';
fragment ENDLINE: '\r' | '\n';

/* We're going to ignore all white space characters */
WS
	: (SPACE | ENDLINE) {$channel=HIDDEN;}
	;

/*
Sample to test:
f4 == "" || f2 == "f2" || wf@45 == "Go Ion" || (2 == "" || wf@45 != "5)") || 5 == "wow!" || 5i != " wonder!ful 5 == 6"
*/