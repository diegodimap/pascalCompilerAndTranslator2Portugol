package analises;

import tabela.Expressoes;
import tabela.Token;
import gui.NitroPascal;
import java.util.ArrayList;

public class AnaliseSintatica {

    private ArrayList<String> lidosSintatica = new ArrayList<String>();
    public ArrayList<Token> tokens = null;
    public Token actualToken = null;
    private int indexToken = 0;

    public AnaliseSintatica(ArrayList<Token> saidaLexica) {
        tokens = saidaLexica;
    }

    public Token getToken() {
        if (indexToken < tokens.size()) {
            actualToken = tokens.get(indexToken);
        }
        return actualToken;
    }

    public void okToken() {
        indexToken++;
    }

//    ---------------------------------------------------------------------------------
    public Token currentToken() {
        actualToken = getToken();
        return actualToken;
    }

    public void tokenOK() {
        okToken();
    }

    private void erroSintatico(Token currentToken, String string) {
        lidosSintatica.add(("ERRO NA LINHA " + currentToken.getLinha() + " = O Token " + currentToken.getConteudo() + " não é um " + string + " !"));
    }

    public void iniciarDescidaRecursiva() {
        initDescida();
    }

    public boolean initDescida() {
        String p = programa();
        lidosSintatica.add(p);
        if (currentToken() == null) {
            return false;
        }
        return true;
    }

    public String programa() {

        if (currentToken().getConteudo().equals("program")) {
            lidosSintatica.add(" 'program'");
            tokenOK();
        } else {
            erroSintatico(currentToken(), "program");
            return "ERRO";
        }

        // verifica a precisença de um identificador identificando a palavra.
        if (currentToken().getTipo().equals(Expressoes.IDENTIFICADOR)) {
            lidosSintatica.add(" IDENTIFICADOR");
            tokenOK();
        } else {
            erroSintatico(currentToken(), Expressoes.IDENTIFICADOR);
            return "ERRO";
        }

        if (currentToken().getTipo().equals(Expressoes.PONTO_E_VIRGULA)) {
            lidosSintatica.add(" ';'");
            tokenOK();
        } else {
            erroSintatico(currentToken(), Expressoes.PONTO_E_VIRGULA);
            return "ERRO";
        }

        // analiza o bloco de declaração do código, com o bloco var e o bloco
        // const
        if (currentToken().getConteudo().equalsIgnoreCase("const")) {
            lidosSintatica.add(" 'const'");
            tokenOK();
        }
        if (declaracao().equalsIgnoreCase("ERRO")) {
            return "ERRO";
        }

        if (var().equalsIgnoreCase("ERRO")) {
            return "ERRO";
        }

        // analiza um escopo de código, com delimiitado com begin e end e ';'
        String escopo = escopo();
        if (escopo.equalsIgnoreCase("ERRO")) {
            return "ERRO";
        }

        if (currentToken() == null) {

            for (Token t : AnaliseLexica.getTokens()) {
                NitroPascal np = new NitroPascal();
                np.adicionarResultadoSintatica(t, true);
            }

            return "Programa compilado";
        } else {
            erroSintatico(currentToken(), "Nada!!!");
            return "ERRO de sintaxe: código extra inválido, procure o último end . e apague o que tiver depois";
        }
    }

    // início dos métodos referentes a descida recursiva
    private String declaracao() {

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
            lidosSintatica.add(" identificador");
            tokenOK();
            if (currentToken().getConteudo().equalsIgnoreCase("=")) {
                lidosSintatica.add(" '='");
                tokenOK();
                if (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO) ||
                        currentToken().getTipo().equalsIgnoreCase(Expressoes.STRING) ||
                        currentToken().getTipo().equalsIgnoreCase(Expressoes.TRUE_FALSE)) {
                    lidosSintatica.add(" " + currentToken().getTipo());
                    tokenOK();
                    if (currentToken().getTipo().equalsIgnoreCase(Expressoes.PONTO_E_VIRGULA)) {
                        lidosSintatica.add(" ';'");
                        tokenOK();
                        declaracao();
                    }
                }
            }
        }
        return "";
    }

    private String var() {
        if (currentToken().getConteudo().equalsIgnoreCase("var")) {
            lidosSintatica.add(" 'var'");
            tokenOK();
            tipificacao();

        } else if (currentToken().getConteudo().equalsIgnoreCase("begin")) {
            return "";
        } else {
            return "ERRO";
        }

        return "";
    }

    private String tipificacao() {
        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
            lidosSintatica.add(" IDENTIFICADOR");
            tokenOK();

        } else {
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.DOIS_PONTOS)) {
            lidosSintatica.add(" ':'");
            tokenOK();

        } else {
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.TIPO)) {
            lidosSintatica.add(" tipo");
            tokenOK();

        } else {
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.PONTO_E_VIRGULA)) {
            lidosSintatica.add(" ';'");
            tokenOK();

        } else {
            return "ERRO";
        }

        if (tipificacao().equalsIgnoreCase("ERRO")) {
            return "ERRO";
        }

        if (currentToken().getConteudo().equals("begin")) {
            escopo();
        }

        return "";
    }

