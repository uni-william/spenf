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
	PEDIDO_CAD("Pedido - Acesso dados do pedido"),
	NOTA_CAD("Pedido - Acesso dados da nota fiscal do pedido"),
	PAGAMENTO_CAD("Pedido - Acesso dados de pagamento do pedido"),
	PAGAMENTO_CLEAR("Pedido - Permissão para limpar pagamento realizado"),
	NOTA_CLEAR("Pedido - Permissão para limpar dados da nota fiscal"),
	PEDIDO_CLEAR("Pedido - Permissão para limpar dados do pedido"),
	RESUMO_PERIODO("Pedido - Resumo por período"),
	PREVISAO_PAGAMENTO("Pedido - Previsão de pagamento"),
	PERFIL_VIEW("Perfil de acesso - Visualizar"),
	PERFIL_INS("Perfil de acesso - Cadastrar"),
	PERFIL_UPD("Perfil de acesso - Atualizar"),
	PERFIL_DEL("Perfil de acesso - Excluir"),
	COLABORADOR_VIEW("Colaborador - Visualizar"),
	COLABORADOR_INS("Colaborador - Cadastrar"),
	COLABORADOR_UPD("Colaborador - Atualizar"),
	COLABORADOR_DEL("Colaborador - Excluir"),
	DESPESA_VIEW("Despesa - Visualizar"),
	DESPESA_INS("Despesa - Cadastrar"),
	DESPESA_UPD("Despesa - Atualizar"),
	DESPESA_DEL("Despesa - Excluir"),
	TIPO_DESPESA_VIEW("Tipo Despesa - Visualizar"),
	TIPO_DESPESA_INS("Tipo Despesa - Cadastrar"),
	TIPO_DESPESA_UPD("Tipo Despesa - Atualizar"),
	TIPO_DESPESA_DEL("Tipo Despesa - Excluir"),	
	TIPO_DESPESA_SOMA("Despesa - Resumo por período"),
	FLUXO_CAIXA("Caixa - Fluxo de caixa"),
	EFETUAR_BACKUP("Efetuar Backup");	
	
	private Funcionalidade(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}	
}
