package translaters;

public class PortugolBook{
	
	public static final String PALAVRA_CHAVE = "PALAVRA CHAVE";
	public static final String ATRIBUIDOR = "ATRIBUIDOR";
	public static final String INDEFINIDO = "INDEFINIDO";
	public static final String NOMEACAO_DO_PROGRAMA = "NOMEACAO DO PROGRAMA";
	public static final String TIPO_DE_DADO = "TIPO DE DADO";
	public static final String IDENTIFICADOR = "IDENTIFICAOR";
	public static final String TIPIFICACAO = "TIPIFICACAO";
	public static final String INICIO = "INICIO";
	public static final String METODO = "METODO";
	public static final String OPERADOR_LOGICO = "OPERADOR_LOGICO";
	public static final String PARTE_DO_SE = "PARTE DO SE";
	public static final String PARTE_DO_FOR = "PARTE DO FOR";
	public static final String OPERADOR_MATEMATICO = "OPERADOR MATEMATICO";
	public static final String STRING_TYPE = "STRING";
	public static final String BOOLEAN_TYPE = "BOOLEAN";
	public static final String SIMBOLO = "SIMBOLO";
	public static final String DO_SOMETHING = "DO SOMETHING";
	public static final String PARTE_DO_REPITA = "PARTE DO REPITA";
	public static final String BOOLEAN_FALSE = "FALSE";
	public static final String BOOLEAN_TRUE = "TRUE";
	public static final String VALOR_LOGICO = "VALOR LOGICO";
	
	
	
	public static final PortugolToken PROGRAM = new PortugolToken("algoritmo", PALAVRA_CHAVE);
	public static final PortugolToken RECEBE = new PortugolToken("<-",ATRIBUIDOR);
	public static final PortugolToken BARRA_N = new PortugolToken("\n", INDEFINIDO);
	public static final PortugolToken BARRA_T = new PortugolToken("   ", INDEFINIDO);
	public static final PortugolToken STRING = new PortugolToken("caractere", TIPO_DE_DADO);
	public static final PortugolToken DOUBLE = new PortugolToken("real", TIPO_DE_DADO);
	public static final PortugolToken INTEGER = new PortugolToken("inteiro", TIPO_DE_DADO);
	public static final PortugolToken LOGICO = new PortugolToken("logico", TIPO_DE_DADO);
	public static final PortugolToken CONST = new PortugolToken("var",PALAVRA_CHAVE);
	public static final PortugolToken VAR = new PortugolToken("var",PALAVRA_CHAVE);
	public static final PortugolToken IGUAL = new PortugolToken(":", TIPIFICACAO);
	public static final PortugolToken DOIS_PONTOS = new PortugolToken(":", TIPIFICACAO);
	public static final PortugolToken FIRST_BEGIN = new PortugolToken("inicio", INICIO);
	public static final PortugolToken THEN = new PortugolToken("entao", PARTE_DO_SE);
	public static final PortugolToken ELSE = new PortugolToken("senao", PARTE_DO_SE);
	public static final PortugolToken IF_STRUCTURE = new PortugolToken("se",PALAVRA_CHAVE);
	public static final PortugolToken WHILE_STRUCTURE = new PortugolToken("enquanto",PALAVRA_CHAVE);
	public static final PortugolToken FOR_STRUCTURE = new PortugolToken("para", TIPIFICACAO);
	public static final PortugolToken SWITCH_STRUCTURE = new PortugolToken("escolha", TIPIFICACAO);
	public static final PortugolToken REPEAT_STRUCTURE = new PortugolToken("repita", INICIO);	
	public static final PortugolToken WRITE = new PortugolToken("escreva (", METODO);
	public static final PortugolToken READ = new PortugolToken("leia (", METODO);
	public static final PortugolToken FECHA_PARENTESES = new PortugolToken(")",SIMBOLO);
	public static final PortugolToken ATRIBUICAO_DO_FOR = new PortugolToken("de", PARTE_DO_FOR);
	public static final PortugolToken TO = new PortugolToken("ate",PARTE_DO_FOR);
	public static final PortugolToken DO = new PortugolToken("faca",DO_SOMETHING);
	public static final PortugolToken UNTIL = new PortugolToken("ate",PARTE_DO_REPITA);
	public static final PortugolToken FALSE = new PortugolToken("falso",VALOR_LOGICO);
	public static final PortugolToken TRUE = new PortugolToken("verdadeiro",VALOR_LOGICO);


	
	
	/*esse m�todo recebe o nome do do programa em pascal {program NAME} e
	*devolve a nomea��o como � feita no portugol "NAME"
	*/
	
	public static PortugolToken getNameOfAlgoritmo(String programName){
		
		return new PortugolToken("\""+programName+"\"",NOMEACAO_DO_PROGRAMA);
	}
	
	
	/*
	 * Esse m�todo retorna o tipo em portugol referente a um tipo em Pascal.
	 */
	public static PortugolToken getPortugolTipo(String tipo){
		
		if(tipo.equalsIgnoreCase("string")){
			return STRING;
		}else if(tipo.equalsIgnoreCase("double")){
			return DOUBLE;
		}else if(tipo.equalsIgnoreCase("integer")){
			return INTEGER;
		}else if(tipo.equalsIgnoreCase("boolean")){
			return LOGICO;
		}else{
			System.out.println("Tipo desconhecido na tradu��o!!!");
			return new PortugolToken("tipo desconhecido", TIPO_DE_DADO);
		}
		
	}
	
	public static PortugolToken getIdentificatorToken(String conteudo){
		
		return new PortugolToken(conteudo,IDENTIFICADOR);
	
	}
	
	public static PortugolToken getEndByBeginStructure(String structureType){
		
		return new PortugolToken("fim"+structureType, PALAVRA_CHAVE);
		
	}
	
	public static PortugolToken getPortugolString(String stringComAspasSimples){
		
		String str = stringComAspasSimples.replaceAll("\'", "\"");
		
		return new PortugolToken(str,STRING_TYPE);
	}
	
	public static PortugolToken getLogicoValueByBoolean(String booleanValue){
		
		PortugolToken pt = null;
		
		if(booleanValue.equalsIgnoreCase(BOOLEAN_FALSE)){
			
			pt = PortugolBook.FALSE;
			
		}else if(booleanValue.equalsIgnoreCase(BOOLEAN_TRUE)){
			
			pt = PortugolBook.TRUE;
			
		}
		
		return pt;
		
	}
	
}
