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
	PEDIDO_VIEW("Pedido - Visualizar"),
	PEDIDO_UPD("Pedido - Atualizar"),
	PERFIL_VIEW("Perfil de acesso - Visualizar"),
	PERFIL_INS("Perfil de acesso - Cadastrar"),
	PERFIL_UPD("Perfil de acesso - Atualizar"),
	PERFIL_DEL("Perfil de acesso - Excluir"),
	COLABORADOR_VIEW("Colaborador - Visualizar"),
	COLABORADOR_INS("Colaborador - Cadastrar"),
	COLABORADOR_UPD("Colaborador - Atualizar"),
	COLABORADOR_DEL("Colaborador - Excluir");	
	
	private Funcionalidade(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}	
}
