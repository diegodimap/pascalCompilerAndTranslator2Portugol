package translaters;

public class PortugolToken {
	
	private String tipo;
	private String conteudo;
	
	 public PortugolToken(String conteudo, String tipo) {
	        this.conteudo = conteudo;
	        this.tipo = tipo;
	 }
	 
	 public PortugolToken(){
		 
	 }

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	
	public void setPortugolToken(String conteudo, String tipo) {
        this.conteudo = conteudo;
        this.tipo = tipo;
    }
	 
	 
	
}
