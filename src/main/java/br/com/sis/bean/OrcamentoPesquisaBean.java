package br.com.sis.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Empresa;
import br.com.sis.entity.Orcamento;
import br.com.sis.enuns.TipoEmpresa;
import br.com.sis.repository.EmpresaRepository;
import br.com.sis.repository.OrcamentoRepository;
import br.com.sis.repository.filter.EmpresaFilter;
import br.com.sis.repository.filter.OrcamentoFilter;
import br.com.sis.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class OrcamentoPesquisaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EmpresaRepository empresaRepository;

	@Inject
	private OrcamentoRepository orcamentoRepository;

	@Getter
	private List<Orcamento> orcamentos = new ArrayList<Orcamento>();

	@Getter
	@Setter
	private Orcamento orcamentoSelecionado;

	@Getter
	private List<Empresa> mantenedoras;

	@Getter
	private List<Empresa> clientes;

	@Getter
	@Setter
	private OrcamentoFilter filter;

	public void inicializar() {
		EmpresaFilter filterMantenedora = new EmpresaFilter();
		filterMantenedora.setTipoEmpresa(TipoEmpresa.MANTENEDORA);
		mantenedoras = empresaRepository.listAll(filterMantenedora);
		filter = new OrcamentoFilter();
		if (mantenedoras.size() > 0) {
			filter.setMantenedora(mantenedoras.get(0));
			aoSelelecionarMantenedora();
		} else {
			FacesUtil.addWarnMessage("Não existe Mantenedora cadastrada. Verifique!");
		}
	}

	public void aoSelelecionarMantenedora() {
		EmpresaFilter filterCliente = new EmpresaFilter();
		filterCliente.setTipoEmpresa(TipoEmpresa.CLIENTE);
		filterCliente.setMantenedora(filter.getMantenedora());
		clientes = empresaRepository.listAll(filterCliente);
	}

	public void pesquisar() {
		if (mantenedoras.size() > 0)
			orcamentos = orcamentoRepository.listAll(filter, null);
		else
			FacesUtil.addWarnMessage("Não existe Mantenedora cadastrada. Verifique!");
	}
	
	public void excluir() {
		if (orcamentoRepository.remover(this.orcamentoSelecionado)) {
			FacesUtil.addInfoMessage("Orçmanento excluído com sucesso");
			orcamentos = orcamentoRepository.listAll(filter, null);
		}
	}
	
	public BigDecimal getTotalItens() {
		BigDecimal total = BigDecimal.ZERO;
		for (Orcamento orcamento : this.orcamentos) {
			total = total.add(orcamento.getValorOrcamento());
		}
		
		return total;
	}	

}
