/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tabela;

import java.util.Collection;
import java.util.Vector;

/**
 *
 * @author bosco
 */
public class Expressoes extends Vector<Expressao>{
    private static final long serialVersionUID = 1L;
	public static final String PONTO_E_VIRGULA = "ponto e virgula";
	public static final String OPERADORES_LOGICOS = "operadores lógicos";
	public static final String FECHA_PARENTESES = "fecha parenteses";
	public static final String PALAVRA_RESERVADA = "palavra reservada";
	public static final String TIPO = "tipo";
	public static final String NUMERO = "numero";
	public static final String CASE = "case";
	public static final String SWITCH = "switch";
	public static final String ELSE = "else";
	public static final String THEN = "then";
	public static final String IF = "if";
	public static final String STRING = "String";
	public static final String IDENTIFICADOR = "identificador";
	public static final String OPERADORES_MATEMATICOS = "operadores matemáticos";
	public static final String ABRE_PARENTESES = "abre parent";
	public static final String VIRGULA = "virgula";
	public static final String DOIS_PONTOS = "dois_pontos";
	public static final String PONTO = "ponto";
	public static final String FOR = "for";
	public static final String REPEAT = "repeat";
	public static final String TO = "to";
	public static final String DO = "do";
	public static final String UNTIL = "until";
	public static final String WHILE = "while";
	public static final String COMENTARIO = "comentario";
	public static final String WRITE = "write";
	public static final String READ = "read";
    public static final String TRUE_FALSE = "true_false";
    public static final String ATRIBUICAO = "atribuicao";
    public static final String NAO_RECONHECIDO = "Não Reconhecido";

    public Expressoes(Collection<? extends Expressao> c) {
        super(c);
    }

    public Expressoes() {
    }

    public Expressoes(int initialCapacity) {
        super(initialCapacity);
    }

    public Expressoes(int initialCapacity, int capacityIncrement) {
        super(initialCapacity, capacityIncrement);
    }


}
