package br.com.sis.enuns;

public enum TipoEmpresa {
	
	MANTENEDORA("Mantenedora"),
	CLIENTE("Cliente");
	
	private TipoEmpresa(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}	
}
