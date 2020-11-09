package br.com.sis.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.ItemOrcamento;
import br.com.sis.entity.Orcamento;
import br.com.sis.security.Seguranca;
import br.com.sis.service.OrcamentoService;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class PagamentoCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private OrcamentoService orcamentoService;

	@Getter
	@Setter
	private Orcamento orcamento;

	@Getter
	@Setter
	private LocalDate dataPagamento;
	
	@Getter
	@Setter
	private BigDecimal desconto;

	@Getter
	private List<ItemOrcamento> itens;
	
	@Inject
	private Seguranca seguranca;
	
	@Getter
	@Setter	
	private String origem;

	public void inicializar() {
		if (this.orcamento != null) {
			itens = this.orcamento.getItensOrcamento();
			if (this.orcamento.getDataEfetivaPagamento() != null)
				this.dataPagamento = this.orcamento.getDataEfetivaPagamento();
			if (this.orcamento.getDescontos() != null) {
				this.desconto = this.orcamento.getDescontos();
			}
		} else {
			FacesUtil.redirecionarPagina("Erro.xhtml");
		}

	}

	public void salvarPedido() {
		this.orcamento.setDataEfetivaPagamento(this.dataPagamento);
		this.orcamento.setDescontos(this.desconto);
		this.orcamento = orcamentoService.salvar(this.orcamento);
		FacesUtil.addInfoMessage("Pagamento registrado com sucesso!");
	}
	
	public void limparPagamento() {
		this.orcamento.setDataEfetivaPagamento(null);
		this.orcamento.setDescontos(null);
		this.orcamento = orcamentoService.salvar(this.orcamento);
		this.dataPagamento = null;
		FacesUtil.addWarnMessage("Pagamento retirado!");
	}
	
	public boolean isPermitirLimparPagamento() {
		return this.orcamento.getDataEfetivaPagamento() != null && seguranca.isPermiteLimparPagamento(); 
	}
	
	public String getDescricaoRetorno() {
		if (origem == null || !origem.equals("2")) {
			return "Pesquisa de pedidos";
			
		} else {
			return "Previsão de pagamento";
		}
	}
	
	public void retornarPaginaAnterior() {
		if (origem == null || !origem.equals("2")) {
			FacesUtil.redirecionarPagina("pedidos/PesquisaPedidos.xhtml");
		} else {
			FacesUtil.redirecionarPagina("pedidos/PrevisaoPagamento.xhtml");
		}		
	}

}
