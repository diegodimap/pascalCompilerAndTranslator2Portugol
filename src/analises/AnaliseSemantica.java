package analises;

import java.util.ArrayList;
import tabela.Expressoes;
import tabela.Token;

public class AnaliseSemantica {

    ArrayList<Token> t = new ArrayList<Token>();
    ArrayList<Token> tokensDeclarados = new ArrayList<Token>();
    ArrayList<String> msgs = new ArrayList<String>();

    public AnaliseSemantica(ArrayList<Token> tokens) {
        t = tokens;
    }

    public ArrayList<Token> adicionarTiposPascal() {
        //verifica todos os números para colocar o tipo pascal
        for (Token to : t) {
            if (to.getTipo().equals(Expressoes.NUMERO)) {
                to.setTipoPascal(classificar(to));
                to.setValor(to.getConteudo());
            } else {
                if (to.getConteudo().equals("+") || to.getConteudo().equals("-") || to.getConteudo().equals("*") || to.getConteudo().equals("/")) {
                    to.setTipoPascal("OPERADOR_MATEMATICO");
                } else {
                    if (to.getConteudo().equals(";")) {
                        to.setTipoPascal("PONTO_E_VIRGULA");
                    } else {
                        if (to.getConteudo().equals(":=")) {
                            to.setTipoPascal("ATRIBUICAO");
                        } else {
                            if (to.getConteudo().equals(".")) {
                                to.setTipoPascal("PONTO");
                            } else {
                                if (to.getTipo().equalsIgnoreCase(Expressoes.PALAVRA_RESERVADA)) {
                                    to.setTipoPascal("PALAVRA_RESERVADA");
                                } else {
                                    if (to.getTipo().equalsIgnoreCase(Expressoes.DOIS_PONTOS)) {
                                        to.setTipoPascal("DOIS_PONTOS");
                                    } else {
                                        if (to.getTipo().equalsIgnoreCase(Expressoes.TIPO)) {
                                            to.setTipoPascal("TIPO");
                                        } else {
                                            if (to.getTipo().equalsIgnoreCase(Expressoes.STRING)) {
                                                to.setTipoPascal("string");
                                            } else {
                                                if (to.getTipoPascal().equalsIgnoreCase("")) {
                                                    to.setTipoPascal("nada");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //classifica as constantes
        for (int i = 0; i < t.size(); i++) {
            Token esq = null;
            Token dir = null;

            if (t.get(i).getConteudo().equals("const")) { //fechado
                int j = i + 1;
                while (!t.get(j).getConteudo().equals("var") && !t.get(j).getConteudo().equals("begin") && j < t.size()) {
                    t.get(j).setTipoPascal(classificar(t.get(j + 2)));
                    msgs.add("Contante " + t.get(j).getConteudo() + " classificado como " + t.get(j).getTipoPascal() + " e recebe o valor " + t.get(j + 2).getConteudo() + " na linha " + t.get(j).getLinha());
                    isDeclarado(t.get(j), true);
                    t.get(j).setValor(t.get(j + 2).getConteudo());
                    t.get(j).setConstante(true);
                    j += 4;
                }
                i = j;
            }
        }
        //classifica as variáveis
        for (int i = 0; i < t.size(); i++) {
            Token esq = null;
            Token dir = null;

            if (t.get(i).getConteudo().equals("var") && i > 2 && i < t.size()) { //fechado
                int j = i + 1;
                while (!t.get(j).getConteudo().equals("begin") && j < t.size()) {
                    if (t.get(j + 2).getTipo().equalsIgnoreCase(Expressoes.TIPO)) {
                        t.get(j).setTipoPascal(t.get(j + 2).getConteudo());
                        msgs.add("Token: " + t.get(j).getConteudo() + " classificado como " + t.get(j).getTipoPascal() + " na linha " + t.get(j).getLinha());
                        isDeclarado(t.get(j), true);
                    }
                    j += 4;
                }
                i = j;
            }
        }
        //verifica a inicialização das variáveis----------------------------------------
        for (int i = 0; i < t.size(); i++) {
            Token esq = null;
            Token dir = null;

            if (t.get(i).getConteudo().equalsIgnoreCase(":=") && t.get(i + 2).getConteudo().equals(";")) { //fechado, faltam as exp
                esq = t.get(i - 1);
                dir = t.get(i + 1);
                if (esq.getTipo().equalsIgnoreCase(Expressoes.IDENTIFICADOR)) {
                    if (!isDeclarado(t.get(i - 1), false)) {
                        msgs.add("ERRO: O identificador " + esq.getConteudo() + " não foi declarado na linha " + esq.getLinha());
                    } else {
                        if (!classificar(dir).equalsIgnoreCase(getTipo(esq))) {
                            if (esq.isConstante()) {
                                msgs.add("ERRO: Tentando atribuir valor à uma contante " + esq.getConteudo() + " na linha  " + esq.getLinha());
                            } else {
                                msgs.add("ERRO: Tentando atribuir tipos diferentes na linha " + esq.getLinha() + ": " + getTipo(esq) + " != " + classificar(dir) + " Tokens: " + esq.getConteudo() + " e " + dir.getConteudo());
                            }
                        } else {
                            String texto = "";
                            if (dir.getTipoPascal().equalsIgnoreCase("integer") || dir.getTipoPascal().equalsIgnoreCase("double") || dir.getTipo().equalsIgnoreCase(Expressoes.STRING)) {
                                esq.setValor(dir.getConteudo());
                                setValorTodos(esq, dir.getConteudo());
                                texto = ("A VARIÁVEL " + esq.getConteudo() + " RECEBE: " + dir.getConteudo());
                            } else {
                                if (dir.getValor() != null) {
                                    esq.setValor(dir.getValor());
                                    texto = ("A VARIÁVEL " + esq.getConteudo() + " RECEBE: " + dir.getValor());
                                }
                            }
                            msgs.add(texto + " na linha " + esq.getLinha());
                        }
                    }

                    if (dir.getTipoPascal().equalsIgnoreCase("string")) {
                        esq.setValor(dir.getConteudo());
                        String texto = ("A VARIÁVEL : " + esq.getConteudo() + " RECEBE: " + dir.getConteudo());
                        msgs.add(texto + " na linha " + esq.getLinha());
                    }
                }
            }
        }
        //verifica as operações lógicas ------------------------------------------
        for (int i = 0; i < t.size(); i++) {
            Token esq = null;
            Token dir = null;
            if (t.get(i).getTipo().equalsIgnoreCase(Expressoes.OPERADORES_LOGICOS)) {
                esq = t.get(i - 1);
                dir = t.get(i + 1);

                if (esq.getTipoPascal().equalsIgnoreCase(dir.getTipoPascal())) {
                    msgs.add("Operador lógico " + t.get(i).getConteudo() + " encontrado na linha " + t.get(i).getLinha() + ". Lado esquerdo do tipo " + getTipo(esq) + " e lado direito do tipo " + classificar(dir) + ". VÁLIDA");
                } else {
                    System.out.println("OP L DIFERENTES");
                    msgs.add("ERRO: Operador lógico " + t.get(i).getConteudo() + " encontrado na linha " + t.get(i).getLinha() + ". Lado esquerdo do tipo " + getTipo(esq) + " e lado direito do tipo " + classificar(dir) + ". INVÁLIDA");
                }
            }
        }
        //verifica as operações matemáticas----------------------------------------
        for (int i = 0; i < t.size(); i++) {
            Token esq = null;
            Token dir = null;
            if (t.get(i).getTipo().equalsIgnoreCase(Expressoes.OPERADORES_MATEMATICOS)) {
                Token resultado = t.get(i - 3);
                while (!t.get(i).getConteudo().equalsIgnoreCase(";") && i > 2 && i < t.size()) {
                    esq = t.get(i - 1);
                    dir = t.get(i + 1);

                    String texto = "";

                    //trata da divisão-------------------------------------------
                    if (t.get(i).getConteudo().equalsIgnoreCase("/")) {
                        if (dir.getConteudo().equalsIgnoreCase("0")) {
                            msgs.add("ERRO: Tentativa de divisão por zero na linha " + esq.getLinha());
                        } else {
                            if (esq.getTipoPascal().equalsIgnoreCase(dir.getTipoPascal()) && (!esq.getTipoPascal().equalsIgnoreCase("string") && !dir.getTipoPascal().equalsIgnoreCase("string"))) {
                                if (!isDeclarado(esq, false) && !isDeclarado(dir, false)) {
                                    if (!esq.getConteudo().contains(".")) {
                                        double r = Integer.parseInt(esq.getConteudo()) / Integer.parseInt(dir.getConteudo());
                                        texto += ("Resultado da divisão é " + r + " e vai para " + resultado.getConteudo());
                                        setValorTodos(resultado, r + "");
                                    } else {
                                        double r = Double.parseDouble(esq.getConteudo()) / Double.parseDouble(dir.getConteudo());
                                        texto += ("Resultado da divisão é " + r + " e vai para " + resultado.getConteudo());
                                        setValorTodos(resultado, r + "");
                                    }
                                } else {
                                    //se forem variáveis
                                    if (!esq.getConteudo().contains(".")) {
                                        double r = Integer.parseInt(esq.getValor()) / Integer.parseInt(dir.getValor());
                                        texto += ("Resultado da divisão é " + r + " e vai para " + resultado.getConteudo());
                                        setValorTodos(resultado, r + "");
                                    } else {
                                        double r = Double.parseDouble(esq.getValor()) / Double.parseDouble(dir.getValor());
                                        texto += ("Resultado da divisão é " + r + " e vai para " + resultado.getConteudo());
                                        setValorTodos(resultado, r + "");
                                    }
                                }
                            } else {
                                //se não forem inteiros
                                }
                            if (esq.getTipoPascal().equalsIgnoreCase(dir.getTipoPascal())) {
                                msgs.add("Operador matemático " + t.get(i).getConteudo() + " encontrado na linha " + esq.getLinha() + ". Lado esquerdo do tipo " + esq.getTipoPascal() + " e lado direito do tipo " + dir.getTipoPascal() + ". " + texto + " VÁLIDA");
                            } else {
                                msgs.add("ERRO: Operador matemático " + t.get(i).getConteudo() + " encontrado na linha " + esq.getLinha() + ". Lado esquerdo do tipo " + esq.getTipoPascal() + " e lado direito do tipo " + dir.getTipoPascal() + ". INVÁLIDA");
                            }
                        }//fim else não é por 0
                    }//fim divisão

                    //trata da multiplicação-------------------------------------------
                    if (t.get(i).getConteudo().equalsIgnoreCase("*")) {
                        if (esq.getTipoPascal().equalsIgnoreCase(dir.getTipoPascal()) && (!esq.getTipoPascal().equalsIgnoreCase("string") && !dir.getTipoPascal().equalsIgnoreCase("string"))) {
                            if (!isDeclarado(esq, false) && !isDeclarado(dir, false)) {
                                if (!esq.getConteudo().contains(".")) {
                                    double r = Integer.parseInt(esq.getConteudo()) * Integer.parseInt(dir.getConteudo());
                                    texto += ("Resultado da multiplicação é " + r + " e vai para " + resultado.getConteudo());
                                    setValorTodos(resultado, r + "");
                                } else {
                                    double r = Double.parseDouble(esq.getConteudo()) * Double.parseDouble(dir.getConteudo());
                                    texto += ("Resultado da multiplicação é " + r + " e vai para " + resultado.getConteudo());
                                    setValorTodos(resultado, r + "");
                                }
                            } else {
                                //se forem variáveis
                                if (!esq.getConteudo().contains(".")) {
                                    double r = Integer.parseInt(esq.getValor()) * Integer.parseInt(dir.getValor());
                                    texto += ("Resultado da multiplicação é " + r + " e vai para " + resultado.getConteudo());
                                    setValorTodos(resultado, r + "");
                                } else {
                                    double r = Double.parseDouble(esq.getValor()) * Double.parseDouble(dir.getValor());
                                    texto += ("Resultado da multiplicação é " + r + " e vai para " + resultado.getConteudo());
                                    setValorTodos(resultado, r + "");
                                }
                            }
                        } else {
                            //se não forem inteiros
                        }
                        if (esq.getTipoPascal().equalsIgnoreCase(dir.getTipoPascal())) {
                            msgs.add("Operador matemático " + t.get(i).getConteudo() + " encontrado na linha " + esq.getLinha() + ". Lado esquerdo do tipo " + esq.getTipoPascal() + " e lado direito do tipo " + dir.getTipoPascal() + ". " + texto + " VÁLIDA");
                        } else {
                            msgs.add("ERRO: Operador matemático " + t.get(i).getConteudo() + " encontrado na linha " + esq.getLinha() + ". Lado esquerdo do tipo " + esq.getTipoPascal() + " e lado direito do tipo " + dir.getTipoPascal() + ". INVÁLIDA");
                        }
                    }//fim multiplicação

                    //trata da adição----------------------------------------------
                    if (t.get(i).getConteudo().equalsIgnoreCase("+")) {
                        if (esq.getTipoPascal().equalsIgnoreCase(dir.getTipoPascal())) {
                            if (!esq.getTipo().equalsIgnoreCase("string") && !dir.getTipo().equalsIgnoreCase("string")) {
                                if (!isDeclarado(esq, false) && !isDeclarado(dir, false)) {
                                    if (!esq.getConteudo().contains(".")) {
                                        double r = Integer.parseInt(esq.getConteudo()) + Integer.parseInt(dir.getConteudo());
                                        texto += ("Resultado da adição é " + r + " e vai para " + resultado.getConteudo());
                                        setValorTodos(resultado, r + "");
                                    } else {
                                        double r = Double.parseDouble(esq.getConteudo()) + Double.parseDouble(dir.getConteudo());
                                        texto += ("Resultado da adição é " + r + " e vai para " + resultado.getConteudo());
                                        setValorTodos(resultado, r + "");
                                    }
                                } else {
                                    //se forem variáveis
                                    if (!esq.getConteudo().contains(".")) {
                                        double r = Integer.parseInt(esq.getValor()) + Integer.parseInt(dir.getValor());
                                        texto += ("Resultado da adição é " + r + " e vai para " + resultado.getConteudo());
                                        setValorTodos(resultado, r + "");
                                    } else {
                                        double r = Double.parseDouble(esq.getValor()) + Double.parseDouble(dir.getValor());
                                        texto += ("Resultado da adição é " + r + " e vai para " + resultado.getConteudo());
                                        setValorTodos(resultado, r + "");
                                    }
                                }
                            } else {
                                //trata da concatenação de strings
                                if (esq.getTipo().equalsIgnoreCase("string") && dir.getTipo().equalsIgnoreCase("string")) {
                                    String r = esq.getConteudo().substring(0, esq.getConteudo().length() - 1) +
                                            dir.getConteudo().substring(1);
                                    texto += ("Resultado da concatenação de strings é " + r + " na linha " + esq.getLinha() + ".");
                                    setValorTodos(resultado, r);
                                //msgs.add("Operador matemático " + t.get(i).getConteudo() + " encontrado na linha " + esq.getLinha() + ". Lado esquerdo do tipo " + esq.getTipoPascal() + " e lado direito do tipo " + dir.getTipoPascal() + ". " + texto + " VÁLIDA");
                                } else {
                                    msgs.add("ERRO: Tentando somar string com outra coisa qualquer na linha" + esq.getLinha());
                                }
                            }
                        } else {
                            //se não forem inteiros
                        }
                        if (esq.getTipoPascal().equalsIgnoreCase(dir.getTipoPascal())) {
                            msgs.add("Operador matemático " + t.get(i).getConteudo() + " encontrado na linha " + esq.getLinha() + ". Lado esquerdo do tipo " + esq.getTipoPascal() + " e lado direito do tipo " + dir.getTipoPascal() + ". " + texto + " VÁLIDA");
                        } else {
                            msgs.add("ERRO: Operador matemático " + t.get(i).getConteudo() + " encontrado na linha " + esq.getLinha() + ". Lado esquerdo do tipo " + esq.getTipoPascal() + " e lado direito do tipo " + dir.getTipoPascal() + ". INVÁLIDA");
                        }
                    }//fim adição

                    //trata da subtração-------------------------------------------
                    if (t.get(i).getConteudo().equalsIgnoreCase("-")) {
                        if (esq.getTipoPascal().equalsIgnoreCase(dir.getTipoPascal()) && (!esq.getTipoPascal().equalsIgnoreCase("string") && !dir.getTipoPascal().equalsIgnoreCase("string"))) {
                            if (!isDeclarado(esq, false) && !isDeclarado(dir, false)) {
                                if (!esq.getConteudo().contains(".")) {
                                    double r = Integer.parseInt(esq.getConteudo()) - Integer.parseInt(dir.getConteudo());
                                    texto += ("Resultado da subtração é " + r + " e vai para " + resultado.getConteudo());
                                    setValorTodos(resultado, r + "");
                                } else {
                                    double r = Double.parseDouble(esq.getConteudo()) - Double.parseDouble(dir.getConteudo());
                                    texto += ("Resultado da subtração é " + r + " e vai para " + resultado.getConteudo());
                                    setValorTodos(resultado, r + "");
                                }
                            } else {
                                //se forem variáveis
                                if (!esq.getConteudo().contains(".")) {
                                    double r = Integer.parseInt(esq.getValor()) - Integer.parseInt(dir.getValor());
                                    texto += ("Resultado da subtração é " + r + " e vai para " + resultado.getConteudo());
                                    setValorTodos(resultado, r + "");
                                } else {
                                    double r = Double.parseDouble(esq.getValor()) - Double.parseDouble(dir.getValor());
                                    texto += ("Resultado da subtração é " + r + " e vai para " + resultado.getConteudo());
                                    setValorTodos(resultado, r + "");
                                }
                            }
                        } else {
                            //se não forem inteiros
                        }
                        if (esq.getTipoPascal().equalsIgnoreCase(dir.getTipoPascal())) {
                            msgs.add("Operador matemático " + t.get(i).getConteudo() + " encontrado na linha " + esq.getLinha() + ". Lado esquerdo do tipo " + esq.getTipoPascal() + " e lado direito do tipo " + dir.getTipoPascal() + ". " + texto + " . VÁLIDA");
                        } else {
                            msgs.add("ERRO: Operador matemático " + t.get(i).getConteudo() + " encontrado na linha " + esq.getLinha() + ". Lado esquerdo do tipo " + esq.getTipoPascal() + " e lado direito do tipo " + dir.getTipoPascal() + " . INVÁLIDA");
                        }
                    }//fim adição

                    i += 2;
                } //while não acha ;
            } //if operador matemático
        }// for operador matemático


        //msg de teste
//        for (Token token : t) {
//            System.out.printf("%4s %10s %4s %10s %4s %10s \n", "-T: ", token.getConteudo(), " -V: ", token.getValor(), " -T: ", token.getTipoPascal() + "");
//        }


        //altera o valor das variáveis no for
        for (int i = 0; i < t.size(); i++) {
            if (t.get(i).getConteudo().equalsIgnoreCase("to")) {
                Token recebe = t.get(i - 3);
                int de = Integer.parseInt(t.get(i - 1).getConteudo());
                int para = Integer.parseInt(t.get(i + 1).getConteudo());

                for (int j = de; j < para; j++) {
                    setValorTodos(recebe, j + "");
                    msgs.add("Alterando o valor de " + recebe.getConteudo() + " para " + j + " no FOR da linha " + recebe.getLinha());
                }
            }
        }

        msgs.add("\n");
        for (Token token : tokensDeclarados) {
            msgs.add("MSG: Token " + token.getConteudo() + " tem valor " + token.getValor());
        }

        //verificar WHILE

        
        return t;
    }

    public boolean isDeclarado(Token to, boolean add) {
        boolean contem = false;
        for (Token token : tokensDeclarados) {
            if (token.getConteudo().equalsIgnoreCase(to.getConteudo())) {
                contem = true;
                break;
            }
        }
        if (contem && add) {
            msgs.add("A CONSTANTE " + to.getConteudo() + " NA LINHA " + to.getLinha() + " JÁ FOI DECLARADA");
        } else {
            tokensDeclarados.add(to);
            for (Token token : t) {
                if (token.getConteudo().equalsIgnoreCase(to.getConteudo())) {
                    token.setTipoPascal(to.getTipoPascal());
                }
            }
        }

        return contem;
    }

    public String getTipo(Token to) {
        for (Token token : tokensDeclarados) {
            if (token.getConteudo().equals(to.getConteudo())) {
                return token.getTipoPascal();
            }
        }

        return "";
    }

    private String classificar(Token t) {
        if ((t.getConteudo().equals("false")) || (t.getConteudo().equals("true"))) {
            return "boolean";
        } else {
            if (t.getConteudo().contains(".") && t.getTipo().equalsIgnoreCase(Expressoes.NUMERO)) {
                return "double";
            } else {
                if (t.getTipo().equals(Expressoes.STRING)) {
                    return "string";
                } else {
                    if (!t.getConteudo().contains(".") && t.getTipo().equalsIgnoreCase(Expressoes.NUMERO)) {
                        return "integer";
                    } else {
                        return "NÂO RECONHECIDO";
                    }
                }
            }
        }
    }

    public ArrayList<Token> getDeclarados() {
        return tokensDeclarados;
    }

    public ArrayList<String> getMsgErro() {
        return msgs;
    }

    private void setValorTodos(Token to, String valor) {
        for (Token token : t) {
            if (token.getConteudo().equalsIgnoreCase(to.getConteudo())) {
                token.setValor(valor);
            }
        }
    }
}
