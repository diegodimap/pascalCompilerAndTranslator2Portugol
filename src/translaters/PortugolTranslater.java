package translaters;

import java.util.ArrayList;
import java.util.Stack;
import tabela.Expressoes;
import tabela.Token;

public class PortugolTranslater {

    private ArrayList<PortugolToken> portugolTranslatedSourceCode = new ArrayList<PortugolToken>();
    private ArrayList<Token> pascalSourceCode = new ArrayList<Token>();
    private Stack<PortugolToken> pilhaDeControle = new Stack<PortugolToken>();
    private static PortugolTranslater translater = null;
    private boolean isVarIncluded = false;
    public Token actualToken = null;
    public int indexToken;
    private Stack<String> arrayDeConstates = new Stack<String>();
    private int nivelDeIdentacao = 0;

    /*C�digo da descida recursiva para fazer o ITERATOR sobre o ArrayList de token em Pascal.
     *
     */
    private Token currentToken() {
        Token actualToken = null;
        if (indexToken < pascalSourceCode.size()) {
            actualToken = pascalSourceCode.get(indexToken);
        }
        return actualToken;
    }

    public void tokenOK() {
        indexToken++;
    }

    //---------------------------------------------------------------------------------------------
    /*
     * Construtor do tradutor
     */
    public PortugolTranslater() {
    }

    public static PortugolTranslater getTranslater() {
        if (translater == null) {
            translater = new PortugolTranslater();
        }
        return translater;
    }

    /* M�todo que realiza a tradu��o de uma c�digo sintaticamente correto em Pascal
     *  para um t�mbem sintaticamente correto em Portugol.
     *
     *  Similar � descida recursiva
     */
    public static void setInstanciaNull() {
        if (translater != null) {
            translater = null;
        }
    }

    public ArrayList<PortugolToken> translate(ArrayList<Token> tokenizedSourceCode) {

        pascalSourceCode.addAll(tokenizedSourceCode);

        indexToken = 0;
        initTranslate();

        pascalSourceCode.clear();
        pascalSourceCode = null;
        return portugolTranslatedSourceCode;
    }

    private boolean initTranslate() {

        //Colocar algo parecido para gerar uma sequncia de informa��es sobre a tradu��o

        ////lidosSintatica.add(p);

        String p = program();

        if (currentToken() == null) {
            return false;
        }
        return true;
    }

