package tabela;

public class Token {

    private String conteudo;
    private String tipo;
    private int inicio;
    private int fim;
    private boolean local; //tá dentro de que método
    private int linha; //tá dentro de que método
    private String tipoPascal; //tá dentro de que método
    private String valor;
    private boolean constante;

    public Token(String conteudo, String tipo, int inicio, int fim, boolean local, int linha) {
        this.conteudo = conteudo;
        this.tipo = tipo;
        this.inicio = inicio;
        this.fim = fim;
        this.local = local;
        this.linha = linha;
        this.tipoPascal = "";
        this.constante = false;
    }

    public Token() {
    }
    
    public void setToken(String conteudo, String tipo, int inicio, int fim, boolean local) {
        this.conteudo = conteudo;
        this.tipo = tipo;
        this.inicio = inicio;
        this.fim = fim;
        this.local = local;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public int getFim() {
        return fim;
    }

    public void setFim(int fim) {
        this.fim = fim;
    }

    public int getInicio() {
        return inicio;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public boolean getLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

	public int getLinha() {
		return linha;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}

    public String getTipoPascal() {
        return tipoPascal;
    }

    public void setTipoPascal(String tipoPascal) {
        this.tipoPascal = tipoPascal;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public boolean isConstante() {
        return constante;
    }

    public void setConstante(boolean contante) {
        this.constante = contante;
    }

    
}
