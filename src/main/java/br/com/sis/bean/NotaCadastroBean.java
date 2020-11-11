package br.com.sis.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.ItemOrcamento;
import br.com.sis.entity.Orcamento;
import br.com.sis.security.Seguranca;
import br.com.sis.service.OrcamentoService;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class NotaCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private OrcamentoService orcamentoService;
	

	@Inject
	private Seguranca seguranca;	

	@Getter
	@Setter
	private Orcamento orcamento;

	@Getter
	@Setter
	@NotNull
	private Long numeroNota;
	
	@Getter
	@Setter
	private String serieNota;	

	@Getter
	@Setter
	private LocalDate dataEmissaoNota;

	@Getter
	@Setter
	private LocalDate dataPrevisaoPagamento;

	@Getter
	private List<ItemOrcamento> itens;
		

	public void inicializar() {
		if (this.orcamento != null) {
			itens = this.orcamento.getItensOrcamento();
			if (this.orcamento.getNumeroNfse() != null)
				this.numeroNota = this.orcamento.getNumeroNfse();
			if (this.orcamento.getSerieNfse() != null)
				this.serieNota = this.orcamento.getSerieNfse();
			
			if (this.orcamento.getDataEmissaoNota() != null)
				this.dataEmissaoNota = this.orcamento.getDataEmissaoNota();
			else
				this.dataEmissaoNota = LocalDate.now();
			
			if (this.orcamento.getDataPrevisaoPagamento() != null)
				this.dataPrevisaoPagamento = this.orcamento.getDataPrevisaoPagamento();
			else
				this.dataPrevisaoPagamento = this.dataEmissaoNota.plusDays(this.orcamento.getCliente().getPrazoPagamento());
		} else {
			FacesUtil.redirecionarPagina("Erro.xhtml");
		}

	}

	public void salvarPedido() {
		this.orcamento.setNumeroNfse(this.numeroNota);
		if (!StringUtils.isEmpty(this.serieNota))
			this.orcamento.setSerieNfse(this.serieNota);
		this.orcamento.setDataEmissaoNota(this.dataEmissaoNota);
		this.orcamento.setDataPrevisaoPagamento(this.dataPrevisaoPagamento);
		this.orcamento = orcamentoService.salvar(this.orcamento);
		FacesUtil.addInfoMessage("Nota fiscal salva com sucesso!");
	}
	
	public boolean isPodeLimparNota() {
		return this.orcamento.getNumeroNfse() != null && this.orcamento.getDataEfetivaPagamento() == null && seguranca.isPermiteLimparNota();
	}
	
	public void limparNota() {
		this.numeroNota = null;
		this.serieNota = null;
		this.orcamento.setNumeroNfse(null);
		this.orcamento.setDataEmissaoNota(null);
		this.orcamento.setDataPrevisaoPagamento(null);
		this.orcamento.setSerieNfse(null);
		this.orcamento = orcamentoService.salvar(this.orcamento);
		
		FacesUtil.addWarnMessage("Nota fiscal retirada!");
	}	

}
