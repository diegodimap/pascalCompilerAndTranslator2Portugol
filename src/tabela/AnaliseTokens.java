package tabela;

import java.util.ArrayList;

public class AnaliseTokens {

	private static ArrayList<String> tokensLidos = null;
	private static ArrayList<Integer> linhas = null;
	private static ArrayList<Token> tokens = new ArrayList<Token>();
	private static ArrayList<Integer> linhasCheias = new ArrayList<Integer>();
	private static ArrayList<String> comentarios = new ArrayList<String>();

	
	public static ArrayList<Token> lerSequencialmente(String codigo) {

		tokensLidos = new ArrayList<String>();
		linhas = new ArrayList<Integer>();
		int linha = 1;

		String palavra = "";
		int aspas = 0;
		int chaves = 0;
		for (int i = 0; i < codigo.length(); i++) {
			if (codigo.charAt(i) == '\'') {
				if (aspas == 1) {
					aspas = 0;
				} else {
					aspas = 1;
				}
			}
			if (codigo.charAt(i) == '{') {
				chaves = 1;
			} else {
				if (codigo.charAt(i) == '}') {
					chaves = 0;
				}
			}
			if ((aspas == 1) || (chaves == 1)) {
				if ((codigo.charAt(i) == '\n')) {
					tokensLidos.add(palavra.trim());
					linhas.add(linha++);
					palavra = "";
				} else {
					palavra = palavra + codigo.charAt(i);
				}
			} else {
				if ((codigo.charAt(i) == ' ') || (codigo.charAt(i) == '\n')) {
					tokensLidos.add(palavra.trim());
					linhas.add(linha);
					if ((codigo.charAt(i) == '\n')) {
						linha++;
					}
					palavra = "";
				} else {
					palavra = palavra + codigo.charAt(i);
				}
			}
		}

		for (int i = 0; i < tokensLidos.size(); i++) {
			if (tokensLidos.get(i).trim().length() > 0) {
				if (tokensLidos.get(i).contains("{")
						&& tokensLidos.get(i).contains("}")) {
						comentarios.add(tokensLidos.get(i));
				} else {
					linhasCheias.add(linhas.get(i));
					tokens.add(new Token(tokensLidos.get(i), null, codigo.indexOf(tokensLidos.get(i)), codigo.indexOf(tokensLidos.get(i))+codigo.indexOf(tokensLidos.get(i))+ tokensLidos.get(i).length() , false, linhas.get(i)));
				}

			} else {
				// linhas.remove(tokensLidos.indexOf(string));
			}
		}

		return tokens;
	}

    public static ArrayList<Token> getTokens() {
        return tokens;
    }

	public static ArrayList<Integer> getLinhas() {
		return linhasCheias;
	}

}
