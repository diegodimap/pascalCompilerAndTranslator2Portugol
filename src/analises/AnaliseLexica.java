package analises;

import tabela.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnaliseLexica {

    private static ArrayList<Token> tokens;
    private static ArrayList<Token> erroLexico = new ArrayList<Token>();
    private Expressoes expressao = new Expressoes();
    public static AnaliseLexica analizadorLexico = null;

    public AnaliseLexica(ArrayList<Token> tokensL) {
        tokens = tokensL;

        this.preenchePalavrasChave();
        this.setTipos();

    }

    public static AnaliseLexica getAnalizadorLexico(ArrayList<Token> tokensL) {
        if (analizadorLexico == null) {
            analizadorLexico = new AnaliseLexica(tokensL);
        } else {
            analizadorLexico = null;
            return AnaliseLexica.getAnalizadorLexico(tokensL);
        }
        return analizadorLexico;
    }

    public static ArrayList<Token> getTokens() {
        return tokens;
    }

    public void preenchePalavrasChave() {
        Expressao exp;

        exp = new Expressao(Expressoes.PONTO, ".");
        expressao.add(exp);

        exp = new Expressao(Expressoes.PONTO_E_VIRGULA, ";");
        expressao.add(exp);

        exp = new Expressao(Expressoes.DOIS_PONTOS, ":");
        expressao.add(exp);

        exp = new Expressao(Expressoes.VIRGULA, ",");
        expressao.add(exp);

        exp = new Expressao(Expressoes.OPERADORES_LOGICOS, "=|>|>=|<|<=|<>");
        expressao.add(exp);

        exp = new Expressao(Expressoes.ATRIBUICAO, ":=");
        expressao.add(exp);

        exp = new Expressao(Expressoes.FECHA_PARENTESES, "\\)");
        expressao.add(exp);

        exp = new Expressao(Expressoes.ABRE_PARENTESES, "\\(");
        expressao.add(exp);

        exp = new Expressao(Expressoes.OPERADORES_MATEMATICOS, "\\+|-|\\*|/");
        expressao.add(exp);

        exp = new Expressao(Expressoes.NUMERO, "[0-9]+.?[0-9]*");
        expressao.add(exp);

        exp = new Expressao(Expressoes.IDENTIFICADOR, "[a-zA-Z]\\w*");
        expressao.add(exp);

        exp = new Expressao(Expressoes.WRITE, "write\\(");
        expressao.add(exp);

        exp = new Expressao(Expressoes.READ, "read\\(");
        expressao.add(exp);

        exp = new Expressao(Expressoes.DO, "do");
        expressao.add(exp);

        exp = new Expressao(Expressoes.TO, "to");
        expressao.add(exp);

        exp = new Expressao(Expressoes.UNTIL, "until");
        expressao.add(exp);

        exp = new Expressao(Expressoes.REPEAT, "repeat");
        expressao.add(exp);

        exp = new Expressao(Expressoes.WHILE, "while");
        expressao.add(exp);

        exp = new Expressao(Expressoes.FOR, "for");
        expressao.add(exp);

        exp = new Expressao(Expressoes.STRING, "'.*'");
        expressao.add(exp);

        exp = new Expressao(Expressoes.IF, "if");
        expressao.add(exp);

        exp = new Expressao(Expressoes.THEN, "then");
        expressao.add(exp);

        exp = new Expressao(Expressoes.ELSE, "else");
        expressao.add(exp);

        exp = new Expressao(Expressoes.SWITCH, "switch");
        expressao.add(exp);

        exp = new Expressao(Expressoes.CASE, "case");
        expressao.add(exp);

        exp = new Expressao(Expressoes.TRUE_FALSE, "true|false");
        expressao.add(exp);

        exp = new Expressao(Expressoes.TIPO, "integer|array|constructor|destructor|float|double|string|label|boolean|record|char");
        expressao.add(exp);

        exp = new Expressao(Expressoes.PALAVRA_RESERVADA, "const|begin|end|program|var|asm|div|exports|implementation|interface|inc|library|mod|nil|object|of|packed|set|shl|shr|type|unit|with");
        expressao.add(exp);

    }

    public static ArrayList<Token> getErroLexico() {
        return erroLexico;
    }

    private void setTipos() {
        Expressao exp;
        for (Token t : tokens) {
            exp = verificarPadrao(t.getConteudo());
            if (exp != null) {
                t.setTipo(exp.getKey());
            }else{
                t.setTipo(Expressoes.NAO_RECONHECIDO);
                erroLexico.add(t);
            }
        }
    }

    private Expressao verificarPadrao(String str) {
        Expressao express = null;

        for (Expressao exp : expressao) {
            Pattern p = Pattern.compile(exp.getValue());
            Matcher m = p.matcher(str);  //codigo
            while (m.matches()) {
                express = exp;
                if (express != null) {
                    break;
                }
            }
        }
        return express;
    }
}
