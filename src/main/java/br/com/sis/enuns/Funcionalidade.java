package br.com.sis.enuns;

public enum Funcionalidade {
	
	MANTENEDORA_VIEW("Mantenedora - Visualizar"),
	MANTENEDORA_INS("Mantenedora - Cadastrar"),
	MANTENEDORA_UPD("Mantenedora - Atualizar"),
	MANTENEDORA_DEL("Mantenedora - Excluir"),
	CLIENTE_VIEW("Cliente - Visualizar"),
	CLIENTE_INS("Cliente - Cadastrar"),
	CLIENTE_UPD("Cliente - Atualizar"),
	CLIENTE_DEL("Cliente - Excluir"),
	ORCAMENTO_VIEW("Orçamento - Visualizar"),
	ORCAMENTO_INS("Orçamento - Cadastrar"),
	ORCAMENTO_UPD("Orçamento - Atualizar"),
	ORCAMENTO_DEL("Orçamento - Excluir"),
	PEDIDO_CONSULTA("Consultar Situação de Pedidos"),
	PEDIDO_EM_ABERTO("Consultar Pedidos em aberto");	
	
	private Funcionalidade(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}	
}
