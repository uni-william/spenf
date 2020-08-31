package br.com.sis.enuns;

public enum TipoRegime {

	SIMPLES_NACIONAL("Simples Nacional", "1"),
	SIMPLES_EXCESSO("Simples Nacional - excesso de sublimite da receita bruta", "2"),
	NORMAL("Regime Normal", "3"), 
	CLIENTE("Cliente sem regime definido", "");
	
	private TipoRegime(String descricao, String code) {
		this.descricao = descricao;
		this.code = code;
	}
	private String code;
	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	public String getCode() {
		return code;
	}
	
	
}