    private String program() {

        //lidosSintatica.add(" 'program'");
			/*
         * A primeira coisa do programa precisa ser um program, esse program � traduzido para pelo PortugolBook
         * o inicio do programa em  POrtugol inicia uma estrutura de c�digo que � finalizada com o fimalgoritmo
         * � UM ArrayList que formar� o c�sdigo portugol � adicionado cada Token gerado pela tradu��o
         * A pilha vai gerenciar o inicio e o fim de estruturas como o algoritmo ---fim algoritmo ou o se --- fimse
         */
        pilhaDeControle.push(PortugolBook.PROGRAM);

        portugolTranslatedSourceCode.add(PortugolBook.PROGRAM);

        tokenOK();


        portugolTranslatedSourceCode.add(PortugolBook.getNameOfAlgoritmo(currentToken().getConteudo()));
        tokenOK();

        portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);
        portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);
        tokenOK();

        // analiza o bloco de declaração do código, com o bloco var e o bloco
        // const
        if (currentToken().getConteudo().equalsIgnoreCase("const")) {
            //lidosSintatica.add(" 'const'");
            if (isVarIncluded == false) {
                portugolTranslatedSourceCode.add(PortugolBook.CONST);
                portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);
                isVarIncluded = true;
            }
            tokenOK();
        }

        declaracao();

        if (var().equalsIgnoreCase("ERRO")) {
            return "ERRO";
        }

        // analiza um escopo de código, com delimiitado com begin e end e ';'
        escopo();

        if (currentToken() == null) {
            int cont = 0;
            while (cont < portugolTranslatedSourceCode.size()) {
                //TODO System.out.print(portugolTranslatedSourceCode.get(cont).getConteudo() + "  ");
                cont++;
            }
            return "Programa Traduzido";
        }

        return null;
    }

    private String declaracao() {
        String dec = "";
        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
            portugolTranslatedSourceCode.add(PortugolBook.getIdentificatorToken(currentToken().getConteudo()));
            dec = dec.concat(currentToken().getConteudo());
            dec = dec.concat(" " + PortugolBook.RECEBE.getConteudo() + " ");

            tokenOK();

            portugolTranslatedSourceCode.add(PortugolBook.IGUAL);

            tokenOK();

            if (currentToken().getTipoPascal().equalsIgnoreCase(PortugolBook.STRING_TYPE)) {
                dec = dec.concat(PortugolBook.getPortugolString(currentToken().getConteudo()).getConteudo());
            } else if (currentToken().getTipoPascal().equalsIgnoreCase(PortugolBook.BOOLEAN_TYPE)) {
                dec = dec.concat(PortugolBook.getLogicoValueByBoolean(currentToken().getConteudo()).getConteudo());
            } else {
                dec = dec.concat(currentToken().getConteudo());
            }
            portugolTranslatedSourceCode.add(PortugolBook.getPortugolTipo(currentToken().getTipoPascal()));

            tokenOK();

            portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);

            tokenOK();

            //Estas atribui��es v�o ser escritas no escopo  do algoritmo, pois mapeia uma declara��es de constantes.
            arrayDeConstates.push(dec);

            declaracao();

        }
        return "";



    }

    private String var() {
        if (currentToken().getConteudo().equalsIgnoreCase("var")) {
            if (isVarIncluded == false) {
                portugolTranslatedSourceCode.add(PortugolBook.VAR);

                portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);

                isVarIncluded = true;
            }
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
            portugolTranslatedSourceCode.add(PortugolBook.getIdentificatorToken(currentToken().getConteudo()));

            tokenOK();

            portugolTranslatedSourceCode.add(PortugolBook.DOIS_PONTOS);


            tokenOK();

            portugolTranslatedSourceCode.add(PortugolBook.getPortugolTipo(currentToken().getConteudo()));

            tokenOK();

            portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);


            tokenOK();

            tipificacao();
        }
        return "";



    }
    private static PortugolToken currentStructureBegin = null;
    private boolean isFirstBeginTranslated = false;

    public String escopo() {

        if (currentToken().getConteudo().equalsIgnoreCase("begin")) {
            if (!isFirstBeginTranslated) {

                portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);
                portugolTranslatedSourceCode.add(PortugolBook.FIRST_BEGIN);

                portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);
                portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);

                while (!arrayDeConstates.empty()) {
                    portugolTranslatedSourceCode.add(new PortugolToken(arrayDeConstates.pop(), "DECLARACAO_DE_CONSTANTES"));
                    portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);
                }
                isFirstBeginTranslated = true;
            } else {
                pilhaDeControle.push(currentStructureBegin);
                portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);
            }

            nivelDeIdentacao++;

            tokenOK();

            codigo();

            nivelDeIdentacao--;

            int cont = 0;
            while (cont < nivelDeIdentacao) {
                portugolTranslatedSourceCode.add(PortugolBook.BARRA_T);
                cont++;
            }

            portugolTranslatedSourceCode.add(PortugolBook.getEndByBeginStructure(pilhaDeControle.pop().getConteudo()));

            tokenOK();

            portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);


            tokenOK();

        }
        return "";

    }

    private String codigo() {



        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.TIPO)) {
            int cont = 0;
            while (cont < nivelDeIdentacao) {
                portugolTranslatedSourceCode.add(PortugolBook.BARRA_T);
                cont++;
            }

            portugolTranslatedSourceCode.add(PortugolBook.getPortugolTipo(currentToken().getTipo()));


            tipificacao();

            codigo();


        } else if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IF)) {
            int cont = 0;
            while (cont < nivelDeIdentacao) {
                portugolTranslatedSourceCode.add(PortugolBook.BARRA_T);
                cont++;
            }

            portugolTranslatedSourceCode.add(PortugolBook.IF_STRUCTURE);

            currentStructureBegin = PortugolBook.IF_STRUCTURE;

            ifPascal();

            codigo();

        } else if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
            int cont = 0;
            while (cont < nivelDeIdentacao) {
                portugolTranslatedSourceCode.add(PortugolBook.BARRA_T);
                cont++;
            }

            portugolTranslatedSourceCode.add(PortugolBook.getIdentificatorToken(currentToken().getConteudo()));


            tokenOK();

            if (currentToken().getConteudo().equalsIgnoreCase(":=")) {


                portugolTranslatedSourceCode.add(PortugolBook.RECEBE);


                tokenOK();

            }
            if (currentToken().getTipoPascal().equalsIgnoreCase(PortugolBook.STRING_TYPE)) {
                portugolTranslatedSourceCode.add(PortugolBook.getPortugolString(currentToken().getConteudo()));
            } else if (currentToken().getTipoPascal().equalsIgnoreCase(PortugolBook.BOOLEAN_TYPE)) {
                portugolTranslatedSourceCode.add(PortugolBook.getLogicoValueByBoolean(currentToken().getConteudo()));
            } else {
                portugolTranslatedSourceCode.add(new PortugolToken(currentToken().getConteudo(), currentToken().getTipoPascal()));
            }

            tokenOK();

            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.PONTO_E_VIRGULA)) {
                portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);

                tokenOK();
            } else {
                operadorMaisNumero();
            }

            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.PONTO_E_VIRGULA)) {
                portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);

                tokenOK();
            }

            codigo();

        } else if (currentToken().getTipo().equalsIgnoreCase(Expressoes.FOR)) {
            int cont = 0;
            while (cont < nivelDeIdentacao) {
                portugolTranslatedSourceCode.add(PortugolBook.BARRA_T);
                cont++;
            }

            portugolTranslatedSourceCode.add(PortugolBook.FOR_STRUCTURE);

            currentStructureBegin = PortugolBook.FOR_STRUCTURE;

            forPascal();

            codigo();

        } else if (currentToken().getTipo().equalsIgnoreCase(Expressoes.REPEAT)) {
            int cont = 0;
            while (cont < nivelDeIdentacao) {
                portugolTranslatedSourceCode.add(PortugolBook.BARRA_T);
                cont++;
            }

            portugolTranslatedSourceCode.add(PortugolBook.REPEAT_STRUCTURE);

            currentStructureBegin = PortugolBook.REPEAT_STRUCTURE;

            repeatPascal();

            codigo();

        } else if (currentToken().getTipo().equalsIgnoreCase(Expressoes.WHILE)) {
            int cont = 0;
            while (cont < nivelDeIdentacao) {
                portugolTranslatedSourceCode.add(PortugolBook.BARRA_T);
                cont++;
            }

            portugolTranslatedSourceCode.add(PortugolBook.WHILE_STRUCTURE);

            currentStructureBegin = PortugolBook.WHILE_STRUCTURE;

            whilePascal();

            codigo();

        } else if (currentToken().getTipo().equalsIgnoreCase(Expressoes.WRITE)) {
            int cont = 0;
            while (cont < nivelDeIdentacao) {
                portugolTranslatedSourceCode.add(PortugolBook.BARRA_T);
                cont++;
            }

            portugolTranslatedSourceCode.add(PortugolBook.WRITE);

            tokenOK();

            writePascal();

            codigo();

        } else if (currentToken().getTipo().equalsIgnoreCase(Expressoes.READ)) {
            int cont = 0;
            while (cont < nivelDeIdentacao) {
                portugolTranslatedSourceCode.add(PortugolBook.BARRA_T);
                cont++;
            }

            portugolTranslatedSourceCode.add(PortugolBook.READ);

            tokenOK();

            readPascal();

            codigo();

        }


        return "";
    }

    public String ifPascal() {

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IF)) {
            tokenOK();
        }

        if ((currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) || (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO))) {
            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
                portugolTranslatedSourceCode.add(PortugolBook.getIdentificatorToken(currentToken().getConteudo()));
            } else {
                portugolTranslatedSourceCode.add(new PortugolToken(currentToken().getConteudo(), currentToken().getTipoPascal()));
            }

            tokenOK();
        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.OPERADORES_LOGICOS)) {
            portugolTranslatedSourceCode.add(new PortugolToken(currentToken().getConteudo(), PortugolBook.OPERADOR_LOGICO));


            tokenOK();
        }

        if ((currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) || (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO))) {
            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
                portugolTranslatedSourceCode.add(PortugolBook.getIdentificatorToken(currentToken().getConteudo()));
            } else {
                portugolTranslatedSourceCode.add(new PortugolToken(currentToken().getConteudo(), currentToken().getTipoPascal()));
            }


            tokenOK();

        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.THEN)) {
            portugolTranslatedSourceCode.add(PortugolBook.THEN);

            tokenOK();

        }

        escopo();

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.ELSE)) {
            portugolTranslatedSourceCode.add(PortugolBook.ELSE);

            tokenOK();

        } else {
            return ""; //volta se não tiver ELSE
        }

        if (escopo().equalsIgnoreCase("ERRO")) {
            return "ERRO";
        }

        return "";
    }

    public String operadorMaisNumero() {
        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.OPERADORES_MATEMATICOS)) {
            portugolTranslatedSourceCode.add(new PortugolToken(currentToken().getConteudo(), PortugolBook.OPERADOR_MATEMATICO));

            tokenOK();

            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO) || currentToken().getTipo().equalsIgnoreCase(Expressoes.STRING) ||
                    currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
                if (currentToken().getTipo().equalsIgnoreCase(Expressoes.STRING)) {
                    portugolTranslatedSourceCode.add(PortugolBook.getPortugolString(currentToken().getConteudo()));
                } else if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
                    portugolTranslatedSourceCode.add(PortugolBook.getIdentificatorToken(currentToken().getConteudo()));
                } else {
                    portugolTranslatedSourceCode.add(new PortugolToken(currentToken().getConteudo(), currentToken().getTipoPascal()));
                }

                tokenOK();

                if (currentToken().getTipo().equalsIgnoreCase(Expressoes.PONTO_E_VIRGULA)) {
                    portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);

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
            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
                portugolTranslatedSourceCode.add(PortugolBook.getIdentificatorToken(currentToken().getConteudo()));

            } else {
                portugolTranslatedSourceCode.add(PortugolBook.getPortugolString(currentToken().getConteudo()));

            }
            tokenOK();

            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.FECHA_PARENTESES)) {
                portugolTranslatedSourceCode.add(PortugolBook.FECHA_PARENTESES);

                tokenOK();

                if (currentToken().getTipo().equalsIgnoreCase(Expressoes.PONTO_E_VIRGULA)) {
                    portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);

                    tokenOK();
                }

            }
        }

    }

    private void readPascal() {
        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
            portugolTranslatedSourceCode.add(PortugolBook.getIdentificatorToken(currentToken().getConteudo()));

            tokenOK();

            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.FECHA_PARENTESES)) {
                portugolTranslatedSourceCode.add(PortugolBook.FECHA_PARENTESES);

                tokenOK();

                if (currentToken().getTipo().equalsIgnoreCase(Expressoes.PONTO_E_VIRGULA)) {
                    portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);

                    tokenOK();
                }

            }

        }

    }

    private String forPascal() {
        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.FOR)) {
            tokenOK();

        }
        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
            portugolTranslatedSourceCode.add(PortugolBook.getIdentificatorToken(currentToken().getConteudo()));


            tokenOK();

        }

        if (currentToken().getConteudo().equalsIgnoreCase(":=")) {
            portugolTranslatedSourceCode.add(PortugolBook.ATRIBUICAO_DO_FOR);


            tokenOK();

        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO) || actualToken.getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
                portugolTranslatedSourceCode.add(PortugolBook.getIdentificatorToken(currentToken().getConteudo()));

            } else {
                portugolTranslatedSourceCode.add(new PortugolToken(currentToken().getConteudo(), currentToken().getTipoPascal()));

            }
            tokenOK();
        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.TO)) {
            portugolTranslatedSourceCode.add(PortugolBook.TO);

            tokenOK();

        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO) || actualToken.getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
                portugolTranslatedSourceCode.add(PortugolBook.getIdentificatorToken(currentToken().getConteudo()));

            } else {
                portugolTranslatedSourceCode.add(new PortugolToken(currentToken().getConteudo(), currentToken().getTipoPascal()));

            }
            tokenOK();

        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.DO)) {
            portugolTranslatedSourceCode.add(PortugolBook.DO);

            tokenOK();

        }

        if (escopo().equalsIgnoreCase("ERRO")) {
            return "ERRO";
        }

        return "";
    }

    private String whilePascal() {
        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.WHILE)) {
            tokenOK();
        }

        if ((currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) || (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO))) {
            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
                portugolTranslatedSourceCode.add(PortugolBook.getIdentificatorToken(currentToken().getConteudo()));

            } else {
                portugolTranslatedSourceCode.add(new PortugolToken(currentToken().getConteudo(), currentToken().getTipoPascal()));

            }
            tokenOK();
        }

        if (currentToken().getTipo().equalsIgnoreCase(
                Expressoes.OPERADORES_LOGICOS)) {
            portugolTranslatedSourceCode.add(new PortugolToken(currentToken().getConteudo(), PortugolBook.OPERADOR_LOGICO));

            tokenOK();
        }

        if ((currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) || (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO))) {

            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
                portugolTranslatedSourceCode.add(PortugolBook.getIdentificatorToken(currentToken().getConteudo()));

            } else {
                portugolTranslatedSourceCode.add(new PortugolToken(currentToken().getConteudo(), currentToken().getTipoPascal()));

            }
            tokenOK();
        }

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.DO)) {
            portugolTranslatedSourceCode.add(PortugolBook.DO);

            tokenOK();
        }



        if (escopo().equalsIgnoreCase("ERRO")) {
            return "ERRO";
        }

        return "";
    }

    private String repeatPascal() {
        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.REPEAT)) {
            tokenOK();

        }

        nivelDeIdentacao++;

        if (codigo().equalsIgnoreCase("ERRO")) {
            return "ERRO";
        }

        nivelDeIdentacao--;

        if (currentToken().getTipo().equalsIgnoreCase(Expressoes.UNTIL)) {
            portugolTranslatedSourceCode.add(PortugolBook.UNTIL);
            tokenOK();

        }

        if ((currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) || (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO))) {
            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
                portugolTranslatedSourceCode.add(PortugolBook.getIdentificatorToken(currentToken().getConteudo()));

            } else {
                portugolTranslatedSourceCode.add(new PortugolToken(currentToken().getConteudo(), currentToken().getTipoPascal()));

            }
            tokenOK();
        }

        if (currentToken().getTipo().equalsIgnoreCase(
                Expressoes.OPERADORES_LOGICOS)) {
            portugolTranslatedSourceCode.add(new PortugolToken(currentToken().getConteudo(), PortugolBook.OPERADOR_LOGICO));
            tokenOK();
        }

        if ((currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) || (currentToken().getTipo().equalsIgnoreCase(Expressoes.NUMERO))) {
            if (currentToken().getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
                portugolTranslatedSourceCode.add(PortugolBook.getIdentificatorToken(currentToken().getConteudo()));
            } else {
                portugolTranslatedSourceCode.add(new PortugolToken(currentToken().getConteudo(), currentToken().getTipoPascal()));
            }
            tokenOK();
        }

        if (currentToken().getTipo().equalsIgnoreCase(
                Expressoes.PONTO_E_VIRGULA)) {
            portugolTranslatedSourceCode.add(PortugolBook.BARRA_N);
            tokenOK();
        }

        return "";
    }
}
