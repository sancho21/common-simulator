// @author	Muhammad Ichsan <ichsan@gmail.com>
// @date	Nov 15th 2010

grammar Condition;

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

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}
}

/* This will be the entry point of our parser. */
eval returns [boolean value]
	:	exp=booleanExp {$value = $exp.value;}
	;

booleanExp returns [boolean value]
	:	a1=atomExp			{$value = $a1.value;}		
		( '&&' a2=atomExp	{$value = $value && $a2.value;}
		| '||' a3=atomExp	{$value = $value || $a3.value;} 
		)*
	;

atomExp returns [boolean value]
	:	f=VALID_CHARS '!:' v=VALID_CHARS	{$value = !$v.text.equals(fields.get($f.text));}
		|	f=VALID_CHARS ':' v=VALID_CHARS	{$value = $v.text.equals(fields.get($f.text));}
		|	f=VALID_CHARS '!:'				{$value = !"".equals(fields.get($f.text));}
		|	f=VALID_CHARS ':'				{$value = "".equals(fields.get($f.text));}
		|	'(' exp=booleanExp ')'			{$value = $exp.value;}
	;

VALID_CHARS
	:	(~(' ' | '\t' | '\n' | '\r' | ':' | '(' | ')' | '!'))+
	;

/* We're going to ignore all white space characters */
WS
	:	(' ' | '\t' | '\r'| '\n') {$channel=HIDDEN;}
	;