// * instanceids -> id := exp | id := exp, instanceids | id | id ,
// instanceids
    private String instanceIds() {
        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.DOIS_PONTOS)) {
            lidosSintatica.add(" ':'");
            tokenOK();

        } else {
            erroSintatico(currentToken(), Expressoes.DOIS_PONTOS);
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO)) {
            lidosSintatica.add(" NUMERO");
            tokenOK();

            if (currentToken().getTipo().equalsIgnoreCase(
                    Expressoes.PONTO_E_VIRGULA)) {
                lidosSintatica.add(" ';'");
                tokenOK();

            } else {
                erroSintatico(currentToken(), Expressoes.PONTO_E_VIRGULA);
                return "ERRO";
            }

        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.VIRGULA)) {
            lidosSintatica.add(" ','");
            tokenOK();

            if (instanceIds().equals("ERRO")) {
                return "ERRO";
            }

        }
        return "";
    }

    public String escopo() {
        if (currentToken().getConteudo().equalsIgnoreCase("begin")) {
            lidosSintatica.add(" 'begin'");
            tokenOK();

        } else {
            erroSintatico(currentToken(), "begin");
            return "ERRO";
        }

        if (codigo().equalsIgnoreCase("ERRO")) {
            return "ERRO";
        }

        if (currentToken().getConteudo().equalsIgnoreCase("end")) {
            lidosSintatica.add(" 'end'");
            tokenOK();

        } else {
            erroSintatico(currentToken(), "end");
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(
                Expressoes.PONTO_E_VIRGULA)) {
            lidosSintatica.add(" ';'");
            tokenOK();

        } else if (currentToken().getConteudo().equalsIgnoreCase(".")) {
            lidosSintatica.add(" '.'");
            tokenOK();

            if (currentToken() != null) {
                return "ERRO";
            }

        } else {
            erroSintatico(currentToken(), "ponto e virgula");
            return "ERRO";
        }

        return "";
    }

    private String codigo() {
        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.TIPO)) {
            lidosSintatica.add(" TIPO");
            if (tipificacao().equalsIgnoreCase("ERRO")) {
                return "ERRO";
            }

            if (codigo().equalsIgnoreCase("ERRO")) {
                return "ERRO";
            }

        } else if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IF)) {
            if (ifPascal().equalsIgnoreCase("ERRO")) {
                return "ERRO";
            }

            if (codigo().equalsIgnoreCase("ERRO")) {
                return "ERRO";
            }

        } else if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
            lidosSintatica.add(" IDENTIFICADOR");
            tokenOK();

            if (currentToken().getConteudo().equalsIgnoreCase(":=")) {
                lidosSintatica.add(" ':='");
                tokenOK();

            } else {
                erroSintatico(currentToken(), ":=");
                return "ERRO";
            }

            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO) ||
                    currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR) ||
                    currentToken().getTipo().equalsIgnoreCase(Expressoes.TRUE_FALSE) ||
                    currentToken().getTipo().equalsIgnoreCase(Expressoes.STRING)) {
                lidosSintatica.add(" numero|id");
                tokenOK();

            } else {
                erroSintatico(currentToken(), "NUMERO");
                return "ERRO";
            }

            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.PONTO_E_VIRGULA)) {
                lidosSintatica.add(" ';'");
                tokenOK();
            } else {
                operadorMaisNumero();
            }

            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.PONTO_E_VIRGULA)) {
                lidosSintatica.add(" ';'");
                tokenOK();
            }
            if (codigo().equalsIgnoreCase("ERRO")) {
                return "ERRO";
            }

        } else if (actualToken.getTipo().equalsIgnoreCase(Expressoes.FOR)) {
            if (forPascal().equalsIgnoreCase("ERRO")) {
                return "ERRO";
            }

            if (codigo().equalsIgnoreCase("ERRO")) {
                return "ERRO";
            }

        } else if (actualToken.getTipo().equalsIgnoreCase(Expressoes.REPEAT)) {
            if (repeatPascal().equalsIgnoreCase("ERRO")) {
                return "ERRO";
            }

            if (codigo().equalsIgnoreCase("ERRO")) {
                return "ERRO";
            }

        } else if (actualToken.getTipo().equalsIgnoreCase(Expressoes.WHILE)) {
            if (whilePascal().equalsIgnoreCase("ERRO")) {
                return "ERRO";
            }

            if (codigo().equalsIgnoreCase("ERRO")) {
                return "ERRO";
            }

        } else if (actualToken.getTipo().equalsIgnoreCase(Expressoes.WRITE)) {
            lidosSintatica.add(" write(");
            tokenOK();

            writePascal();

            codigo();

        } else if (actualToken.getTipo().equalsIgnoreCase(Expressoes.READ)) {
            lidosSintatica.add(" read(");
            tokenOK();

            readPascal();

            codigo();

        }




        return "";
    }

    public String operadorMaisNumero() {
        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.OPERADORES_MATEMATICOS)) {
            lidosSintatica.add(" op mat");
            tokenOK();
            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO) ||
                    currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR) ||
                    currentToken().getTipo().equalsIgnoreCase(Expressoes.STRING) ) {
                lidosSintatica.add(" num|id");
                tokenOK();

                if (currentToken().getTipo().equalsIgnoreCase(Expressoes.PONTO_E_VIRGULA)) {
                    return "";
                } else {
                    if (currentToken().getTipo().equalsIgnoreCase(Expressoes.OPERADORES_MATEMATICOS)) {
                        operadorMaisNumero();
                    }
                }
            }
        }

        return "";
    }

    private void writePascal() {
        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.STRING) || currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
            lidosSintatica.add(" " + currentToken().getTipo());
            tokenOK();

            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.FECHA_PARENTESES)) {
                lidosSintatica.add(" ')'");
                tokenOK();

                if (currentToken().getTipo().equalsIgnoreCase(Expressoes.PONTO_E_VIRGULA)) {
                    lidosSintatica.add(" ';'");
                    tokenOK();

                } else {
                    lidosSintatica.add("ERRO: esperava ';'");
                }

            } else {
                lidosSintatica.add("ERRO: esperava ')'");
            }

        } else {
            lidosSintatica.add("ERRO: esperava 'string'");
        }

    }

    private void readPascal() {
        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
            lidosSintatica.add(" " + currentToken().getTipo());
            tokenOK();

            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.FECHA_PARENTESES)) {
                lidosSintatica.add(" ')'");
                tokenOK();

                if (currentToken().getTipo().equalsIgnoreCase(Expressoes.PONTO_E_VIRGULA)) {
                    lidosSintatica.add(" ';'");
                    tokenOK();

                } else {
                    lidosSintatica.add("ERRO: esperava ';'");
                }

            } else {
                lidosSintatica.add("ERRO: esperava ')'");
            }

        } else {
            lidosSintatica.add("ERRO: esperava 'identificador'");
        }

    }

    private String forPascal() {
        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.FOR)) {
            lidosSintatica.add(" 'for'");
            tokenOK();

        } else {
            erroSintatico(currentToken(), "FOR");
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
            lidosSintatica.add(" identificador");
            tokenOK();

        } else {
            erroSintatico(currentToken(), "identificador");
            return "ERRO";
        }

        if (currentToken().getConteudo().equalsIgnoreCase(":=")) {
            lidosSintatica.add(" ':='");
            tokenOK();

        } else {
            erroSintatico(currentToken(), ":=");
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO) || actualToken.getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
            lidosSintatica.add(" numero");
            tokenOK();

        } else {
            erroSintatico(currentToken(), "numero");
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.TO)) {
            lidosSintatica.add(" 'to'");
            tokenOK();

        } else {
            erroSintatico(currentToken(), "TO");
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO) || actualToken.getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
            lidosSintatica.add(" " + actualToken.getTipo());
            tokenOK();

        } else {
            erroSintatico(currentToken(), "Numero ou Identificador");
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.DO)) {
            lidosSintatica.add(" 'do'");
            tokenOK();

        } else {
            erroSintatico(currentToken(), "DO");
            return "ERRO";
        }

        if (escopo().equalsIgnoreCase("ERRO")) {
            return "ERRO";
        }

        return "";
    }

    private String repeatPascal() {
        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.REPEAT)) {
            lidosSintatica.add(" 'repeat'");
            tokenOK();

        } else {
            erroSintatico(currentToken(), "REPEAT");
            return "ERRO";
        }

        if (codigo().equalsIgnoreCase("ERRO")) {
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.UNTIL)) {
            lidosSintatica.add(" 'until'");
            tokenOK();

        } else {
            erroSintatico(currentToken(), "UNTIL");
            return "ERRO";
        }

        if ((currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) || (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO))) {
            lidosSintatica.add(" IDENTIFICADOR");
            tokenOK();

        } else {
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(
                Expressoes.OPERADORES_LOGICOS)) {
            lidosSintatica.add(" 'operador lógico'");
            tokenOK();

        } else {
            return "ERRO";
        }

        if ((currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) || (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO))) {
            lidosSintatica.add(" IDENTIFICADOR");
            tokenOK();

        } else {
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(
                Expressoes.PONTO_E_VIRGULA)) {
            lidosSintatica.add(" ';'");
            tokenOK();

        } else {
            erroSintatico(currentToken(), "PONTO E VIRGULA");
            return "ERRO";
        }

        return "";
    }

    private String whilePascal() {
        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.WHILE)) {
            lidosSintatica.add(" 'while'");
            tokenOK();

        } else {
            erroSintatico(currentToken(), "WHILE");
            return "ERRO";
        }

        if ((currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) || (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO))) {
            lidosSintatica.add(" IDENTIFICADOR");
            tokenOK();

        } else {
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(
                Expressoes.OPERADORES_LOGICOS)) {
            lidosSintatica.add(" 'operador lógico'");
            tokenOK();

        } else {
            return "ERRO";
        }

        if ((currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) || (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO))) {
            lidosSintatica.add(" IDENTIFICADOR");
            tokenOK();

        } else {
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.DO)) {
            lidosSintatica.add(" 'do'");
            tokenOK();

        } else {
            erroSintatico(currentToken(), "DO");
            return "ERRO";
        }

        if (escopo().equalsIgnoreCase("ERRO")) {
            return "ERRO";
        }

        return "";
    }

    public String ifPascal() {
        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IF)) {
            lidosSintatica.add(" 'if'");
            tokenOK();

        } else {
            return "ERRO";
        }

        if ((currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) || (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO))) {
            lidosSintatica.add(" IDENTIFICADOR");
            tokenOK();

        } else {
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(
                Expressoes.OPERADORES_LOGICOS)) {
            lidosSintatica.add(" 'operador lógico'");
            tokenOK();

        } else {
            return "ERRO";
        }

        if ((currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) || (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO))) {
            lidosSintatica.add(" IDENTIFICADOR");
            tokenOK();

        } else {
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.THEN)) {
            lidosSintatica.add(" 'then'");
            tokenOK();

        } else {
            return "ERRO";
        }

        if (escopo().equalsIgnoreCase("ERRO")) {
            return "ERRO";
        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.ELSE)) {
            lidosSintatica.add(" 'else'");
            tokenOK();

        } else {
            return ""; //volta se não tiver ELSE
        }

        if (escopo().equalsIgnoreCase("ERRO")) {
            return "ERRO";
        }

        return "";
    }

    public ArrayList getLidosSintatica() {
        return this.lidosSintatica;
    }

// * IF -> if EXPRESSÂO-BOOLEAN then escopo EXPB-> num opB val | id op val
// val
// * -> num | id
// * atribuicao -> := exp | h instanceids -> id atribuicao | id atribuicao,
// * instanceids
// * decraração -> var
// * tipificacao -> tipo instanceids ; | tipo instanceids ; tipificacao
// * instanceids -> id := exp | id := exp, instanceids | id | id ,
// instanceids
}