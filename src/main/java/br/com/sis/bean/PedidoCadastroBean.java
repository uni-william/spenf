package br.com.sis.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.validator.constraints.NotEmpty;

import br.com.sis.entity.ItemOrcamento;
import br.com.sis.entity.Orcamento;
import br.com.sis.service.OrcamentoService;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class PedidoCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private OrcamentoService orcamentoService;

	@Getter
	@Setter
	private Orcamento orcamento;

	@Getter
	@Setter
	@NotEmpty
	private String numeroPedido;

	@Getter
	@Setter
	private LocalDate dataPedido;

	@Getter
	@Setter
	private LocalDate dataEntrega;

	@Getter
	private List<ItemOrcamento> itens;

	public void inicializar() {
		if (this.orcamento != null) {
			itens = this.orcamento.getItensOrcamento();
			if (this.orcamento.getPedidoCliente() != null)
				this.numeroPedido = this.orcamento.getPedidoCliente();
			if (this.orcamento.getDataRecebimentoPedido() != null)
				this.dataPedido = this.orcamento.getDataRecebimentoPedido();
			if (this.orcamento.getDataEntregaPedido() != null)
				this.dataEntrega = this.orcamento.getDataEntregaPedido();
		} else {
			FacesUtil.redirecionarPagina("Erro.xhtml");
		}

	}

	public void salvarPedido() {
		this.orcamento.setPedidoCliente(this.numeroPedido);
		this.orcamento.setDataRecebimentoPedido(this.dataPedido);
		this.orcamento.setDataEntregaPedido(this.dataEntrega);
		this.orcamento = orcamentoService.salvar(this.orcamento);
		FacesUtil.redirecionarPaginaComMensagemInformativa(
				"orcamentos/CadastroOrcamento.xhtml?orcamento=" + this.orcamento.getId().toString(),
				"Pedido do cliente registro com sucesso!");
	}

}
